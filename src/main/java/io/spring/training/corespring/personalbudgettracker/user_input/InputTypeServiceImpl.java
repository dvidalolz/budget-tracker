package io.spring.training.corespring.personalbudgettracker.user_input;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import io.spring.training.corespring.personalbudgettracker.user_input.internal.InputTypeService;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions.InputTypeDeletionException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions.InputTypeCreationException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions.InputTypeNotFoundException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions.InputTypeRetrievalException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions.InputSubTypeRetrievalException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions.InputSubTypeCreationException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions.InputSubTypeDeletionException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions.InputSubTypeNotFoundException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_subtype.InputSubType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_subtype.InputSubTypeRepository;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputTypeRepository;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.User;

/**
 * Implementation for : Manages input types and subtypes, including creation and
 * removal, tied to specific users.
 * 
 * This object is the application-layer service for managing and fetching inputs
 */
@Service
public class InputTypeServiceImpl implements InputTypeService {

    private final InputTypeRepository inputTypeRepository;

    private final InputSubTypeRepository inputSubTypeRepository;

    /**
     * Manages input-type and subtype related logic
     * 
     * @param inputTypeRepository    data access for all input types
     * @param inputSubTypeRepository data access for all input subtypes
     * 
     *                               Input Types are associated with User through
     *                               User object id.
     *                               InputSubTypes are associated with a User
     *                               through InputType
     */
    public InputTypeServiceImpl(InputTypeRepository inputTypeRepository,
            InputSubTypeRepository inputSubTypeRepository) {
        this.inputTypeRepository = inputTypeRepository;
        this.inputSubTypeRepository = inputSubTypeRepository;
    }

    /**
     * Create lightweight inputType with lightweight user and save 
     * Exception handling for generic data access issues like connection problems
     */
    @Override
    public InputType addInputTypeForUser(Long userId, String inputTypeName) {
        try {
            User user = User.userWithOnlyId(userId);
            InputType inputType = new InputType(inputTypeName, user);
            inputType = inputTypeRepository.save(inputType);
            return inputType;
        } catch (DataAccessException e) {
            throw new InputTypeCreationException("Failed to create input type " + inputTypeName, e);
        }

    }

    /**
     * Updates type if found by Id by mapping lambda expression
     * If no type found by typeId (empty optional returned), throws exception
     */
    @Override
    public InputType updateInputType(Long typeId, String newTypeName) {
        return inputTypeRepository.findById(typeId).map(existingInputType -> {
            existingInputType.setName(newTypeName);
            return inputTypeRepository.save(existingInputType);
        }).orElseThrow(() -> new InputTypeNotFoundException("InputType with ID " + typeId + " not found"));
    }

    /**
     * Deletes input types by Id if it exists
     * If it doesn't exist, no rows affected in repository level, thrown and caught and thrown specified exceptions
     */
    @Override
    public void deleteInputTypeById(Long typeId) {
        try {
            inputTypeRepository.deleteById(typeId);
        } catch (InputTypeNotFoundException ex) {
            throw new InputTypeDeletionException("Failed to delete input type with ID: " + typeId,ex);
        }
       
    }

    /**
     * Create lightweight input SubType with lightweight type and save 
     * Exception handling for generic data access issues like connection problems
     */
    @Override
    public InputSubType addInputSubType(Long typeId, String inputSubTypeName) {
        try {
            InputType inputType = InputType.inputTypeWithOnlyId(typeId);
            InputSubType inputSubType = new InputSubType(inputSubTypeName, inputType);
            inputSubType = inputSubTypeRepository.save(inputSubType);
            return inputSubType;
        } catch (DataAccessException e) {
            throw new InputSubTypeCreationException("Failed to create input subtype " + inputSubTypeName, e);
        }
    }

    /**
     * Updates subtype if found by Id by mapping lambda expression
     * If no type found by typeId (empty optional returned), throws exception
     */
    @Override
    public InputSubType updateInputSubType(Long subTypeId, String newSubTypeName) {
        return inputSubTypeRepository.findById(subTypeId).map(existingInputSubType -> {
            existingInputSubType.setName(newSubTypeName);
            return inputSubTypeRepository.save(existingInputSubType);
        }).orElseThrow(() -> new InputSubTypeNotFoundException("InputType with ID " + subTypeId + " not found"));
    }

    /**
     * Deletes input subtypes by Id if it exists
     * If it doesn't exist, no rows affected in repository level, thrown and caught and thrown specified exceptions
     */
    @Override
    public void deleteInputSubType(Long subTypeId) {
        try {
            inputSubTypeRepository.deleteById(subTypeId);
        } catch (InputSubTypeNotFoundException ex) {
            throw new InputSubTypeDeletionException("Failed to delete input type with ID: " + subTypeId, ex);
        }
    }

    /**
     * Important Note: this method returns a list of types which has no set of
     * subtypes and whose user has only id
     * Exception : if none found, empty list triggers exception, if data access
     * error occurs, triggers exception
     */
    @Override
    public List<InputType> findAllInputTypesByUserId(Long userId) {
        try {
            List<InputType> inputTypes = inputTypeRepository.findAllByUserId(userId);
            if (inputTypes.isEmpty()) {
                throw new InputTypeRetrievalException("No input types found for user with ID: " + userId);
            }
            return inputTypes;

        } catch (DataAccessException e) {
            throw new InputTypeRetrievalException("Error fetching input types for user with ID: " + userId, e);
        }
    }

    /**
     * Important note: Returns a list of inputsubtypes' which have a type object
     * with no user object or set of subtype objects
     * Exception : if none found, empty list triggers exception, if data access
     * error occurs, triggers exception
     */
    @Override
    public List<InputSubType> findAllInputSubTypesByTypeId(Long typeId) {
        try {
            List<InputSubType> inputSubTypes = inputSubTypeRepository.findAllByTypeId(typeId);
            if (inputSubTypes.isEmpty()) {
                throw new InputSubTypeRetrievalException("No input subtypes found for type with ID: " + typeId);
            }
            return inputSubTypes;

        } catch (DataAccessException e) {
            throw new InputSubTypeRetrievalException("Error fetching input subtypes for type with ID: " + typeId, e);
        }
    }

}
