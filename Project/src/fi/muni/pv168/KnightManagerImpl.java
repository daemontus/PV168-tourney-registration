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
 * <p>Implementation of KnightManager using JDBC.</p>
 */
public class KnightManagerImpl implements KnightManager {

    private final static String COL_ID = "ID";
    private final static String COL_NAME = "NAME";
    private final static String COL_BORN = "BORN";
    private final static String COL_CASTLE = "CASTLE";
    private final static String COL_HERALDRY = "HERALDRY";
    private final static String TABLE = "KNIGHTS";

    private final static Calendar gmtTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

    private final static Logger logger = LoggerFactory.getLogger(KnightManagerImpl.class);

    private DataSource dataSource;

    /**
     * Set data source of this manager
     * @param dataSource DataSource, can be null.
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("No data source set.");
        }
    }

    @Override
	public void createKnight(Knight knight) {

        logger.debug("Creating Knight: "+knight);

        checkDataSource();

        validate(knight);

        if (knight.getId() != null) {
            throw new IllegalArgumentException("Knight already exists (ID is not null)");
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
                            ","+COL_BORN+
                            ","+COL_CASTLE+
                            ","+COL_HERALDRY+
                        ") " +
                    "VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, knight.getName());
            st.setDate(2, knight.getBorn(), gmtTime);
            st.setString(3, knight.getCastle());
            st.setString(4, knight.getHeraldry());

            int count = st.executeUpdate();

            if (count != 1) {
                throw new ServiceFailureException("Unexpected number of modified rows during Knight create: "+count);
            }

            Long id = DBUtils.getId(st.getGeneratedKeys());
            knight.setId(id);

            conn.commit();

            logger.info("Knight successfully created: "+knight);

        } catch (SQLException ex) {
            throw logException("Error inserting knight into db", ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
	}

	@Override
	public Knight getKnightById(Long id) {

        logger.debug("Getting knight by id: "+id);

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
                        ", "+COL_BORN+
                        ", "+COL_CASTLE+
                        ", "+COL_HERALDRY+
                    " FROM "+TABLE+
                    " WHERE "+COL_ID+" = ?"
            );
            st.setLong(1, id);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Knight result = rowToKnight(rs);
                if (rs.next()) {
                    throw new ServiceFailureException("Internal integrity error: more knights with the same id!");
                }
                logger.info("Retrieved knight by id "+id+": "+result);
                return result;
            } else {
                logger.info("No knight with id "+id+" in database.");
                return null;
            }

        } catch (SQLException ex) {
            throw logException("Error getting knight with id = " + id + " from DB", ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
	}

    @Override
	public List<Knight> findAllKnights() throws ServiceFailureException {

        logger.debug("Getting all knights from database.");

        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT "+
                        COL_ID+
                        ", "+COL_NAME+
                        ", "+COL_BORN+
                        ", "+COL_CASTLE+
                        ", "+COL_HERALDRY+
                    " FROM "+TABLE
            );

            ResultSet rs = st.executeQuery();
            List<Knight> result = new ArrayList<Knight>();
            while (rs.next()) {
                result.add(rowToKnight(rs));
            }
            logger.info("Retrieved "+result.size()+" knights from database.");
            return result;
        } catch (SQLException ex) {
            throw logException("Error when getting all knights from DB", ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
	}

	@Override
	public void updateKnight(Knight knight) throws ServiceFailureException {

        logger.debug("Updating knight: "+knight);

        checkDataSource();

        validate(knight);

        if (knight.getId() == null) {
            throw new IllegalArgumentException("Knight does not exist (ID is null)");
        }

        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = dataSource.getConnection();

            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "UPDATE "+TABLE+" SET "+
                        COL_NAME+" = ?, "+
                        COL_BORN+" = ?, "+
                        COL_CASTLE+" = ?, "+
                        COL_HERALDRY+" = ? " +
                    "WHERE "+COL_ID+" = ?"
            );
            st.setString(1, knight.getName());
            st.setDate(2, knight.getBorn(), gmtTime);
            st.setString(3, knight.getCastle());
            st.setString(4, knight.getHeraldry());
            st.setLong(5, knight.getId());

            int count = st.executeUpdate();

            if (count == 0) {
                throw new IllegalArgumentException("Updating non existent knight, id:"+knight.getId());
            }
            if (count != 1) {
                throw new ServiceFailureException("Integrity error. Updated row count: "+count);
            }

            logger.info("Knight updated: "+knight);
            conn.commit();
        } catch (SQLException ex) {
            throw logException("Error updating knight in the db", ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
	}

	@Override
	public void deleteKnight(Knight knight) throws ServiceFailureException {

        logger.debug("Deleting knight: "+knight);

        checkDataSource();

        if (knight == null) {
            throw new IllegalArgumentException("Knight is null.");
        }
        if (knight.getId() == null) {
            throw new IllegalArgumentException("Knight is not created. (ID is null)");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();

            conn.setAutoCommit(false);
            st = conn.prepareStatement("DELETE FROM "+TABLE+" WHERE "+COL_ID+" = ?");
            st.setLong(1, knight.getId());

            int count = st.executeUpdate();

            if (count == 0) {
                throw new IllegalArgumentException("No such Knight in database");
            }
            if (count != 1) {
                throw new ServiceFailureException("Internal integrity error. Number of affected rows:"+count);
            }
            conn.commit();
            knight.setId(null);

            logger.info("Knight deleted: "+knight);

        } catch (SQLException ex) {
            throw logException("Error deleting knight from the db", ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
	}

    private Knight rowToKnight(ResultSet r) throws SQLException {
        Knight result = new Knight();
        result.setId(r.getLong(COL_ID));
        result.setCastle(r.getString(COL_CASTLE));
        result.setName(r.getString(COL_NAME));
        result.setHeraldry(r.getString(COL_HERALDRY));
        result.setBorn(r.getDate(COL_BORN, gmtTime));
        return result;
    }

    private void validate(Knight knight) {
        if (knight == null) {
            throw new IllegalArgumentException("Knight is null.");
        }
        if (knight.getName() == null) {
            throw new IllegalArgumentException("Knight with no name.");
        }
        if (knight.getCastle() == null) {
            throw new IllegalArgumentException("Knight with no castle.");
        }
        if (knight.getHeraldry() == null) {
            throw new IllegalArgumentException("Knight with no heraldry.");
        }
        if (knight.getBorn() == null) {
            throw new IllegalArgumentException("Knight with no birth date.");
        }
    }

    private ServiceFailureException logException(String message, Exception ex) throws ServiceFailureException {
        logger.error(message, ex);
        return new ServiceFailureException(message, ex);
    }
}