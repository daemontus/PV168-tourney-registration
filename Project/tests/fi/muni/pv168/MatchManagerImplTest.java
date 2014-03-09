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

        assertNull(manager.getMatchById(1L));

        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Date(0), new Date(1000*60), 10);

        Match match = new Match(1L, knight, discipline, 5, 120);
        manager.createMatch(match);
        Long id = match.getId();

        assertNotNull(id);

        Match result = manager.getMatchById(match.getId());
        assertEquals(match, result);
        assertNotSame(match, result);
        assertDeepEquals(match, result);

    }

    @Test
    public void getAllMatches() {

        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Date(0), new Date(1000*60), 10);

        Match match1 = new Match(1L, knight, discipline, 5, 120);
        manager.createMatch(match1);
        Match match2 = new Match(2L, knight, discipline, 4, null);
        manager.createMatch(match2);

        List<Match> expected = Arrays.asList(match1, match2);
        List<Match> actual = manager.findAllMatches();

        Collections.sort(expected, idComparator);
        Collections.sort(actual, idComparator);

        assertEquals(expected.size(), actual.size());

        for (int i=0; i<actual.size(); i++) {
            assertDeepEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void deleteMatch() {

        Knight knight = new Knight(1l, "TestKnight", "TestCastle", new Date(0), "TestHeraldry");
        Discipline discipline = new Discipline(1l, "TestDiscipline", new Date(0), new Date(1000*60), 10);

        Match match1 = new Match(1L, knight, discipline, 5, 120);
        manager.createMatch(match1);
        Match match2 = new Match(2L, knight, discipline, 4, null);
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

        Match match = new Match(1L, knight, discipline, 5, 120);

        assertNull(manager.getMatchById(1L));

        try {
            manager.deleteMatch(null);
            fail();
        } catch (IllegalArgumentException e) {
            //ok
        }

        try {
            match.setId(null);
            manager.deleteMatch(match);
            fail();
        } catch (IllegalArgumentException e) {
            //ok
        }

        try {
            match.setId(1L);
            manager.deleteMatch(match);
            fail();
        } catch (IllegalArgumentException e) {
            //ok
        }
    }



    private void assertDeepEquals(Match expected, Match actual) {
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
