package fi.muni.pv168;

import java.util.List;

/**
 * <p>Implementation of MatchManager using JDBC.</p>
 */
public class MatchManagerImpl implements MatchManager{

	@Override
	public void createMatch(Match match) {
		// TODO - implement fi.muni.pv168.MatchManagerImpl.createMatch
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateMatch(Match match) {
		// TODO - implement fi.muni.pv168.MatchManagerImpl.updateMatch
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteMatch(Match match) {
		// TODO - implement fi.muni.pv168.MatchManagerImpl.deleteMatch
		throw new UnsupportedOperationException();
	}

    @Override
	public List<Match> findAllMatches() {
		// TODO - implement fi.muni.pv168.MatchManagerImpl.findAllMatches
		throw new UnsupportedOperationException();
	}

	@Override
	public Match getMatchById(Long id) {
		// TODO - implement fi.muni.pv168.MatchManagerImpl.getMatchById
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Match> findMatchesForKnight(Knight knight) {
		// TODO - implement fi.muni.pv168.MatchManagerImpl.findMatchesForKnight
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Match> findMatchesForDiscipline(Discipline discipline) {
		// TODO - implement fi.muni.pv168.MatchManagerImpl.findMatchesForDiscipline
		throw new UnsupportedOperationException();
	}

	@Override
	public Match findMatchForKnightAndDiscipline(Knight knight, Discipline discipline) {
		// TODO - implement fi.muni.pv168.MatchManagerImpl.findMatchForKnightAndDiscipline
		throw new UnsupportedOperationException();
	}

}