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
 *
 * @author Samuel Pastva
 * @version 09/03/2014
 */
public class MatchManagerImplTest {

    private MatchManagerImpl manager;
    private BasicDataSource dataSource;

    @Before
    public void setUp() throws SQLException {
        manager = new MatchManagerImpl();
        dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:derby:memory:knight-manager-test;create=true");
        DBUtils.executeSqlScript(dataSource, MatchManager.class.getResource("createTables.sql"));
        manager = new MatchManagerImpl();
        manager.setDataSource(dataSource);
    }

    @After
    public void cleanUp() throws SQLException {
        DBUtils.executeSqlScript(dataSource, KnightManager.class.getResource("dropTables.sql"));
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void createMatch() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);
        Match match = new Match(null, knight, discipline, 5, 120);
        manager.createMatch(match);
        Long id = match.getId();

        assertNotNull(id);
        Match result = manager.getMatchById(match.getId());
        assertEquals(match, result);
        assertNotSame(match, result);
        assertDeepEquals(match, result);
    }

    @Test
    public void createNullPointsMatch() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);
        Match match = new Match(null, knight, discipline, 5, null);
        manager.createMatch(match);
        Long id = match.getId();

        assertNotNull(id);
        Match result = manager.getMatchById(match.getId());
        assertEquals(match, result);
        assertNotSame(match, result);
        assertDeepEquals(match, result);
    }

    @Test
    public void invalidPointsBelowZeroCreate() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);
        exception.expect(IllegalArgumentException.class);
        manager.createMatch(new Match(null, knight, discipline, 3, -1));
    }

    @Test
    public void invalidNumberBelowZeroCreate() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);
        exception.expect(IllegalArgumentException.class);
        manager.createMatch(new Match(null, knight, discipline, -1, 120));
    }

    @Test
    public void invalidNullDisciplineCreate() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        exception.expect(IllegalArgumentException.class);
        manager.createMatch(new Match(null, knight, null, 5, 120));
    }

    @Test
    public void invalidNullKnightCreate() {
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);
        exception.expect(IllegalArgumentException.class);
        manager.createMatch(new Match(null, null, discipline, 5, 120));
    }

    @Test
    public void invalidIdCreate() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);
        exception.expect(IllegalArgumentException.class);
        manager.createMatch(new Match(1L, knight, discipline, 5, 120));
    }

    @Test
    public void invalidNullCreate() {
        exception.expect(IllegalArgumentException.class);
        manager.createMatch(null);
    }

    @Test
    public void getAllMatches() {

        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Knight knight2 = new Knight(2l, "TestKnight2", "TestCastle2", new Date(1), "TestHeraldry2");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);

        Match match1 = new Match(null, knight, discipline, 5, 120);
        manager.createMatch(match1);
        Match match2 = new Match(null, knight2, discipline, 4, null);
        manager.createMatch(match2);

        List<Match> expected = Arrays.asList(match1, match2);
        List<Match> actual = manager.findAllMatches();

        assertDeepEquals(expected, actual);
    }

    @Test
    public void updateMatchKnight() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Knight knight2 = new Knight(2l, "TestKnight2", "TestCastle2", new Date(1), "TestHeraldry2");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);
        Discipline discipline2 = new Discipline(2l, "TestDiscipline2", new Timestamp(1), new Timestamp(1001*60), 4);

        Match match = new Match(null, knight, discipline, 5, 120);
        Match match2 = new Match(null, knight, discipline2, 5, null);
        manager.createMatch(match);
        manager.createMatch(match2);
        Long id = match.getId();

        match.setKnight(knight2);   //edit knight
        manager.updateMatch(match);
        assertDeepEquals(match, manager.getMatchById(id));

        //are other records affected?
        assertDeepEquals(match2, manager.getMatchById(match2.getId()));
    }

    @Test
    public void updateMatchDiscipline() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Knight knight2 = new Knight(2l, "TestKnight2", "TestCastle2", new Date(1), "TestHeraldry2");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);
        Discipline discipline2 = new Discipline(2l, "TestDiscipline2", new Timestamp(1), new Timestamp(1001*60), 4);

        Match match = new Match(null, knight, discipline, 5, 120);
        Match match2 = new Match(null, knight2, discipline, 5, null);
        manager.createMatch(match);
        manager.createMatch(match2);
        Long id = match.getId();

        match.setDiscipline(discipline2);   //edit discipline
        manager.updateMatch(match);
        assertDeepEquals(match, manager.getMatchById(id));

        //are other records affected?
        assertDeepEquals(match2, manager.getMatchById(match2.getId()));
    }

    @Test
    public void updateMatchPointsNull() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Knight knight2 = new Knight(2l, "TestKnight2", "TestCastle2", new Date(1), "TestHeraldry2");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);

        Match match = new Match(null, knight, discipline, 5, 120);
        Match match2 = new Match(null, knight2, discipline, 5, null);
        manager.createMatch(match);
        manager.createMatch(match2);
        Long id = match.getId();

        match.setPoints(null);  //set null points
        manager.updateMatch(match);
        assertDeepEquals(match, manager.getMatchById(id));

        //are other records affected?
        assertDeepEquals(match2, manager.getMatchById(match2.getId()));
    }

    @Test
    public void updateMatchPoints() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Knight knight2 = new Knight(2l, "TestKnight2", "TestCastle2", new Date(1), "TestHeraldry2");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);

        Match match = new Match(null, knight, discipline, 5, 120);
        Match match2 = new Match(null, knight2, discipline, 5, null);
        manager.createMatch(match);
        manager.createMatch(match2);
        Long id = match.getId();

        match.setPoints(23); //set not null points
        manager.updateMatch(match);
        assertDeepEquals(match, manager.getMatchById(id));

        //are other records affected?
        assertDeepEquals(match2, manager.getMatchById(match2.getId()));
    }

    @Test
    public void updateMatchStartNumber() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Knight knight2 = new Knight(2l, "TestKnight2", "TestCastle2", new Date(1), "TestHeraldry2");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);

        Match match = new Match(null, knight, discipline, 5, 120);
        Match match2 = new Match(null, knight2, discipline, 5, null);
        manager.createMatch(match);
        manager.createMatch(match2);
        Long id = match.getId();

        match.setStartNumber(321);  //set start number
        manager.updateMatch(match);
        assertDeepEquals(match, manager.getMatchById(id));

        //are other records affected?
        assertDeepEquals(match2, manager.getMatchById(match2.getId()));
    }

    @Test
    public void invalidNullUpdate() {
        exception.expect(IllegalArgumentException.class);
        manager.updateMatch(null);
    }

    @Test
    public void invalidUpdateNullId() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);

        Match match = new Match(null, knight, discipline, 5, 120);
        manager.createMatch(match);
        Long id = match.getId();
        Match m = manager.getMatchById(id);
        m.setId(null);

        exception.expect(IllegalArgumentException.class);
        manager.updateMatch(m);
    }

    @Test
    public void invalidUpdateNullKnight() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);

        Match match = new Match(null, knight, discipline, 5, 120);
        manager.createMatch(match);
        Long id = match.getId();
        Match m = manager.getMatchById(id);
        m.setKnight(null);

        exception.expect(IllegalArgumentException.class);
        manager.updateMatch(m);
    }

    @Test
    public void invalidUpdateNullDiscipline() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);

        Match match = new Match(null, knight, discipline, 5, 120);
        manager.createMatch(match);
        Long id = match.getId();
        Match m = manager.getMatchById(id);
        m.setDiscipline(null);

        exception.expect(IllegalArgumentException.class);
        manager.updateMatch(m);
    }

    @Test
    public void invalidUpdateNegativeStartNumber() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);

        Match match = new Match(null, knight, discipline, 5, 120);
        manager.createMatch(match);
        Long id = match.getId();
        Match m = manager.getMatchById(id);
        m.setStartNumber(-1);

        exception.expect(IllegalArgumentException.class);
        manager.updateMatch(m);
    }

    @Test
    public void invalidUpdateNegativePoints() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);

        Match match = new Match(null, knight, discipline, 5, 120);
        manager.createMatch(match);
        Long id = match.getId();
        Match m = manager.getMatchById(id);
        m.setPoints(-1);

        exception.expect(IllegalArgumentException.class);
        manager.updateMatch(m);
    }

    @Test
    public void deleteMatch() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);

        Match match1 = new Match(null, knight, discipline, 5, 120);
        manager.createMatch(match1);
        Match match2 = new Match(null, knight, discipline, 4, null);
        manager.createMatch(match2);

        assertNotNull(manager.getMatchById(match1.getId()));
        assertNotNull(manager.getMatchById(match2.getId()));

        manager.deleteMatch(match1);

        assertNull(manager.getMatchById(match1.getId()));
        assertNotNull(manager.getMatchById(match2.getId()));
    }

    @Test
    public void invalidNonExistentDelete() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);

        Match match = new Match(null, knight, discipline, 5, 120);
        assertNull(manager.getMatchById(match.getId()));

        exception.expect(IllegalArgumentException.class);
        manager.deleteMatch(match);
    }

    @Test
    public void deleteNullMatch() {
        exception.expect(IllegalArgumentException.class);
        manager.deleteMatch(null);
    }

    @Test
    public void invalidNullIdDelete() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);
        exception.expect(IllegalArgumentException.class);
        manager.deleteMatch(new Match(null, knight, discipline, 5, 120));
    }

    @Test
    public void findByKnight() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Knight knight2 = new Knight(2l, "TestKnight2", "TestCastle2", new Date(1), "TestHeraldry2");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);
        Discipline discipline2 = new Discipline(2l, "TestDiscipline2", new Timestamp(1), new Timestamp(1001*60), 4);

        Match m00 = new Match(null, knight, discipline, 1, null);
        Match m10 = new Match(null, knight2, discipline, 2, null);
        Match m01 = new Match(null, knight, discipline2, 1, null);
        Match m11 = new Match(null, knight2, discipline2, 2, null);
        manager.createMatch(m00);
        manager.createMatch(m10);
        manager.createMatch(m01);
        manager.createMatch(m11);

        List<Match> expected = Arrays.asList(m00, m01);
        List<Match> actual = manager.findMatchesForKnight(knight);
        assertDeepEquals(expected, actual);

        expected = Arrays.asList(m10, m11);
        actual = manager.findMatchesForKnight(knight2);
        assertDeepEquals(expected, actual);
    }

    @Test
    public void findByUnknownKnight() {
        Knight knight3 = new Knight(3l, "TestKnight2", "TestCastle2", new Date(1), "TestHeraldry2");
        List<Match> actual = manager.findMatchesForKnight(knight3);
        assertEquals(0, actual.size());
    }

    @Test
    public void findByNullKnight() {
        exception.expect(IllegalArgumentException.class);
        manager.findMatchesForKnight(null);
    }

    @Test
    public void findByDiscipline() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Knight knight2 = new Knight(2l, "TestKnight2", "TestCastle2", new Date(1), "TestHeraldry2");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);
        Discipline discipline2 = new Discipline(2l, "TestDiscipline2", new Timestamp(1), new Timestamp(1001*60), 4);

        Match m00 = new Match(null, knight, discipline, 1, null);
        Match m10 = new Match(null, knight2, discipline, 2, null);
        Match m01 = new Match(null, knight, discipline2, 1, null);
        Match m11 = new Match(null, knight2, discipline2, 2, null);
        manager.createMatch(m00);
        manager.createMatch(m10);
        manager.createMatch(m01);
        manager.createMatch(m11);

        List<Match> expected = Arrays.asList(m00, m10);
        List<Match> actual = manager.findMatchesForDiscipline(discipline);
        assertDeepEquals(expected, actual);

        expected = Arrays.asList(m01, m11);
        actual = manager.findMatchesForDiscipline(discipline2);
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
    public void findByBoth() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Knight knight2 = new Knight(2l, "TestKnight2", "TestCastle2", new Date(1), "TestHeraldry2");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);
        Discipline discipline2 = new Discipline(2l, "TestDiscipline2", new Timestamp(1), new Timestamp(1001*60), 4);

        assertNull(manager.findMatchForKnightAndDiscipline(knight, discipline2));
        assertNull(manager.findMatchForKnightAndDiscipline(knight2, discipline));
        assertNull(manager.findMatchForKnightAndDiscipline(knight2, discipline2));
        assertNull(manager.findMatchForKnightAndDiscipline(knight, discipline));

        Match m00 = new Match(null, knight, discipline, 1, null);
        Match m11 = new Match(null, knight2, discipline2, 2, null);
        manager.createMatch(m00);
        manager.createMatch(m11);

        assertNull(manager.findMatchForKnightAndDiscipline(knight, discipline2));
        assertNull(manager.findMatchForKnightAndDiscipline(knight2, discipline));
        assertNotNull(manager.findMatchForKnightAndDiscipline(knight2, discipline2));
        assertNotNull(manager.findMatchForKnightAndDiscipline(knight, discipline));
    }

    @Test
    public void findByBothNullKnight() {
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Timestamp(0), new Timestamp(1000*60), 10);
        exception.expect(IllegalArgumentException.class);
        manager.findMatchForKnightAndDiscipline(null, discipline);
    }

    @Test
    public void findByBothNullDiscipline() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        exception.expect(IllegalArgumentException.class);
        manager.findMatchForKnightAndDiscipline(knight, null);
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
        public int compare(Match match, Match match2) {
            if (match.getId() == null || match2.getId() == null) throw new IllegalArgumentException("Cant compare null ids");
            return match.getId().compareTo(match2.getId());
        }
    };
}
