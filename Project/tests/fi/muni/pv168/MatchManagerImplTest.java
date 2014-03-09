package fi.muni.pv168;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
/**
 *
 * @author Samuel Pastva
 * @version 09/03/2014
 */
public class MatchManagerImplTest {

    private MatchManagerImpl manager;

    @Before
    public void setUp() {
        manager = new MatchManagerImpl();
    }

    @Test
    public void createMatch() {

        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Date(0), new Date(1000*60), 10);

        Match match = new Match(null, knight, discipline, 5, 120);    //points not null
        Match match2 = new Match(null, knight, discipline, 5, null);    //points null
        manager.createMatch(match);
        manager.createMatch(match2);
        Long id = match.getId();

        assertNotNull(id);

        Match result = manager.getMatchById(match.getId());
        assertEquals(match, result);
        assertNotSame(match, result);
        assertDeepEquals(match, result);

        id = match2.getId();

        assertNotNull(id);

        result = manager.getMatchById(match.getId());
        assertEquals(match, result);
        assertNotSame(match, result);
        assertDeepEquals(match, result);
    }

    @Test
    public void invalidCreate() {

        try {
            manager.createMatch(null);
            fail();
        } catch (IllegalArgumentException e) { /*ok*/ }

        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Date(0), new Date(1000*60), 10);

        try {   // id not null
            manager.createMatch(new Match(1L, knight, discipline, 5, 120));
            fail();
        } catch (IllegalArgumentException e) { /*ok*/ }

        try {   // knight null
            manager.createMatch(new Match(null, null, discipline, 5, 120));
            fail();
        } catch (IllegalArgumentException e) { /*ok*/ }

        try {   // discipline null
            manager.createMatch(new Match(null, knight, null, 5, 120));
            fail();
        } catch (IllegalArgumentException e) { /*ok*/ }

        try {   // number below 0
            manager.createMatch(new Match(null, knight, discipline, -1, 120));
            fail();
        } catch (IllegalArgumentException e) { /*ok*/ }

        try {   // points below 0
            manager.createMatch(new Match(null, knight, discipline, 3, -1));
            fail();
        } catch (IllegalArgumentException e) { /*ok*/ }
    }

    @Test
    public void getAllMatches() {

        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Date(0), new Date(1000*60), 10);

        Match match1 = new Match(null, knight, discipline, 5, 120);
        manager.createMatch(match1);
        Match match2 = new Match(null, knight, discipline, 4, null);
        manager.createMatch(match2);

        List<Match> expected = Arrays.asList(match1, match2);
        List<Match> actual = manager.findAllMatches();

        assertDeepEquals(expected, actual);
    }

    @Test
    public void updateMatch() {

        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Knight knight2 = new Knight(2l, "TestKnight2", "TestCastle2", new Date(1), "TestHeraldry2");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Date(0), new Date(1000*60), 10);
        Discipline discipline2 = new Discipline(2l, "TestDiscipline2", new Date(1), new Date(1001*60), 4);

        Match match = new Match(null, knight, discipline, 5, 120);
        Match match2 = new Match(null, knight, discipline, 5, null);
        manager.createMatch(match);
        manager.createMatch(match2);
        Long id = match.getId();

        match.setKnight(knight2);   //edit knight
        manager.updateMatch(match);
        assertDeepEquals(match, manager.getMatchById(id));

        match.setDiscipline(discipline2);   //edit discipline
        manager.updateMatch(match);
        assertDeepEquals(match, manager.getMatchById(id));

        match.setPoints(null);  //set null points
        manager.updateMatch(match);
        assertDeepEquals(match, manager.getMatchById(id));

        match.setPoints(23); //set not null points
        manager.updateMatch(match);
        assertDeepEquals(match, manager.getMatchById(id));

        match.setStartNumber(321);  //set start number
        manager.updateMatch(match);
        assertDeepEquals(match, manager.getMatchById(id));

        //are other records affected?
        assertDeepEquals(match2, manager.getMatchById(match2.getId()));
    }

    @Test
    public void invalidUpdate() {

        try {
            manager.updateMatch(null);
            fail();
        } catch (IllegalArgumentException e) { /*ok*/ }

        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Date(0), new Date(1000*60), 10);

        Match match = new Match(null, knight, discipline, 5, 120);
        manager.createMatch(match);
        Long id = match.getId();

        try {
            Match m = manager.getMatchById(id);
            m.setId(null);
            manager.updateMatch(m);
            fail();
        } catch (IllegalArgumentException e) { /*ok*/ }

        try {
            Match m = manager.getMatchById(id);
            m.setKnight(null);
            manager.updateMatch(m);
            fail();
        } catch (IllegalArgumentException e) { /*ok*/ }

        try {
            Match m = manager.getMatchById(id);
            m.setDiscipline(null);
            manager.updateMatch(m);
            fail();
        } catch (IllegalArgumentException e) { /*ok*/ }

        try {
            Match m = manager.getMatchById(id);
            m.setStartNumber(-1);
            manager.updateMatch(m);
            fail();
        } catch (IllegalArgumentException e) { /*ok*/ }

        try {
            Match m = manager.getMatchById(id);
            m.setPoints(-1);
            manager.updateMatch(m);
            fail();
        } catch (IllegalArgumentException e) { /*ok*/ }
    }

    @Test
    public void deleteMatch() {

        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Date(0), new Date(1000*60), 10);

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
    public void invalidDelete() {

        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Date(0), new Date(1000*60), 10);

        Match match = new Match(null, knight, discipline, 5, 120);

        try {
            manager.deleteMatch(null);
            fail();
        } catch (IllegalArgumentException e) { /*ok*/ }

        try {
            match.setId(null);
            manager.deleteMatch(match);
            fail();
        } catch (IllegalArgumentException e) { /*ok*/ }

        try {
            match.setId(1L);
            assertNull(manager.getMatchById(match.getId()));
            manager.deleteMatch(match);
            fail();
        } catch (IllegalArgumentException e) { /*ok*/ }
    }

    @Test
    public void findByKnight() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Knight knight2 = new Knight(2l, "TestKnight2", "TestCastle2", new Date(1), "TestHeraldry2");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Date(0), new Date(1000*60), 10);
        Discipline discipline2 = new Discipline(2l, "TestDiscipline2", new Date(1), new Date(1001*60), 4);

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

        Knight knight3 = new Knight(3l, "TestKnight2", "TestCastle2", new Date(1), "TestHeraldry2");
        actual = manager.findMatchesForKnight(knight3);
        assertEquals(0, actual.size());

        try {
            manager.findMatchesForKnight(null);
            fail();
        } catch (IllegalArgumentException e) { /*ok*/ }
    }

    @Test
    public void findByDiscipline() {
        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Knight knight2 = new Knight(2l, "TestKnight2", "TestCastle2", new Date(1), "TestHeraldry2");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Date(0), new Date(1000*60), 10);
        Discipline discipline2 = new Discipline(2l, "TestDiscipline2", new Date(1), new Date(1001*60), 4);

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

        Discipline discipline3 = new Discipline(3l, "TestDiscipline2", new Date(1), new Date(1001*60), 4);
        actual = manager.findMatchesForDiscipline(discipline3);
        assertEquals(0, actual.size());

        try {
            manager.findMatchesForDiscipline(null);
            fail();
        } catch (IllegalArgumentException e) { /*ok*/ }
    }

    @Test
    public void findByBoth() {

        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Knight knight2 = new Knight(2l, "TestKnight2", "TestCastle2", new Date(1), "TestHeraldry2");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Date(0), new Date(1000*60), 10);
        Discipline discipline2 = new Discipline(2l, "TestDiscipline2", new Date(1), new Date(1001*60), 4);

        assertNull(manager.findMatchForKnightAndDiscipline(knight, discipline));

        Match m00 = new Match(null, knight, discipline, 1, null);
        Match m11 = new Match(null, knight2, discipline2, 2, null);
        manager.createMatch(m00);
        manager.createMatch(m11);

        assertNull(manager.findMatchForKnightAndDiscipline(knight, discipline2));
        assertNull(manager.findMatchForKnightAndDiscipline(knight2, discipline));
        assertNotNull(manager.findMatchForKnightAndDiscipline(knight2, discipline2));
        assertNotNull(manager.findMatchForKnightAndDiscipline(knight, discipline));

        try {
            manager.findMatchForKnightAndDiscipline(null, discipline);
            fail();
        } catch (IllegalArgumentException e) { /*ok*/ }

        try {
            manager.findMatchForKnightAndDiscipline(knight, null);
            fail();
        } catch (IllegalArgumentException e) { /*ok*/ }

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
