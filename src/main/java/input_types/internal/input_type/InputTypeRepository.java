package input_types.internal.input_type;

import java.util.List;
import java.util.Optional;


/**
 * Facilitates `InputType` entity persistence operations.
 */
public interface InputTypeRepository {

    // Save input type to the database. This method can handle both creating a new input type
    // and updating an existing input type
    InputType save(InputType inputType);

    // Fetch all input types associated with user account by id
    List<InputType> findAllByUserId(Long userId);

    Optional<InputType> findById(Long typeId);

    void deleteById(Long typeId);


}
