package fi.muni.pv168;

import javax.sql.DataSource;
import java.util.List;

/**
 * <p>Implementation of MatchManager using JDBC.</p>
 */
public class MatchManagerImpl implements MatchManager{

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void checkDataSource() {
        if (dataSource == null) throw new IllegalStateException("Data source not set");
    }

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

    private void validate(Match match) {
        if (match.getPoints() != null && match.getPoints() < 0) throw new IllegalArgumentException("Match points below zero.");
        if (match.getStartNumber() < 0) throw new IllegalArgumentException("Match starting number below zero.");
        if (match.getDiscipline() == null) throw new IllegalArgumentException("Match with null discipline.");
        if (match.getKnight() == null) throw new IllegalArgumentException("Match with null knight.");
    }

}