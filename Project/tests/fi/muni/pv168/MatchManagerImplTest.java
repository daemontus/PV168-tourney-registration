package fi.muni.pv168;

import fi.muni.pv168.utils.DBUtils;
import fi.muni.pv168.utils.ServiceFailureException;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.*;

/**
 *
 * @author Samuel Pastva
 * @version 09/03/2014
 */
public class MatchManagerImplTest {

    private static final int MILLIS_IN_DAY = 1000*60*60*24;

    private KnightManager mockKnightManager = new KnightManager() {
        private long idPool = 1;
        private Map<Long, Knight> data = new HashMap<Long, Knight>();

        @Override
        public void createKnight(Knight knight) throws ServiceFailureException {
            knight.setId(idPool);
            idPool++;
            data.put(knight.getId(), knight);
        }

        @Override
        public Knight getKnightById(Long id) throws ServiceFailureException {
            return data.get(id);
        }

        @Override
        public List<Knight> findAllKnights() throws ServiceFailureException {
            return new ArrayList<Knight>(data.values());
        }

        @Override
        public void updateKnight(Knight knight) throws ServiceFailureException {
            data.put(knight.getId(), knight);
        }

        @Override
        public void deleteKnight(Knight knight) throws ServiceFailureException {
            data.remove(knight.getId());
        }
    };

    private DisciplineManager mockDisciplineManager = new DisciplineManager() {
        private long idPool = 0;
        private Map<Long, Discipline> data = new HashMap<Long, Discipline>();

        @Override
        public void createDiscipline(Discipline discipline) {
            discipline.setId(idPool);
            idPool++;
            data.put(discipline.getId(), discipline);
        }

        @Override
        public Discipline getDisciplineById(Long id) {
            return data.get(id);
        }

        @Override
        public List<Discipline> findAllDisciplines() {
            return new ArrayList<Discipline>(data.values());
        }

        @Override
        public List<Discipline> getDisciplinesByDate(Date day) {
            throw new UnsupportedOperationException("This should not be used in this context.");
        }

        @Override
        public void updateDiscipline(Discipline discipline) {
            data.put(discipline.getId(), discipline);
        }

        @Override
        public void deleteDiscipline(Discipline discipline) {
            data.remove(discipline.getId());
        }
    };

    private MatchManagerImpl manager;
    private BasicDataSource dataSource;

    //test instances
    private Knight testKnightOne;
    private Knight testKnightTwo;
    private Discipline testDisciplineOne;
    private Discipline testDisciplineTwo;
    private Match testMatchOne;
    private Match testMatchTwo;




    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws SQLException {
        manager = new MatchManagerImpl();
        dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:derby:memory:match-manager-test;create=true");
        DBUtils.executeSqlScript(dataSource, MatchManager.class.getResource("createTables.sql"));
        manager = new MatchManagerImpl();
        manager.setDataSource(dataSource);
        manager.setKnightManager(mockKnightManager);
        manager.setDisciplineManager(mockDisciplineManager);

        testKnightOne = new Knight(null, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        testKnightTwo = new Knight(null, "TestKnight2", "TestCastle2", new Date(MILLIS_IN_DAY), "TestHeraldry2");

        testDisciplineOne = new Discipline(null, "TestDiscipline", new Timestamp(0), new Timestamp(7200*1000), 120);
        testDisciplineTwo = new Discipline(null, "TestDiscipline2", new Timestamp(1000*60*30), new Timestamp(1000*60*180), 3);

        mockKnightManager.createKnight(testKnightOne);
        mockKnightManager.createKnight(testKnightTwo);

        mockDisciplineManager.createDiscipline(testDisciplineOne);
        mockDisciplineManager.createDiscipline(testDisciplineTwo);

        testMatchOne = new Match(null, testKnightOne, testDisciplineOne, 5, 120);
        testMatchTwo = new Match(null, testKnightTwo, testDisciplineTwo, 4, null);
    }

    @After
    public void cleanUp() throws SQLException {
        DBUtils.executeSqlScript(dataSource, KnightManager.class.getResource("dropTables.sql"));
    }




    @Test
    public void createMatch() {
        manager.createMatch(testMatchOne);
        Long id = testMatchOne.getId();

        assertNotNull(id);
        Match result = manager.getMatchById(testMatchOne.getId());
        assertEquals(testMatchOne, result);
        assertNotSame(testMatchOne, result);
        assertDeepEquals(testMatchOne, result);
    }

    @Test
    public void createNullPointsMatch() {
        manager.createMatch(testMatchTwo);
        Long id = testMatchTwo.getId();

        assertNotNull(id);
        Match result = manager.getMatchById(testMatchTwo.getId());
        assertEquals(testMatchTwo, result);
        assertNotSame(testMatchTwo, result);
        assertDeepEquals(testMatchTwo, result);
    }

    @Test
    public void invalidPointsBelowZeroCreate() {
        exception.expect(IllegalArgumentException.class);
        manager.createMatch(new Match(null, testKnightOne, testDisciplineOne, 3, -1));
    }

    @Test
    public void invalidNumberBelowZeroCreate() {
        exception.expect(IllegalArgumentException.class);
        manager.createMatch(new Match(null, testKnightOne, testDisciplineOne, -1, 120));
    }

    @Test
    public void invalidNullDisciplineCreate() {
        exception.expect(IllegalArgumentException.class);
        manager.createMatch(new Match(null, testKnightOne, null, 5, 120));
    }

    @Test
    public void invalidNullKnightCreate() {
        exception.expect(IllegalArgumentException.class);
        manager.createMatch(new Match(null, null, testDisciplineOne, 5, 120));
    }

    @Test
    public void invalidIdCreate() {
        exception.expect(IllegalArgumentException.class);
        manager.createMatch(new Match(1L, testKnightOne, testDisciplineOne, 5, 120));
    }

    @Test
    public void invalidNullCreate() {
        exception.expect(IllegalArgumentException.class);
        manager.createMatch(null);
    }

    @Test
    public void invalidNonExistentKnightCreate() {
        mockKnightManager.deleteKnight(testKnightOne);
        exception.expect(IllegalArgumentException.class);
        manager.createMatch(testMatchOne);
    }

    @Test
    public void invalidNonExistentDisciplineCreate() {
        mockDisciplineManager.deleteDiscipline(testDisciplineOne);
        exception.expect(IllegalArgumentException.class);
        manager.createMatch(testMatchOne);
    }

    @Test
    public void createMatchInvalidDataSource() {
        exception.expect(IllegalStateException.class);
        manager.createMatch(testMatchOne);
    }

    @Test
    public void createMatchInvalidDisciplineManager() {
        manager.setDisciplineManager(null);
        exception.expect(IllegalStateException.class);
        manager.createMatch(testMatchOne);
    }

    @Test
    public void createMatchInvalidKnightManager() {
        manager.setKnightManager(null);
        exception.expect(IllegalStateException.class);
        manager.createMatch(testMatchTwo);
    }



    @Test
    public void getAllMatches() {
        manager.createMatch(testMatchOne);
        manager.createMatch(testMatchTwo);

        List<Match> expected = Arrays.asList(testMatchOne, testMatchTwo);
        List<Match> actual = manager.findAllMatches();

        assertDeepEquals(expected, actual);
    }

    @Test
    public void getAllMatchesInvalidDisciplineManager() {
        manager.setDisciplineManager(null);
        exception.expect(IllegalStateException.class);
        manager.findAllMatches();
    }

    @Test
    public void getAllMatchesInvalidKnightManager() {
        manager.setKnightManager(null);
        exception.expect(IllegalStateException.class);
        manager.findAllMatches();
    }

    @Test
    public void getAllMatchesInvalidDataSource() {
        manager.setDataSource(null);
        exception.expect(IllegalStateException.class);
        manager.findAllMatches();
    }




    @Test
    public void updateMatchKnight() {
        manager.createMatch(testMatchOne);
        manager.createMatch(testMatchTwo);

        testMatchOne.setKnight(testKnightTwo);   //edit knight
        manager.updateMatch(testMatchOne);
        assertDeepEquals(testMatchOne, manager.getMatchById(testDisciplineOne.getId()));
        //are other records affected?
        assertDeepEquals(testMatchTwo, manager.getMatchById(testMatchTwo.getId()));
    }

    @Test
    public void updateMatchDiscipline() {
        manager.createMatch(testMatchTwo);
        manager.createMatch(testMatchOne);

        testMatchOne.setDiscipline(testDisciplineTwo);   //edit discipline
        manager.updateMatch(testMatchOne);
        assertDeepEquals(testMatchOne, manager.getMatchById(testMatchOne.getId()));
        //are other records affected?
        assertDeepEquals(testMatchTwo, manager.getMatchById(testMatchTwo.getId()));
    }

    @Test
    public void updateMatchPointsNull() {
        manager.createMatch(testMatchOne);
        manager.createMatch(testMatchTwo);

        testMatchOne.setPoints(null);  //set null points
        manager.updateMatch(testMatchOne);
        assertDeepEquals(testMatchOne, manager.getMatchById(testMatchOne.getId()));
        //are other records affected?
        assertDeepEquals(testMatchTwo, manager.getMatchById(testMatchTwo.getId()));
    }

    @Test
    public void updateMatchPoints() {
        manager.createMatch(testMatchOne);
        manager.createMatch(testMatchTwo);

        testMatchOne.setPoints(23); //set not null points
        manager.updateMatch(testMatchOne);
        assertDeepEquals(testMatchOne, manager.getMatchById(testMatchOne.getId()));
        //are other records affected?
        assertDeepEquals(testMatchTwo, manager.getMatchById(testMatchTwo.getId()));
    }

    @Test
    public void updateMatchStartNumber() {
        manager.createMatch(testMatchOne);
        manager.createMatch(testMatchTwo);

        testMatchOne.setStartNumber(321);  //set start number
        manager.updateMatch(testMatchOne);
        assertDeepEquals(testMatchOne, manager.getMatchById(testMatchOne.getId()));

        //are other records affected?
        assertDeepEquals(testMatchTwo, manager.getMatchById(testMatchTwo.getId()));
    }

    @Test
    public void invalidNullUpdate() {
        exception.expect(IllegalArgumentException.class);
        manager.updateMatch(null);
    }

    @Test
    public void invalidUpdateNullId() {
        manager.createMatch(testMatchOne);
        testMatchOne.setId(null);

        exception.expect(IllegalArgumentException.class);
        manager.updateMatch(testMatchOne);
    }

    @Test
    public void invalidUpdateNullKnight() {
        manager.createMatch(testMatchOne);
        testMatchOne.setKnight(null);

        exception.expect(IllegalArgumentException.class);
        manager.updateMatch(testMatchOne);
    }

    @Test
    public void invalidUpdateNullDiscipline() {
        manager.createMatch(testMatchOne);
        testMatchOne.setDiscipline(null);

        exception.expect(IllegalArgumentException.class);
        manager.updateMatch(testMatchOne);
    }

    @Test
    public void invalidUpdateNegativeStartNumber() {
        manager.createMatch(testMatchOne);
        testMatchOne.setStartNumber(-1);

        exception.expect(IllegalArgumentException.class);
        manager.updateMatch(testMatchOne);
    }

    @Test
    public void invalidUpdateNegativePoints() {
        manager.createMatch(testMatchOne);
        testMatchOne.setPoints(-1);

        exception.expect(IllegalArgumentException.class);
        manager.updateMatch(testMatchOne);
    }

    @Test
    public void updateMatchInvalidDisciplineManager() {
        manager.createMatch(testMatchOne);
        manager.setDisciplineManager(null);
        testMatchOne.setDiscipline(testDisciplineTwo);
        exception.expect(IllegalStateException.class);
        manager.updateMatch(testMatchOne);
    }

    @Test
    public void updateMatchInvalidKnightManager() {
        manager.createMatch(testMatchOne);
        manager.setKnightManager(null);
        testMatchOne.setKnight(testKnightTwo);
        exception.expect(IllegalStateException.class);
        manager.updateMatch(testMatchOne);
    }

    @Test
    public void updateMatchInvalidDataSource() {
        manager.setDataSource(null);
        exception.expect(IllegalStateException.class);
        manager.createMatch(testMatchOne);
    }






    @Test
    public void deleteMatch() {
        manager.createMatch(testMatchOne);
        manager.createMatch(testMatchTwo);

        assertNotNull(manager.getMatchById(testMatchOne.getId()));
        assertNotNull(manager.getMatchById(testMatchTwo.getId()));

        manager.deleteMatch(testMatchOne);

        assertNull(manager.getMatchById(testMatchOne.getId()));
        assertNotNull(manager.getMatchById(testMatchTwo.getId()));
    }

    @Test
    public void invalidNonExistentDelete() {
        assertNull(manager.getMatchById(testMatchOne.getId()));
        exception.expect(IllegalArgumentException.class);
        manager.deleteMatch(testMatchOne);
    }

    @Test
    public void deleteNullMatch() {
        exception.expect(IllegalArgumentException.class);
        manager.deleteMatch(null);
    }


    @Test
    public void deleteMatchInvalidDisciplineManager() {
        manager.createMatch(testMatchOne);
        manager.setDisciplineManager(null);
        testMatchOne.setDiscipline(testDisciplineTwo);
        exception.expect(IllegalStateException.class);
        manager.deleteMatch(testMatchOne);
    }

    @Test
    public void deleteMatchInvalidKnightManager() {
        manager.createMatch(testMatchOne);
        manager.setKnightManager(null);
        exception.expect(IllegalStateException.class);
        manager.updateMatch(testMatchOne);
    }

    @Test
    public void deleteMatchInvalidDataSource() {
        manager.setDataSource(null);
        exception.expect(IllegalStateException.class);
        manager.createMatch(testMatchOne);
    }





    @Test
    public void findByKnight() {
        Match m00 = new Match(null, testKnightOne, testDisciplineOne, 1, null);
        Match m10 = new Match(null, testKnightTwo, testDisciplineOne, 2, null);
        Match m01 = new Match(null, testKnightOne, testDisciplineTwo, 1, null);
        Match m11 = new Match(null, testKnightTwo, testDisciplineTwo, 2, null);
        manager.createMatch(m00);
        manager.createMatch(m10);
        manager.createMatch(m01);
        manager.createMatch(m11);

        List<Match> expected = Arrays.asList(m00, m01);
        List<Match> actual = manager.findMatchesForKnight(testKnightOne);
        assertDeepEquals(expected, actual);

        expected = Arrays.asList(m10, m11);
        actual = manager.findMatchesForKnight(testKnightTwo);
        assertDeepEquals(expected, actual);
    }

    @Test
    public void findByUnknownKnight() {
        Knight knight3 = new Knight(3l, "TestKnight2", "TestCastle2", new Date(1), "TestHeraldry2");
        List<Match> actual = manager.findMatchesForKnight(knight3);
        assertEquals(0, actual.size());
    }

    @Test
    public void findByNullIdKnight() {
        testKnightOne.setId(null);
        exception.expect(IllegalArgumentException.class);
        manager.findMatchesForKnight(testKnightOne);
    }

    @Test
    public void findByNullKnight() {
        exception.expect(IllegalArgumentException.class);
        manager.findMatchesForKnight(null);
    }

    @Test
    public void findByKnightInvalidDisciplineManager() {
        manager.createMatch(testMatchOne);
        manager.setDisciplineManager(null);
        exception.expect(IllegalStateException.class);
        manager.findMatchesForKnight(testKnightOne);
    }

    @Test
    public void findByKnightInvalidKnightManager() {
        manager.createMatch(testMatchOne);
        manager.setKnightManager(null);
        exception.expect(IllegalStateException.class);
        manager.findMatchesForKnight(testKnightOne);
    }

    @Test
    public void findByKnightInvalidDataSource() {
        manager.createMatch(testMatchOne);
        manager.setDataSource(null);
        exception.expect(IllegalStateException.class);
        manager.findMatchesForKnight(testKnightOne);
    }




    @Test
    public void findByDiscipline() {
        Match m00 = new Match(null, testKnightOne, testDisciplineOne, 1, null);
        Match m10 = new Match(null, testKnightTwo, testDisciplineOne, 2, null);
        Match m01 = new Match(null, testKnightOne, testDisciplineTwo, 1, null);
        Match m11 = new Match(null, testKnightTwo, testDisciplineTwo, 2, null);
        manager.createMatch(m00);
        manager.createMatch(m10);
        manager.createMatch(m01);
        manager.createMatch(m11);

        List<Match> expected = Arrays.asList(m00, m10);
        List<Match> actual = manager.findMatchesForDiscipline(testDisciplineOne);
        assertDeepEquals(expected, actual);

        expected = Arrays.asList(m01, m11);
        actual = manager.findMatchesForDiscipline(testDisciplineTwo);
        assertDeepEquals(expected, actual);
    }

    @Test
    public void findByUnknownDiscipline() {
        Discipline discipline3 = new Discipline(3l, "TestDiscipline2", new Timestamp(1), new Timestamp(1001*60), 4);
        List<Match> actual = manager.findMatchesForDiscipline(discipline3);
        assertEquals(0, actual.size());
    }

    @Test
    public void findByNullDiscipline() {
        exception.expect(IllegalArgumentException.class);
        manager.findMatchesForDiscipline(null);
    }

    @Test
    public void findByNullIdDiscipline() {
        testDisciplineOne.setId(null);
        exception.expect(IllegalArgumentException.class);
        manager.findMatchesForDiscipline(testDisciplineOne);
    }

    @Test
    public void findByDisciplineInvalidDisciplineManager() {
        manager.createMatch(testMatchOne);
        manager.setDisciplineManager(null);
        exception.expect(IllegalStateException.class);
        manager.findMatchesForDiscipline(testDisciplineOne);
    }

    @Test
    public void findByDisciplineInvalidKnightManager() {
        manager.createMatch(testMatchOne);
        manager.setKnightManager(null);
        exception.expect(IllegalStateException.class);
        manager.findMatchesForDiscipline(testDisciplineOne);
    }

    @Test
    public void findByDisciplineInvalidDataSource() {
        manager.createMatch(testMatchOne);
        manager.setDataSource(null);
        exception.expect(IllegalStateException.class);
        manager.findMatchesForDiscipline(testDisciplineOne);
    }






    @Test
    public void findByBoth() {
        assertNull(manager.findMatchForKnightAndDiscipline(testKnightOne, testDisciplineTwo));
        assertNull(manager.findMatchForKnightAndDiscipline(testKnightTwo, testDisciplineOne));
        assertNull(manager.findMatchForKnightAndDiscipline(testKnightTwo, testDisciplineTwo));
        assertNull(manager.findMatchForKnightAndDiscipline(testKnightOne, testDisciplineOne));

        manager.createMatch(testMatchOne);
        manager.createMatch(testMatchTwo);

        assertNull(manager.findMatchForKnightAndDiscipline(testMatchTwo.getKnight(), testMatchOne.getDiscipline()));
        assertNull(manager.findMatchForKnightAndDiscipline(testMatchOne.getKnight(), testMatchTwo.getDiscipline()));
        assertDeepEquals(testMatchOne, manager.findMatchForKnightAndDiscipline(testMatchOne.getKnight(), testMatchOne.getDiscipline()));
        assertDeepEquals(testMatchTwo, manager.findMatchForKnightAndDiscipline(testMatchTwo.getKnight(), testMatchTwo.getDiscipline()));
    }

    @Test
    public void findByBothNullKnight() {
        exception.expect(IllegalArgumentException.class);
        manager.findMatchForKnightAndDiscipline(null, testDisciplineOne);
    }

    @Test
    public void findByBothNullDiscipline() {
        exception.expect(IllegalArgumentException.class);
        manager.findMatchForKnightAndDiscipline(testKnightOne, null);
    }

    @Test
    public void findByBothNullIdKnight() {
        testKnightOne.setId(null);
        exception.expect(IllegalArgumentException.class);
        manager.findMatchForKnightAndDiscipline(testKnightOne, testDisciplineOne);
    }

    @Test
    public void findByBothNullIdDiscipline() {
        testDisciplineOne.setId(null);
        exception.expect(IllegalArgumentException.class);
        manager.findMatchForKnightAndDiscipline(testKnightOne, testDisciplineOne);
    }

    @Test
    public void findByBothNonExistentKnight() {
        manager.createMatch(testMatchOne);
        manager.createMatch(testMatchTwo);
        testKnightOne.setId(12L);
        assertNull(mockKnightManager.getKnightById(12L));
        assertNull(manager.findMatchForKnightAndDiscipline(testKnightOne, testDisciplineOne));
    }

    @Test
    public void findByBothNonExistentDiscipline() {
        manager.createMatch(testMatchOne);
        manager.createMatch(testMatchTwo);
        testDisciplineOne.setId(12L);
        assertNull(mockDisciplineManager.getDisciplineById(12L));
        assertNull(manager.findMatchForKnightAndDiscipline(testKnightOne, testDisciplineOne));
    }


    @Test
    public void findByBothInvalidDisciplineManager() {
        manager.createMatch(testMatchOne);
        manager.setDisciplineManager(null);
        exception.expect(IllegalStateException.class);
        manager.findMatchForKnightAndDiscipline(testKnightOne, testDisciplineOne);
    }

    @Test
    public void findByBothInvalidKnightManager() {
        manager.createMatch(testMatchOne);
        manager.setKnightManager(null);
        exception.expect(IllegalStateException.class);
        manager.findMatchForKnightAndDiscipline(testKnightOne, testDisciplineOne);
    }

    @Test
    public void findByBothInvalidDataSource() {
        manager.createMatch(testMatchOne);
        manager.setDataSource(null);
        exception.expect(IllegalStateException.class);
        manager.findMatchForKnightAndDiscipline(testKnightOne, testDisciplineOne);
    }



    private static void assertDeepEquals(List<Match> expected, List<Match> actual) {
        assertEquals(expected.size(), actual.size());
        Collections.sort(expected, idComparator);
        Collections.sort(actual, idComparator);
        for (int i=0; i<actual.size(); i++) assertDeepEquals(expected.get(i), actual.get(i));
    }

    private static void assertDeepEquals(Match expected, Match actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getKnight(), actual.getKnight());
        assertEquals(expected.getDiscipline(), actual.getDiscipline());
        assertEquals(expected.getPoints(), actual.getPoints());
        assertEquals(expected.getStartNumber(), actual.getStartNumber());
    }

    private static Comparator<Match> idComparator = new Comparator<Match>() {
        @Override
        public int compare(Match match, Match testMatchTwo) {
            if (match.getId() == null || testMatchTwo.getId() == null) throw new IllegalArgumentException("Cant compare null ids");
            return match.getId().compareTo(testMatchTwo.getId());
        }
    };
}
