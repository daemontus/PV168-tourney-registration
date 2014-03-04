package fi.muni.pv168;

import java.util.List;

public interface KnightManager {

	/**
	 * 
	 * @param knight
	 */
	void createKnight(Knight knight);

	/**
	 * 
	 * @param id
	 */
	Knight getKnightById(Long id);

	List<Knight> findAllKnights();

	/**
	 * 
	 * @param knight
	 */
	void updateKnight(Knight knight);

	/**
	 * 
	 * @param knight
	 */
	void deleteKnight(Knight knight);

}