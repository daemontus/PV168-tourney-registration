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
 * Describe the class here.
 *
 * @author David Kizivat
 * @version 0.1
 */
public class KnightManagerImplTest {

    private KnightManagerImpl manager;
    private BasicDataSource dataSource;

    private static final int MILIS_IN_DAY = 1000*60*60*24;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws SQLException {
        dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:derby:memory:knight-manager-test;create=true");
        DBUtils.executeSqlScript(dataSource, KnightManager.class.getResource("createTables.sql"));
        manager = new KnightManagerImpl();
        manager.setDataSource(dataSource);
    }

    @After
    public void cleanUp() throws SQLException {
        DBUtils.executeSqlScript(dataSource, KnightManager.class.getResource("dropTables.sql"));
    }

    @Test
    public void createKnight() {
        Knight knight = new Knight(null, "TestKnightName", "TestCastle", new Date(0), "TestHeraldry"); //not-null name
        manager.createKnight(knight);
        Long id = knight.getId();

        assertNotNull(id);

        Knight result = manager.getKnightById(knight.getId());
        assertEquals(knight, result);
        assertNotSame(knight, result);
        assertDeepEquals(knight, result);
    }

    @Test
    public void createKnightNoName() {

    }

    @Test
    public void getAllKnights() {

        Knight knight1 = new Knight(null, "TestName", "TestCastle", new Date(0), "TestHeraldry");
        manager.createKnight(knight1);
        Knight knight2 = new Knight(null, "TestName2", "TestCastle2", new Date(MILIS_IN_DAY), "TestHeraldry2");
        manager.createKnight(knight2);

        List<Knight> expected = Arrays.asList(knight1, knight2);
        List<Knight> actual = manager.findAllKnights();

        assertDeepEquals(expected, actual);
    }

    @Test
    public void updateKnightName() {

        Knight knight = new Knight(null, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Knight knight2 = new Knight(null, "TestKnight2", "TestCastle2", new Date(MILIS_IN_DAY), "TestHeraldry2");
        manager.createKnight(knight);
        manager.createKnight(knight2);
        Long id = knight.getId();

        knight.setName(knight2.getName());   //edit name
        manager.updateKnight(knight);
        assertDeepEquals(knight, manager.getKnightById(id));


        //are other records affected?
        assertDeepEquals(knight2, manager.getKnightById(knight2.getId()));
    }

    @Test
    public void updateKnightCastle() {

        Knight knight = new Knight(null, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Knight knight2 = new Knight(null, "TestKnight2", "TestCastle2", new Date(MILIS_IN_DAY), "TestHeraldry2");
        manager.createKnight(knight);
        manager.createKnight(knight2);
        Long id = knight.getId();

        knight.setCastle(knight2.getCastle());   //edit castle
        manager.updateKnight(knight);
        assertDeepEquals(knight, manager.getKnightById(id));

        //are other records affected?
        assertDeepEquals(knight2, manager.getKnightById(knight2.getId()));
    }

    @Test
    public void updateKnightBorn() {

        Knight knight = new Knight(null, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Knight knight2 = new Knight(null, "TestKnight2", "TestCastle2", new Date(MILIS_IN_DAY), "TestHeraldry2");
        manager.createKnight(knight);
        manager.createKnight(knight2);
        Long id = knight.getId();

        knight.setBorn(knight2.getBorn());   //edit born
        manager.updateKnight(knight);
        assertDeepEquals(knight, manager.getKnightById(id));

        //are other records affected?
        assertDeepEquals(knight2, manager.getKnightById(knight2.getId()));
    }

    @Test
    public void updateKnightHeraldry() {

        Knight knight = new Knight(null, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Knight knight2 = new Knight(null, "TestKnight2", "TestCastle2", new Date(MILIS_IN_DAY), "TestHeraldry2");
        manager.createKnight(knight);
        manager.createKnight(knight2);
        Long id = knight.getId();

        knight.setHeraldry(knight2.getHeraldry()); //set not null points
        manager.updateKnight(knight);
        assertDeepEquals(knight, manager.getKnightById(id));

        //are other records affected?
        assertDeepEquals(knight2, manager.getKnightById(knight2.getId()));
    }



    @Test
    public void deleteKnight() {


        Knight knight = new Knight(null, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        manager.createKnight(knight);
        Knight knight2 = new Knight(null, "TestKnight2", "TestCastle2", new Date(1), "TestHeraldry2");
        manager.createKnight(knight2);

        assertNotNull(manager.getKnightById(knight.getId()));
        assertNotNull(manager.getKnightById(knight2.getId()));

        manager.deleteKnight(knight);

        assertNull(manager.getKnightById(knight.getId()));
        assertNotNull(manager.getKnightById(knight2.getId()));

    }

    private static void assertDeepEquals(List<Knight> expected, List<Knight> actual) {
        assertEquals(expected.size(), actual.size());
        Collections.sort(expected, idComparator);
        Collections.sort(actual, idComparator);
        for (int i=0; i<actual.size(); i++) assertDeepEquals(expected.get(i), actual.get(i));
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
            if (knight.getId() == null || knight2.getId() == null) throw new IllegalArgumentException("Cant compare null ids");
            return knight.getId().compareTo(knight2.getId());
        }
    };
}
