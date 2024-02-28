package input_types.internal.input_subtype;

import java.util.List;
import java.util.Optional;


/**
 * Facilitates `InputSubType` entity persistence operations.
 */
public interface InputSubtypeRepository {
    
    // Save inputsubType to the database. This method can handle both creating a new inputsubType
    // and updating an existing inputsubType
    InputSubType save(InputSubType inputSubType);

    // Fetch all inputs subType associated with user account by id
    List<InputSubType> findAllByTypeId(Long typeId);

    Optional<InputSubType> findBySubTypeId(Long subTypeId);

    void deleteById(Long subTypeId);   
}
