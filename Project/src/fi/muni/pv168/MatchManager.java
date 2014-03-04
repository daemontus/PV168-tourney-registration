package fi.muni.pv168;

import java.util.List;

public interface MatchManager {

	/**
	 * 
	 * @param match
	 */
	void createMatch(Match match);

	/**
	 * 
	 * @param match
	 */
	void updateMatch(Match match);

	/**
	 * 
	 * @param match
	 */
	void deleteMatch(Match match);

	List<Match> findAllMatches();

	/**
	 * 
	 * @param id
	 */
	Match getMatchById(Long id);

	/**
	 * 
	 * @param knight
	 */
	List<Match> findMatchesForKnight(Knight knight);

	/**
	 * 
	 * @param discipline
	 */
	List<Match> findMatchesForDiscipline(Discipline discipline);

	/**
	 * 
	 * @param knight
	 * @param discipline
	 */
	Match findMatchForKnightAndDiscipline(Knight knight, Discipline discipline);

}