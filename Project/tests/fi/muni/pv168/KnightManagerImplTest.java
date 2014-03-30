package fi.muni.pv168;

import fi.muni.pv168.utils.DBUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Implementation unit tests of Knight Manager
 *
 * @author David Kizivat
 * @version 0.1
 */
public class KnightManagerImplTest {

    private KnightManagerImpl manager;
    private BasicDataSource dataSource;

    //test instances
    private Knight testKnightOne;
    private Knight testKnightTwo;

    private static final int MILLIS_IN_DAY = 1000*60*60*24;

    @Rule
    public ExpectedException exception = ExpectedException.none();




    @Before
    public void setUp() throws SQLException {
        dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:derby:memory:knight-manager-test;create=true");
        DBUtils.executeSqlScript(dataSource, KnightManager.class.getResource("createTables.sql"));
        manager = new KnightManagerImpl();
        manager.setDataSource(dataSource);

        testKnightOne = new Knight(null, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        testKnightTwo = new Knight(null, "TestKnight2", "TestCastle2", new Date(MILLIS_IN_DAY), "TestHeraldry2");

    }

    @After
    public void cleanUp() throws SQLException {
        DBUtils.executeSqlScript(dataSource, KnightManager.class.getResource("dropTables.sql"));
    }




    @Test
    public void createKnight() {
        manager.createKnight(testKnightOne);
        Long id = testKnightOne.getId();

        assertNotNull(id);

        Knight result = manager.getKnightById(testKnightOne.getId());
        assertEquals(testKnightOne, result);
        assertNotSame(testKnightOne, result);
        assertDeepEquals(testKnightOne, result);
    }

    @Test
    public void createNullKnight() {
        exception.expect(IllegalArgumentException.class);
        manager.createKnight(null);
    }

    @Test
    public void createNullNameKnight() {
        exception.expect(IllegalArgumentException.class);
        manager.createKnight(new Knight(null, null, "TestCastle", new Date(0), "Test"));
    }

    @Test
    public void createNullCastleKnight() {
        exception.expect(IllegalArgumentException.class);
        manager.createKnight(new Knight(null, "TestName", null, new Date(0), "Test"));
    }

    @Test
    public void createNullBornKnight() {
        exception.expect(IllegalArgumentException.class);
        manager.createKnight(new Knight(null, "TestName", "TestCastle", null, "Test"));
    }

    @Test
    public void createNullHeraldryKnight() {
        exception.expect(IllegalArgumentException.class);
        manager.createKnight(new Knight(null, "TestName", "TestCastle", new Date(0), null));
    }

    @Test
    public void createKnightInvalidDataSource() {
        manager = new KnightManagerImpl();
        exception.expect(IllegalStateException.class);
        manager.createKnight(testKnightOne);
    }




    @Test
    public void getAllKnights() {
        manager.createKnight(testKnightOne);
        manager.createKnight(testKnightTwo);
        List<Knight> expected = Arrays.asList(testKnightOne, testKnightTwo);
        List<Knight> actual = manager.findAllKnights();
        assertDeepEquals(expected, actual);
    }

    @Test
    public void getAllKnightsInvalidDataSource() {
        manager = new KnightManagerImpl();
        exception.expect(IllegalStateException.class);
        manager.findAllKnights();
    }



    @Test
    public void updateKnightName() {
        manager.createKnight(testKnightOne);
        manager.createKnight(testKnightTwo);
        Long id = testKnightOne.getId();

        testKnightOne.setName(testKnightTwo.getName());   //edit name
        manager.updateKnight(testKnightOne);

        assertDeepEquals(testKnightOne, manager.getKnightById(id));
        assertDeepEquals(testKnightTwo, manager.getKnightById(testKnightTwo.getId()));
    }

    @Test
    public void updateKnightCastle() {
        manager.createKnight(testKnightOne);
        manager.createKnight(testKnightTwo);
        Long id = testKnightOne.getId();

        testKnightOne.setCastle(testKnightTwo.getCastle());   //edit castle
        manager.updateKnight(testKnightOne);

        assertDeepEquals(testKnightOne, manager.getKnightById(id));
        assertDeepEquals(testKnightTwo, manager.getKnightById(testKnightTwo.getId()));
    }

    @Test
    public void updateKnightBorn() {
        manager.createKnight(testKnightOne);
        manager.createKnight(testKnightTwo);
        Long id = testKnightOne.getId();

        testKnightOne.setBorn(testKnightTwo.getBorn());   //edit born
        manager.updateKnight(testKnightOne);

        assertDeepEquals(testKnightOne, manager.getKnightById(id));
        assertDeepEquals(testKnightTwo, manager.getKnightById(testKnightTwo.getId()));
    }

    @Test
    public void updateKnightHeraldry() {
        manager.createKnight(testKnightOne);
        manager.createKnight(testKnightTwo);
        Long id = testKnightOne.getId();

        testKnightOne.setHeraldry(testKnightTwo.getHeraldry()); //set not null points
        manager.updateKnight(testKnightOne);

        assertDeepEquals(testKnightOne, manager.getKnightById(id));
        assertDeepEquals(testKnightTwo, manager.getKnightById(testKnightTwo.getId()));
    }

    @Test
    public void updateNullKnight() {
        exception.expect(IllegalArgumentException.class);
        manager.updateKnight(null);
    }

    @Test
    public void updateNullIdKnight() {
        exception.expect(IllegalArgumentException.class);
        manager.updateKnight(testKnightOne);
    }

    @Test
    public void updateNonExistentKnight() {
        testKnightOne.setId(3L);
        exception.expect(IllegalArgumentException.class);
        manager.updateKnight(testKnightOne);
    }

    @Test
    public void updateNullNameKnight() {
        manager.createKnight(testKnightOne);
        testKnightOne.setName(null);

        exception.expect(IllegalArgumentException.class);
        manager.updateKnight(testKnightOne);
    }

    @Test
    public void updateNullCastleKnight() {
        manager.createKnight(testKnightOne);
        testKnightOne.setCastle(null);

        exception.expect(IllegalArgumentException.class);
        manager.updateKnight(testKnightOne);
    }

    @Test
    public void updateNullBornKnight() {
        manager.createKnight(testKnightOne);
        testKnightOne.setBorn(null);

        exception.expect(IllegalArgumentException.class);
        manager.updateKnight(testKnightOne);
    }

    @Test
    public void updateNullHeraldryKnight() {
        manager.createKnight(testKnightOne);
        testKnightOne.setHeraldry(null);

        exception.expect(IllegalArgumentException.class);
        manager.updateKnight(testKnightOne);
    }

    @Test
    public void updateKnightInvalidDataSource() {
        manager = new KnightManagerImpl();
        exception.expect(IllegalStateException.class);
        manager.updateKnight(testKnightOne);
    }




    @Test
    public void deleteKnight() {
        manager.createKnight(testKnightOne);
        manager.createKnight(testKnightTwo);
        Long id = testKnightOne.getId();

        assertNotNull(manager.getKnightById(testKnightOne.getId()));
        assertNotNull(manager.getKnightById(testKnightTwo.getId()));

        manager.deleteKnight(testKnightOne);

        assertNull(manager.getKnightById(id));
        assertNull(testKnightOne.getId());
        assertNotNull(manager.getKnightById(testKnightTwo.getId()));
    }

    @Test
    public void deleteNullKnight() {
        exception.expect(IllegalArgumentException.class);
        manager.deleteKnight(null);
    }

    @Test
    public void deleteNullIdKnight() {
        exception.expect(IllegalArgumentException.class);
        manager.deleteKnight(testKnightOne);
    }

    @Test
    public void deleteNonExistentKnight() {
        testKnightOne.setId(3L);
        exception.expect(IllegalArgumentException.class);
        manager.deleteKnight(testKnightOne);
    }

    @Test
    public void deleteKnightInvalidDataSource() {
        manager = new KnightManagerImpl();
        exception.expect(IllegalStateException.class);
        manager.deleteKnight(testKnightOne);
    }




    private static void assertDeepEquals(List<Knight> expected, List<Knight> actual) {
        assertEquals(expected.size(), actual.size());
        Collections.sort(expected, idComparator);
        Collections.sort(actual, idComparator);
        for (int i=0; i<actual.size(); i++) {
            assertDeepEquals(expected.get(i), actual.get(i));
        }
    }

    private static void assertDeepEquals(Knight expected, Knight actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getCastle(), actual.getCastle());
        assertEquals(expected.getBorn(), actual.getBorn());
        assertEquals(expected.getHeraldry(), actual.getHeraldry());
    }

    private static Comparator<Knight> idComparator = new Comparator<Knight>() {
        @Override
        public int compare(Knight knight, Knight knight2) {
            if (knight.getId() == null || knight2.getId() == null) {
                throw new IllegalArgumentException("Can't compare null ids");
            }
            return knight.getId().compareTo(knight2.getId());
        }
    };


}
