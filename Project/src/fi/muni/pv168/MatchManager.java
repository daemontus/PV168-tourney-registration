package fi.muni.pv168;

import java.util.List;

/**
 * <p>Manager responsible for storage of Matches.</p>
 */
public interface MatchManager {

    /**
     * Create new match.
     * @param match Match to create.
     */
	void createMatch(Match match);

    /**
     * Update match with new data.
     * @param match Match with new data.
     */
    void updateMatch(Match match);

    /**
     * Delete match.
     * @param match Match you want to delete from database.
     */
    void deleteMatch(Match match);

    /**
     * Get all matches in database.
     * @return All available matches
     */
	List<Match> findAllMatches();

    /**
     * Get match by id.
     * @param id Id of match.
     * @return Match with specified id, or null if not found.
     */
	Match getMatchById(Long id);

	/**
	 * Find all matches for specified knight.
	 * @param knight Knight you want matches for.
     * @return List of Matches specified Knight is part of.
	 */
	List<Match> findMatchesForKnight(Knight knight);

    /**
     * Find all matches for specified discipline.
     * @param discipline Discipline you want matches for.
     * @return List of Matches specified Discipline is part of.
     */
	List<Match> findMatchesForDiscipline(Discipline discipline);

	/**
	 * Find one specific Match using Knight and Discipline.
	 * @param knight Knight
	 * @param discipline Discipline
     * @return Match, if specified Knight rides in specified Discipline, null otherwise.
	 */
	Match findMatchForKnightAndDiscipline(Knight knight, Discipline discipline);
}