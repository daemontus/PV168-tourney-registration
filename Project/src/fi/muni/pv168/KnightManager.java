package fi.muni.pv168;

import fi.muni.pv168.utils.ServiceFailureException;

import java.util.List;

/**
 * <p>Manager responsible for storage of Knights.</p>
 */
public interface KnightManager {

    /**
     * Create new knight.
     * @param knight Knight to create.
     */
	void createKnight(Knight knight) throws ServiceFailureException;

    /**
     * Get knight by id.
     * @param id Id of knight.
     * @return Knight with specified id, or null if not found.
     */
	Knight getKnightById(Long id) throws ServiceFailureException;

    /**
     * Get all knights in database.
     * @return All available knights
     */
	List<Knight> findAllKnights() throws ServiceFailureException;


    /**
     * Update knight with new data.
     * @param knight Knight with new data.
     */
	void updateKnight(Knight knight) throws ServiceFailureException;

    /**
     * Delete knight.
     * @param knight Knight you want to delete from database.
     */
	void deleteKnight(Knight knight) throws ServiceFailureException;

}