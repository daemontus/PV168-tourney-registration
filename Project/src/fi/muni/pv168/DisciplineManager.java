package fi.muni.pv168;

import java.sql.Date;
import java.util.List;

/**
 * <p>Manager responsible for storage of Disciplines.</p>
 */
public interface DisciplineManager {

	/**
	 * Create new discipline.
	 * @param discipline Discipline to create.
	 */
	void createDiscipline(Discipline discipline);

	/**
	 * Get discipline by id.
	 * @param id Id of discipline.
     * @return Discipline with specified id, or null if not found.
	 */
	Discipline getDisciplineById(Long id);

    /**
     * Get all disciplines in database.
     * @return All available disciplines
     */
	List<Discipline> findAllDisciplines();

    /**
     * Return all disciplines that are held during one day.
     * @param day Day we want disciplines for.
     * @return All disciplines for specified day.
     */
	List<Discipline> getDisciplinesByDate(Date day);

    /**
     * Update discipline with new data.
     * @param discipline Discipline with new data.
     */
	void updateDiscipline(Discipline discipline);

	/**
	 * Delete discipline.
	 * @param discipline Discipline you want to delete from database.
	 */
	void deleteDiscipline(Discipline discipline);

}