package fi.muni.pv168.ui.resources;

import fi.muni.pv168.*;
import fi.muni.pv168.utils.DBUtils;
import fi.muni.pv168.utils.ServiceFailureException;
import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class ManagerFactory {

    final static Logger logger = LoggerFactory.getLogger(ManagerFactory.class);

    public static final String DATABASE_PROPERTIES = "database.properties";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String CONNECTION_STRING = "connection_string";
    public static final String CREATE_TABLES = "create_tables";
    private static final String DEFAULT_CONNECTION_STRING = "jdbc:derby:memory:tourney;create=true";


    private static BasicDataSource dataSource;

    public static KnightManager initKnightManager() throws ServiceFailureException {
        KnightManagerImpl knightManagerImpl = new KnightManagerImpl();
        knightManagerImpl.setDataSource(getDatabase());
        return knightManagerImpl;
    }

    public static DisciplineManager initDisciplineManager() {
        DisciplineManagerImpl disciplineManagerImpl = new DisciplineManagerImpl();
        disciplineManagerImpl.setDataSource(getDatabase());
        return disciplineManagerImpl;
    }

    public static MatchManager initMatchManager() {
        MatchManagerImpl matchManagerImpl = new MatchManagerImpl();
        matchManagerImpl.setDataSource(getDatabase());
        return matchManagerImpl;
    }

    private static DataSource getDatabase() throws ServiceFailureException {

        if (dataSource != null) {
            return dataSource;
        }

        dataSource = new BasicDataSource();
        Properties settings = new Properties();
        try {
            settings.load(ManagerFactory.class.getResourceAsStream(DATABASE_PROPERTIES));
        } catch (IOException e) {
            String description = "Unable to open database properties: "+e.toString();
            logger.error(description, e);
            throw new RuntimeException(description, e);
        }
        dataSource.setUsername(settings.getProperty(USERNAME, null));
        dataSource.setPassword(settings.getProperty(PASSWORD, null));
        dataSource.setUrl(settings.getProperty(CONNECTION_STRING, DEFAULT_CONNECTION_STRING));
        if (settings.getProperty(CREATE_TABLES).equals(String.valueOf(true))) {
            try {
                DBUtils.executeSqlScript(dataSource, DisciplineManager.class.getResource("createTables.sql"));
            } catch (SQLException e) {
                String description = "Unable to create raw tables: "+e.toString();
                logger.error(description, e);
                throw new ServiceFailureException(description, e);
            }
        }

        return dataSource;
    }

}
