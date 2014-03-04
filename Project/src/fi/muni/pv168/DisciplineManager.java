package fi.muni.pv168;

import java.util.List;

public interface DisciplineManager {

	/**
	 * 
	 * @param discipline
	 */
	void createDiscipline(Discipline discipline);

	/**
	 * 
	 * @param id
	 */
	Discipline getDisciplineById(Long id);

	List<Discipline> findAllDisciplines();

	List<Discipline> getDisciplinesByDay();

	/**
	 * 
	 * @param discipline
	 */
	void updateDiscipline(Discipline discipline);

	/**
	 * 
	 * @param discipline
	 */
	void deleteDiscipline(Discipline discipline);

}