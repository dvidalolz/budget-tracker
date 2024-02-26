package input_types.internal;

import java.util.List;

import input_types.InputTypeService;
import input_types.internal.input_subtype.InputSubType;
import input_types.internal.input_type.InputType;
import input_types.internal.input_type.InputTypeRepository;
import users.UserService;


/**
 * Implementation for : Manages input types and subtypes, including creation and removal, tied to specific users.
 * 
 * This object is the application-layer service for managing and fetching inputs
 */
public class InputTypeServiceImpl implements InputTypeService {

    private final UserService userService;

    private final InputTypeRepository inputTypeRepository;

    private final InputTypeRepository inputSubTypeRepository;

    
    /**
     * Manages input-type and subtype related logic
     * @param userService service for accessing users 
     * @param inputTypeRepository data access for all input types 
     * @param inputSubTypeRepository data access for all input subtypes
     * 
     * Input Types are associated with User through User and InputType implementation
     * InputSubTypes are associated with a User through InputType
     */
    public InputTypeServiceImpl(UserService userService, InputTypeRepository inputTypeRepository, InputTypeRepository inputSubTypeRepository) {
        this.userService = userService;
        this.inputTypeRepository = inputTypeRepository;
        this.inputSubTypeRepository = inputSubTypeRepository;
    }

    // Inputtypes and subtype adds/updates/deletes must be reflected in User in userrepo as well
    // because If a user is retrieved for the purpose of referring to its inputtypes, subtypes, it must have been updated properly
    // to avoid mismatch
    @Override
    public InputType createInputType(InputType inputType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createInputType'");
    }

    @Override
    public InputType updateInputType(Long typeId, String newTypeName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateInputType'");
    }

    @Override
    public void deleteInputType(Long typeId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteInputType'");
    }

    @Override
    public InputSubType createInputSubType(InputSubType inputSubType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createInputSubType'");
    }

    @Override
    public InputSubType updateInputSubType(Long subTypeId, String newSubTypeName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateInputSubType'");
    }

    @Override
    public void deleteInputSubType(Long subTypeId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteInputSubType'");
    }

    @Override
    public List<InputType> findAllInputTypesByUserId(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllInputTypesByUserId'");
    }

    @Override
    public List<InputSubType> findAllInputSubTypesByUserId(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllInputSubTypesByUserId'");
    }

    @Override
    public List<InputSubType> findAllInputSubTypesByTypeId(Long typeId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllInputSubTypesByTypeId'");
    }
    
}
