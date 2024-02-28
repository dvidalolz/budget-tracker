package input_types.internal.input_subtype;

import java.util.List;


/**
 * Facilitates `InputSubType` entity persistence operations.
 */
public interface InputSubtypeRepository {
    
    // Save inputsubType to the database. This method can handle both creating a new inputsubType
    // and updating an existing inputsubType
    InputSubType save(InputSubType inputSubType);

    // Fetch all inputs subType associated with user account by id
    List<InputSubType> findByTypeId(Long typeId);

    // Delete an input subType using unique subType id
    void deleteById(Long subTypeId);   
}
