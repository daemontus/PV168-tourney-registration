package fi.muni.pv168;

import fi.muni.pv168.utils.DBUtils;
import fi.muni.pv168.utils.ServiceFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * <p>Implementation of Discipline Manager using JDBC.</p>
 */
public class DisciplineManagerImpl implements DisciplineManager {

    private static final String COL_ID = "ID";
    private static final String COL_NAME = "NAME";
    private static final String COL_START = "START_TIME";
    private static final String COL_END = "END_TIME";
    private static final String COL_MAX_PARTICIPANTS = "MAX_PARTICIPANTS";
    private static final String TABLE = "DISCIPLINES";

    private static final Calendar gmtTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

    private static final Logger logger = LoggerFactory.getLogger(DisciplineManagerImpl.class);

    private DataSource dataSource;


    /**
     * Set data source of this manager. Manager without data source cannot perform operations.
     * @param dataSource DataSource, can't be null.
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
	public void createDiscipline(Discipline discipline) {

        logger.debug("Creating discipline: "+discipline);

        checkDataSource();

        validate(discipline);

        if (discipline.getId() != null) {
            throw new IllegalArgumentException("Discipline already exists (ID is not null)");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            st = conn.prepareStatement(
                    "INSERT INTO "+TABLE+
                        " ("+
                            COL_NAME+
                            ","+COL_START+
                            ","+COL_END+
                            ","+COL_MAX_PARTICIPANTS+
                        ") " +
                    "VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, discipline.getName());
            st.setTimestamp(2, discipline.getStart(), gmtTime);
            st.setTimestamp(3, discipline.getEnd(), gmtTime);
            st.setInt(4, discipline.getMaxParticipants());

            int count = st.executeUpdate();

            if (count != 1) {
                throw new ServiceFailureException("Unexpected number of modified rows during Discipline create: "+count);
            }

            Long id = DBUtils.getId(st.getGeneratedKeys());
            discipline.setId(id);

            conn.commit();

            logger.info("Discipline successfully created: "+discipline);

        } catch (SQLException ex) {
            String message = "Error inserting discipline into db";
            logger.error(message, ex);
            throw new ServiceFailureException(message, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
	}

    @Override
	public Discipline getDisciplineById(Long id) {

        logger.debug("Getting discipline by id: "+id);

        checkDataSource();

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
                        ", "+COL_NAME+
                        ", "+COL_START+
                        ", "+COL_END+
                        ", "+COL_MAX_PARTICIPANTS+
                    " FROM "+TABLE+
                    " WHERE "+COL_ID+" = ?"
            );
            st.setLong(1, id);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Discipline result = rowToDiscipline(rs);
                if (rs.next()) {
                    throw new ServiceFailureException("Internal integrity error: more disciplines with the same id!");
                }
                logger.info("Retrieved discipline by id "+id+": "+result);
                return result;
            } else {
                logger.info("No discipline with id "+id+" in database.");
                return null;
            }

        } catch (SQLException ex) {
            String message = "Error getting discipline with id = " + id + " from DB";
            logger.error(message, ex);
            throw new ServiceFailureException(message, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
	}

    @Override
	public List<Discipline> findAllDisciplines() {

        logger.debug("Getting all disciplines");

        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT "+
                        COL_ID+
                        ", "+COL_NAME+
                        ", "+COL_START+
                        ", "+COL_END+
                        ", "+COL_MAX_PARTICIPANTS+
                    " FROM "+TABLE
            );

            ResultSet rs = st.executeQuery();
            List<Discipline> result = new ArrayList<Discipline>();
            while (rs.next()) {
                result.add(rowToDiscipline(rs));
            }

            logger.info("Retrieved "+result.size()+" disciplines from database.");

            return result;
        } catch (SQLException ex) {
            String msg = "Error when getting all disciplines from DB";
            logger.error(msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
	}

    @Override
	public List<Discipline> getDisciplinesByDate(Date day) {

        logger.debug("Getting discipline by date.");

        checkDataSource();

        if (day == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT "+
                        COL_ID+
                        ", "+COL_NAME+
                        ", "+COL_START+
                        ", "+COL_END+
                        ", "+COL_MAX_PARTICIPANTS+
                    " FROM "+TABLE+
                    " WHERE "+
                        COL_START+" >= ? AND "+
                        COL_START+" <= ?"
            );
            st.setDate(1, day, gmtTime);
            Calendar c = Calendar.getInstance();
            c.setTime(day);
            c.add(Calendar.DATE, 1);
            st.setDate(2, new Date(c.getTime().getTime()), gmtTime);

            ResultSet rs = st.executeQuery();
            List<Discipline> result = new ArrayList<Discipline>();
            while (rs.next()) {
                result.add(rowToDiscipline(rs));
            }
            logger.debug("Retrieved "+result.size()+" disciplines from database by date: "+day);
            return result;
        } catch (SQLException ex) {
            String msg = "Error when getting disciplines from DB";
            logger.error(msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
	}

	@Override
	public void updateDiscipline(Discipline discipline) {

        logger.debug("Updating discipline: "+discipline);

        checkDataSource();

        validate(discipline);

        if (discipline.getId() == null) {
            throw new IllegalArgumentException("Discipline does not exist (ID is null)");
        }

        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = dataSource.getConnection();

            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "UPDATE "+TABLE+" SET "+
                        COL_NAME+" = ?, "+
                        COL_START+" = ?, "+
                        COL_END+" = ?, "+
                        COL_MAX_PARTICIPANTS+" = ? " +
                    "WHERE "+COL_ID+" = ?"
            );
            st.setString(1, discipline.getName());
            st.setTimestamp(2, discipline.getStart(), gmtTime);
            st.setTimestamp(3, discipline.getEnd(), gmtTime);
            st.setInt(4, discipline.getMaxParticipants());
            st.setLong(5, discipline.getId());

            int count = st.executeUpdate();

            if (count == 0) {
                throw new IllegalArgumentException("Updating non existent discipline, id:"+discipline.getId());
            }
            if (count != 1) {
                throw new ServiceFailureException("Integrity error. Updated row count: "+count);
            }

            conn.commit();

            logger.info("Discipline updated: "+discipline);

        } catch (SQLException ex) {
            String message = "Error updating discipline in the db";
            logger.error(message, ex);
            throw new ServiceFailureException(message, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
	}

	@Override
	public void deleteDiscipline(Discipline discipline) {

        logger.debug("Deleting discipline: "+discipline);

        checkDataSource();

        if (discipline == null) {
            throw new IllegalArgumentException("Discipline is null.");
        }
        if (discipline.getId() == null) {
            throw new IllegalArgumentException("Discipline is not created. (ID is null)");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();

            conn.setAutoCommit(false);
            st = conn.prepareStatement("DELETE FROM "+TABLE+" WHERE "+COL_ID+" = ?");
            st.setLong(1, discipline.getId());

            int count = st.executeUpdate();

            if (count == 0) {
                throw new IllegalArgumentException("No such Discipline in database");
            }
            if (count != 1) {
                throw new ServiceFailureException("Internal integrity error. Number of affected rows:"+count);
            }
            conn.commit();
            discipline.setId(null);

            logger.info("Discipline deleted: "+discipline);

        } catch (SQLException ex) {
            String message = "Error deleting discipline from the db";
            logger.error(message, ex);
            throw new ServiceFailureException(message, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
	}

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("No data source set.");
        }
    }

    private Discipline rowToDiscipline(ResultSet r) throws SQLException {
        Discipline result = new Discipline();
        result.setId(r.getLong(COL_ID));
        result.setName(r.getString(COL_NAME));
        result.setStart(r.getTimestamp(COL_START, gmtTime));
        result.setEnd(r.getTimestamp(COL_END, gmtTime));
        result.setMaxParticipants(r.getInt(COL_MAX_PARTICIPANTS));
        return result;
    }

    private void validate(Discipline discipline) {
        if (discipline == null) {
            throw new IllegalArgumentException("Discipline is null.");
        }
        if (discipline.getName() == null) {
            throw new IllegalArgumentException("Discipline with no name.");
        }
        if (discipline.getStart() == null) {
            throw new IllegalArgumentException("Discipline with no start.");
        }
        if (discipline.getEnd() == null) {
            throw new IllegalArgumentException("Discipline with no end.");
        }
        if (discipline.getMaxParticipants() < 0) {
            throw new IllegalArgumentException("Discipline with negative number of participants.");
        }
        if (discipline.getStart().after(discipline.getEnd())) {
            throw new IllegalArgumentException("Discipline with negative duration. (start > end)");
        }
    }

}