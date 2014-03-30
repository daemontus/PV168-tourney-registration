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
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for Discipline manager implementation class
 *
 * @author Samuel Pastva
 * @version 26/03/2014
 */
public class DisciplineManagerImplTest {


    private DisciplineManagerImpl manager;
    private BasicDataSource dataSource;

    //test instances
    private Discipline testDisciplineOne;
    private Discipline testDisciplineTwo;

    @Rule
    public ExpectedException exception = ExpectedException.none();




    @Before
    public void setUp() throws SQLException {
        dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:derby:memory:discipline-manager-test;create=true");
        DBUtils.executeSqlScript(dataSource, DisciplineManager.class.getResource("createTables.sql"));
        manager = new DisciplineManagerImpl();
        manager.setDataSource(dataSource);

        testDisciplineOne = new Discipline(null, "TestDiscipline", new Timestamp(0), new Timestamp(7200*1000), 120);
        testDisciplineTwo = new Discipline(null, "TestDiscipline2", new Timestamp(1000*60*30), new Timestamp(1000*60*180), 3);

    }

    @After
    public void cleanUp() throws SQLException {
        DBUtils.executeSqlScript(dataSource, KnightManager.class.getResource("dropTables.sql"));
    }




    @Test
    public void createDiscipline() {
        manager.createDiscipline(testDisciplineOne);
        Long id = testDisciplineOne.getId();

        assertNotNull(id);

        Discipline result = manager.getDisciplineById(testDisciplineOne.getId());
        assertEquals(testDisciplineOne, result);
        assertNotSame(testDisciplineOne, result);
        assertDeepEquals(testDisciplineOne, result);
    }

    @Test
    public void createNullDiscipline() {
        exception.expect(IllegalArgumentException.class);
        manager.createDiscipline(null);
    }

    @Test
    public void createNullNameDiscipline() {
        exception.expect(IllegalArgumentException.class);
        manager.createDiscipline(new Discipline(null, null, new Timestamp(8000), new Timestamp(12000), 500));
    }

    @Test
    public void createNullStartDiscipline() {
        exception.expect(IllegalArgumentException.class);
        manager.createDiscipline(new Discipline(null, "TestName", null, new Timestamp(5000), 5));
    }

    @Test
    public void createNullEndDiscipline() {
        exception.expect(IllegalArgumentException.class);
        manager.createDiscipline(new Discipline(null, "TestName", new Timestamp(1000), null, 12));
    }

    @Test
    public void createNegativeParticipantsDiscipline() {
        exception.expect(IllegalArgumentException.class);
        manager.createDiscipline(new Discipline(null, "TestName", new Timestamp(1000), new Timestamp(5000), -1));
    }

    @Test
    public void createNegativeDurationDiscipline() {
        exception.expect(IllegalArgumentException.class);
        manager.createDiscipline(new Discipline(null, "TestName", new Timestamp(50000), new Timestamp(2000), 123));
    }

    @Test
    public void createDisciplineInvalidDataSource() {
        manager = new DisciplineManagerImpl();
        exception.expect(IllegalStateException.class);
        manager.createDiscipline(testDisciplineOne);
    }




    @Test
    public void getAllDisciplines() {
        manager.createDiscipline(testDisciplineOne);
        manager.createDiscipline(testDisciplineTwo);
        List<Discipline> expected = Arrays.asList(testDisciplineOne, testDisciplineTwo);
        List<Discipline> actual = manager.findAllDisciplines();
        assertDeepEquals(expected, actual);
    }

    @Test
    public void getAllDisciplinesInvalidDataSource() {
        manager = new DisciplineManagerImpl();
        exception.expect(IllegalStateException.class);
        manager.findAllDisciplines();
    }




    @Test
    public void updateDisciplineName() {
        manager.createDiscipline(testDisciplineOne);
        manager.createDiscipline(testDisciplineTwo);

        testDisciplineOne.setName(testDisciplineTwo.getName());   //edit name
        manager.updateDiscipline(testDisciplineOne);

        assertDeepEquals(testDisciplineOne, manager.getDisciplineById(testDisciplineOne.getId()));
        assertDeepEquals(testDisciplineTwo, manager.getDisciplineById(testDisciplineTwo.getId()));
    }

    @Test
    public void updateDisciplineStart() {
        manager.createDiscipline(testDisciplineOne);
        manager.createDiscipline(testDisciplineTwo);

        testDisciplineOne.setStart(testDisciplineTwo.getStart());   //edit castle
        manager.updateDiscipline(testDisciplineOne);

        assertDeepEquals(testDisciplineOne, manager.getDisciplineById(testDisciplineOne.getId()));
        assertDeepEquals(testDisciplineTwo, manager.getDisciplineById(testDisciplineTwo.getId()));
    }

    @Test
    public void updateDisciplineEnd() {
        manager.createDiscipline(testDisciplineOne);
        manager.createDiscipline(testDisciplineTwo);

        testDisciplineOne.setEnd(testDisciplineTwo.getEnd());   //edit born
        manager.updateDiscipline(testDisciplineOne);

        assertDeepEquals(testDisciplineOne, manager.getDisciplineById(testDisciplineOne.getId()));
        assertDeepEquals(testDisciplineTwo, manager.getDisciplineById(testDisciplineTwo.getId()));
    }

    @Test
    public void updateDisciplineMaxParticipants() {
        manager.createDiscipline(testDisciplineOne);
        manager.createDiscipline(testDisciplineTwo);

        testDisciplineOne.setMaxParticipants(testDisciplineTwo.getMaxParticipants()); //set not null points
        manager.updateDiscipline(testDisciplineOne);

        assertDeepEquals(testDisciplineOne, manager.getDisciplineById(testDisciplineOne.getId()));
        assertDeepEquals(testDisciplineTwo, manager.getDisciplineById(testDisciplineTwo.getId()));
    }

    @Test
    public void updateNullDiscipline() {
        exception.expect(IllegalArgumentException.class);
        manager.updateDiscipline(null);
    }

    @Test
    public void updateNullIdDiscipline() {
        exception.expect(IllegalArgumentException.class);
        manager.updateDiscipline(testDisciplineOne);
    }

    @Test
    public void updateNonExistentDiscipline() {
        testDisciplineOne.setId(3L);
        exception.expect(IllegalArgumentException.class);
        manager.updateDiscipline(testDisciplineOne);
    }

    @Test
    public void updateNullNameDiscipline() {
        manager.createDiscipline(testDisciplineOne);
        testDisciplineOne.setName(null);

        exception.expect(IllegalArgumentException.class);
        manager.updateDiscipline(testDisciplineOne);
    }

    @Test
    public void updateNullStartDiscipline() {
        manager.createDiscipline(testDisciplineOne);
        testDisciplineOne.setStart(null);

        exception.expect(IllegalArgumentException.class);
        manager.updateDiscipline(testDisciplineOne);
    }

    @Test
    public void updateNullEndDiscipline() {
        manager.createDiscipline(testDisciplineOne);
        testDisciplineOne.setEnd(null);

        exception.expect(IllegalArgumentException.class);
        manager.updateDiscipline(testDisciplineOne);
    }

    @Test
    public void updateNegativeParticipantsDiscipline() {
        manager.createDiscipline(testDisciplineOne);
        testDisciplineOne.setMaxParticipants(-1);

        exception.expect(IllegalArgumentException.class);
        manager.updateDiscipline(testDisciplineOne);
    }

    @Test
    public void updateNegativeDurationDiscipline() {
        manager.createDiscipline(testDisciplineOne);
        testDisciplineOne.setStart(new Timestamp(123000));
        testDisciplineOne.setEnd(new Timestamp(50233));

        exception.expect(IllegalArgumentException.class);
        manager.updateDiscipline(testDisciplineOne);
    }

    @Test
    public void updateDisciplineInvalidDataSource() {
        manager = new DisciplineManagerImpl();
        exception.expect(IllegalStateException.class);
        manager.updateDiscipline(testDisciplineOne);
    }




    @Test
    public void deleteDiscipline() {
        manager.createDiscipline(testDisciplineOne);
        manager.createDiscipline(testDisciplineTwo);
        Long id = testDisciplineOne.getId();

        assertNotNull(manager.getDisciplineById(testDisciplineOne.getId()));
        assertNotNull(manager.getDisciplineById(testDisciplineTwo.getId()));

        manager.deleteDiscipline(testDisciplineOne);

        assertNull(manager.getDisciplineById(id));
        assertNull(testDisciplineOne.getId());
        assertNotNull(manager.getDisciplineById(testDisciplineTwo.getId()));
    }

    @Test
    public void deleteNullDiscipline() {
        exception.expect(IllegalArgumentException.class);
        manager.deleteDiscipline(null);
    }

    @Test
    public void deleteNullIdDiscipline() {
        exception.expect(IllegalArgumentException.class);
        manager.deleteDiscipline(testDisciplineOne);
    }

    @Test
    public void deleteNonExistentDiscipline() {
        testDisciplineOne.setId(3L);
        exception.expect(IllegalArgumentException.class);
        manager.deleteDiscipline(testDisciplineOne);
    }

    @Test
    public void deleteDisciplineInvalidDataSource() {
        manager = new DisciplineManagerImpl();
        exception.expect(IllegalStateException.class);
        manager.deleteDiscipline(testDisciplineOne);
    }




    @Test
    public void getDisciplinesByDate() {
        manager.createDiscipline(testDisciplineOne);
        manager.createDiscipline(testDisciplineTwo);

        List<Discipline> expected = Arrays.asList(testDisciplineOne, testDisciplineTwo);
        List<Discipline> actual = manager.getDisciplinesByDate(new Date(testDisciplineOne.getEnd().getTime()));
        assertDeepEquals(expected, actual);

        testDisciplineTwo.setStart(new Timestamp(1000*60*60*24*5));
        testDisciplineTwo.setEnd(new Timestamp(1000*60*60*24*5 + 1000*60*60));
        manager.updateDiscipline(testDisciplineTwo);

        expected = Arrays.asList(testDisciplineOne);
        actual = manager.getDisciplinesByDate(new Date(testDisciplineOne.getEnd().getTime()));
        assertDeepEquals(expected, actual);

        expected = Arrays.asList(testDisciplineTwo);
        actual = manager.getDisciplinesByDate(new Date(testDisciplineTwo.getEnd().getTime()));
        assertDeepEquals(expected, actual);

        actual = manager.getDisciplinesByDate(new Date(1000*60*60*24*10));
        assertEquals(actual.size(), 0);
    }

    @Test
    public void getDisciplinesByNullDate() {
        exception.expect(IllegalArgumentException.class);
        manager.getDisciplinesByDate(null);
    }

    @Test
    public void getDisciplinesByDateInvalidDataSource() {
        manager = new DisciplineManagerImpl();
        exception.expect(IllegalStateException.class);
        manager.getDisciplinesByDate(new Date(0));
    }




    private static void assertDeepEquals(List<Discipline> expected, List<Discipline> actual) {
        assertEquals(expected.size(), actual.size());
        Collections.sort(expected, idComparator);
        Collections.sort(actual, idComparator);
        for (int i=0; i<actual.size(); i++) {
            assertDeepEquals(expected.get(i), actual.get(i));
        }
    }

    private static void assertDeepEquals(Discipline expected, Discipline actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getStart(), actual.getStart());
        assertEquals(expected.getEnd(), actual.getEnd());
        assertEquals(expected.getMaxParticipants(), actual.getMaxParticipants());
    }

    private static Comparator<Discipline> idComparator = new Comparator<Discipline>() {
        @Override
        public int compare(Discipline discipline, Discipline discipline2) {
            if (discipline.getId() == null || discipline2.getId() == null) {
                throw new IllegalArgumentException("Can't compare null ids");
            }
            return discipline.getId().compareTo(discipline2.getId());
        }
    };


}
