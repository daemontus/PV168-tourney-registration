package fi.muni.pv168;

import fi.muni.pv168.utils.DBUtils;
import fi.muni.pv168.utils.ServiceFailureException;
import org.apache.derby.iapi.types.JSQLType;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Implementation of MatchManager using JDBC.</p>
 */
public class MatchManagerImpl implements MatchManager{

    private final static String COL_ID = "ID";
    private final static String COL_KNIGHT = "KNIGHT_ID";
    private final static String COL_DISCIPLINE = "DISCIPLINE_ID";
    private final static String COL_STARTING_NUMBER = "STARTING_NUMBER";
    private final static String COL_POINTS = "POINTS";
    private final static String TABLE = "MATCHES";

    private static final Logger logger = Logger.getLogger(MatchManagerImpl.class.getName());

    private DataSource dataSource;
    private KnightManager knightManager;
    private DisciplineManager disciplineManager;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void checkEnvironment() {
        if (dataSource == null) {
            throw new IllegalStateException("Data source not set");
        }
        if (knightManager == null) {
            throw new IllegalStateException("Knight manager not set");
        }
        if (disciplineManager == null) {
            throw new IllegalStateException("Discipline manager not set");
        }
    }

    public void setKnightManager(KnightManager knightManager) {
        this.knightManager = knightManager;
    }

    public void setDisciplineManager(DisciplineManager disciplineManager) {
        this.disciplineManager = disciplineManager;
    }

    @Override
	public void createMatch(Match match) {
        checkEnvironment();

        validate(match);

        if (match.getId() != null) {
            throw new IllegalArgumentException("Match already exists (ID is not null)");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            st = conn.prepareStatement(
                    "INSERT INTO "+TABLE+
                            " ("+
                            COL_KNIGHT+
                            ","+COL_DISCIPLINE+
                            ","+ COL_STARTING_NUMBER +
                            ","+COL_POINTS+
                            ") " +
                            "VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setLong(1, match.getKnight().getId());
            st.setLong(2, match.getDiscipline().getId());
            st.setInt(3, match.getStartNumber());
            if (match.getPoints() != null) {
                st.setInt(4, match.getPoints());
            } else {
                st.setNull(4, JSQLType.INT);
            }


            int count = st.executeUpdate();

            if (count != 1) {
                throw new ServiceFailureException("Unexpected number of modified rows during Match create: "+count);
            }

            Long id = DBUtils.getId(st.getGeneratedKeys());
            match.setId(id);

            conn.commit();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error inserting match into db", ex);
            throw new ServiceFailureException("Error inserting match into db", ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
	}

	@Override
	public void updateMatch(Match match) {
        checkEnvironment();

        validate(match);

        if (match.getId() == null) {
            throw new IllegalArgumentException("Match does not exist (ID is null)");
        }

        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = dataSource.getConnection();

            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "UPDATE "+TABLE+" SET "+
                            COL_KNIGHT+" = ?, "+
                            COL_DISCIPLINE+" = ?, "+
                            COL_STARTING_NUMBER +" = ?, "+
                            COL_POINTS+" = ? " +
                            "WHERE "+COL_ID+" = ?"
            );
            st.setLong(1, match.getKnight().getId());
            st.setLong(2, match.getDiscipline().getId());
            st.setInt(3, match.getStartNumber());
            if (match.getPoints() !=null) {
                st.setInt(4, match.getPoints());
            } else {
                st.setNull(4, JSQLType.INT);
            }
            st.setLong(5, match.getId());

            int count = st.executeUpdate();

            if (count == 0) {
                throw new IllegalArgumentException("Updating non existent match, id:"+match.getId());
            }
            if (count != 1) {
                throw new ServiceFailureException("Integrity error. Updated row count: "+count);
            }

            conn.commit();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error updating match in the db", ex);
            throw new ServiceFailureException("Error updating match in the db", ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
	}

	@Override
	public void deleteMatch(Match match) {
        checkEnvironment();

        validate(match);

        if (match == null) {
            throw new IllegalArgumentException("Discipline is null.");
        }
        if (match.getId() == null) {
            throw new IllegalArgumentException("Match is not created. (ID is null)");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();

            conn.setAutoCommit(false);
            st = conn.prepareStatement("DELETE FROM "+TABLE+" WHERE "+COL_ID+" = ?");
            st.setLong(1, match.getId());

            int count = st.executeUpdate();

            if (count == 0) {
                throw new IllegalArgumentException("No such match in database");
            }
            if (count != 1) {
                throw new ServiceFailureException("Internal integrity error. Number of affected rows:"+count);
            }
            conn.commit();
            match.setId(null);

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error deleting match from the db", ex);
            throw new ServiceFailureException("Error deleting match from the db", ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
	}


    @Override
	public List<Match> findAllMatches() {
        checkEnvironment();
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT "+
                            COL_ID+
                            ", "+COL_KNIGHT+
                            ", "+COL_DISCIPLINE+
                            ", "+ COL_STARTING_NUMBER +
                            ", "+COL_POINTS+
                            " FROM "+TABLE
            );

            ResultSet rs = st.executeQuery();
            List<Match> result = new ArrayList<Match>();
            while (rs.next()) {
                result.add(rowToMatch(rs));
            }
            return result;
        } catch (SQLException ex) {
            String msg = "Error when getting all matches from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
	}

	@Override
	public Match getMatchById(Long id) {
        checkEnvironment();

        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {

            conn = dataSource.getConnection();

            st = conn.prepareStatement(
                    "SELECT "+
                            COL_ID+
                            ", "+COL_KNIGHT+
                            ", "+COL_DISCIPLINE+
                            ", "+ COL_STARTING_NUMBER +
                            ", "+COL_POINTS+
                            " FROM "+TABLE+
                            " WHERE "+COL_ID+" = ?"
            );
            st.setLong(1, id);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Match result = rowToMatch(rs);
                if (rs.next()) {
                    throw new ServiceFailureException("Internal integrity error: more matches with the same id!");
                }
                return result;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error getting match with id = " + id + " from DB", ex);
            throw new ServiceFailureException("Error getting match with id = " + id + " from DB", ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
	}

	@Override
	public List<Match> findMatchesForKnight(Knight knight) {
		checkEnvironment();

        if (knight == null) {
            throw new IllegalArgumentException("Knight cannot be null!");
        }
        if (knight.getId() == null) {
            throw new IllegalArgumentException("Knight does not exist (ID is null)");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT "+
                            COL_ID+
                            ", "+COL_KNIGHT+
                            ", "+COL_DISCIPLINE+
                            ", "+ COL_STARTING_NUMBER +
                            ", "+COL_POINTS+
                            " FROM "+TABLE+
                            " WHERE " +
                                 COL_KNIGHT+" = ?"
            );
            st.setLong(1, knight.getId());

            ResultSet rs = st.executeQuery();
            List<Match> result = new ArrayList<Match>();
            while (rs.next()) {
                result.add(rowToMatch(rs));
            }
            return result;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error getting match with knight with id = " + knight.getId()  + " from DB", ex);
            throw new ServiceFailureException("Error getting knight with id = " + knight.getId() + " from DB", ex);
        }  finally {
            DBUtils.closeQuietly(conn, st);
        }
    }

	@Override
	public List<Match> findMatchesForDiscipline(Discipline discipline) {
        checkEnvironment();

        if (discipline == null) {
            throw new IllegalArgumentException("Discipline cannot be null!");
        }
        if (discipline.getId() == null) {
            throw new IllegalArgumentException("Discipline does not exist (ID is null)");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT "+
                            COL_ID+
                            ", "+COL_KNIGHT+
                            ", "+COL_DISCIPLINE+
                            ", "+ COL_STARTING_NUMBER +
                            ", "+COL_POINTS+
                            " FROM "+TABLE+
                            " WHERE " +
                            COL_DISCIPLINE+" = ?"
            );
            st.setLong(1, discipline.getId());

            ResultSet rs = st.executeQuery();
            List<Match> result = new ArrayList<Match>();
            while (rs.next()) {
                result.add(rowToMatch(rs));
            }
            return result;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error getting match with discipline with id = " + discipline.getId()  + " from DB", ex);
            throw new ServiceFailureException("Error getting discipline with id = " + discipline.getId() + " from DB", ex);
        }  finally {
            DBUtils.closeQuietly(conn, st);
        }
	}

	@Override
	public Match findMatchForKnightAndDiscipline(Knight knight, Discipline discipline) {
        checkEnvironment();

        if (discipline == null) {
            throw new IllegalArgumentException("Discipline cannot be null!");
        }
        if (discipline.getId() == null) {
            throw new IllegalArgumentException("Discipline does not exist (ID is null)");
        }

        if (knight == null) {
            throw new IllegalArgumentException("Knight cannot be null!");
        }
        if (knight.getId() == null) {
            throw new IllegalArgumentException("Knight does not exist (ID is null)");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT " +
                            COL_ID +
                            ", " + COL_KNIGHT +
                            ", " + COL_DISCIPLINE +
                            ", " + COL_STARTING_NUMBER +
                            ", " + COL_POINTS +
                            " FROM " + TABLE +
                            " WHERE " +
                                   COL_DISCIPLINE + " = ? AND " +
                                   COL_KNIGHT + "= ?"
            );
            st.setLong(1, discipline.getId());
            st.setLong(2, knight.getId());

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Match result = rowToMatch(rs);
                if (rs.next()) {
                    throw new ServiceFailureException("Internal integrity error: more matches with the same knight and discipline!");
                }
                return result;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            String msg = "Error getting match for a knight and a discipline.";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }


    private Match rowToMatch(ResultSet r) throws SQLException {
        Match result = new Match();
        result.setId(r.getLong(COL_ID));
        result.setKnight(knightManager.getKnightById(r.getLong(COL_KNIGHT)));
        result.setDiscipline(disciplineManager.getDisciplineById(r.getLong(COL_DISCIPLINE)));
        result.setStartNumber(r.getInt(COL_STARTING_NUMBER));
        result.setPoints(r.getObject(COL_POINTS) != null ? r.getInt(COL_POINTS) : null);
        return result;
    }

    private void validate(Match match) {
        if (match == null) {
            throw new IllegalArgumentException("Match cannt be null");
        }
        if (match.getPoints() != null && match.getPoints() < 0) {
            throw new IllegalArgumentException("Match points below zero.");
        }
        if (match.getStartNumber() < 0) {
            throw new IllegalArgumentException("Match starting number below zero.");
        }
        if (match.getDiscipline() == null) {
            throw new IllegalArgumentException("Match with null discipline.");
        }
        if (match.getDiscipline().getId() == null) {
            throw new IllegalArgumentException("Match with Discipline without id. Insert discipline into db first.");
        }
        if (match.getKnight() == null) {
            throw new IllegalArgumentException("Match with null knight.");
        }
        if (match.getKnight().getId() == null) {
            throw new IllegalArgumentException("Match with Knight without id. Insert knight into db first.");
        }
    }
}