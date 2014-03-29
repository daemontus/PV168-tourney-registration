package fi.muni.pv168;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

/**
 * <p>Implementation of Discipline Manager using JDBC.</p>
 */
public class DisciplineManagerImpl implements DisciplineManager {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
	public void createDiscipline(Discipline discipline) {
		// TODO - implement fi.muni.pv168.DisciplineManagerImpl.createDiscipline
		throw new UnsupportedOperationException();
	}

    @Override
	public Discipline getDisciplineById(Long id) {
		// TODO - implement fi.muni.pv168.DisciplineManagerImpl.getDisciplineById
		throw new UnsupportedOperationException();
	}

    @Override
	public List<Discipline> findAllDisciplines() {
		// TODO - implement fi.muni.pv168.DisciplineManagerImpl.findAllDisciplines
		throw new UnsupportedOperationException();
	}

    @Override
	public List<Discipline> getDisciplinesByDay(Date day) {
		// TODO - implement fi.muni.pv168.DisciplineManagerImpl.getDisciplineByDay
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateDiscipline(Discipline discipline) {
		// TODO - implement fi.muni.pv168.DisciplineManagerImpl.updateDiscipline
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteDiscipline(Discipline discipline) {
		// TODO - implement fi.muni.pv168.DisciplineManagerImpl.deleteDiscipline
		throw new UnsupportedOperationException();
	}

}