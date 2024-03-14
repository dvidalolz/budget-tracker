package io.spring.training.corespring.personalbudgettracker.input_types.internal;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import io.spring.training.corespring.personalbudgettracker.exceptions.InputTypeExceptions;
import io.spring.training.corespring.personalbudgettracker.input_types.InputTypeService;
import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_subtype.InputSubType;
import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_subtype.InputSubTypeRepository;
import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_type.InputType;
import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_type.InputTypeRepository;
import io.spring.training.corespring.personalbudgettracker.users.internal.user.User;

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

    @Transactional
    @Override
    public InputType createInputTypeForUser(Long userId, String inputTypeName) {
        try {
            User user = User.userWithOnlyId(userId);
            InputType inputType = new InputType(inputTypeName, user);
            inputType = inputTypeRepository.save(inputType);
            return inputType;
        } catch (RuntimeException e) {
            throw new InputTypeExceptions.InputTypeCreationException("Failed to create input type " + inputTypeName, e);
        }

    }

    @Transactional
    @Override
    public InputType updateInputType(Long typeId, String newTypeName) {
        return inputTypeRepository.findById(typeId).map(existingInputType -> {
            existingInputType.setName(newTypeName);
            return inputTypeRepository.save(existingInputType);
        }).orElseThrow(() -> new InputTypeExceptions.InputTypeNotFoundException("InputType with ID " + typeId + " not found"));
    }

    @Transactional
    @Override
    public void deleteInputTypeById(Long typeId) {
        inputTypeRepository.findById(typeId).ifPresentOrElse(inputType -> {
            inputTypeRepository.deleteById(typeId);
        }, () -> {
            throw new InputTypeExceptions.InputTypeDeletionException("InputType with ID " + typeId + " not found");
        });
    }

    @Transactional
    @Override
    public InputSubType createInputSubType(Long typeId, String inputSubTypeName) {
        try {
            InputType inputType = InputType.inputTypeWithOnlyId(typeId);
            InputSubType inputSubType = new InputSubType(inputSubTypeName, inputType);
            inputSubType = inputSubTypeRepository.save(inputSubType);
            return inputSubType;
        } catch (RuntimeException e) {
            throw new InputTypeExceptions.InputSubTypeCreationException("Failed to create input subtype " + inputSubTypeName, e);
        }
    }

    @Transactional
    @Override
    public InputSubType updateInputSubType(Long subTypeId, String newSubTypeName) {
        return inputSubTypeRepository.findById(subTypeId).map(existingInputSubType -> {
            existingInputSubType.setName(newSubTypeName);
            return inputSubTypeRepository.save(existingInputSubType);
        }).orElseThrow(() -> new InputTypeExceptions.InputSubTypeNotFoundException("InputType with ID " + subTypeId + " not found"));
    }

    @Transactional
    @Override
    public void deleteInputSubType(Long subTypeId) {
        inputSubTypeRepository.findById(subTypeId).ifPresentOrElse(inputType -> {
            inputSubTypeRepository.deleteById(subTypeId);
        }, () -> {
            throw new InputTypeExceptions.InputSubTypeDeletionException("InputSubType with ID " + subTypeId + " not found");
        });
    }

    /**
     * Important Note: this method returns a list of types which has no set of
     * subtypes and whose user has only id
     * No error handling, it just returns an empty list if no input found
     */
    @Override
    public List<InputType> findAllInputTypesByUserId(Long userId) {
        return inputTypeRepository.findAllByUserId(userId);
    }

    /**
     * Important note: Returns a list of inputsubtypes' which have a type object
     * with no user object or set of subtype objects
     * No error handling, it just returns an empty list if no input found
     */
    @Override
    public List<InputSubType> findAllInputSubTypesByTypeId(Long typeId) {
        return inputSubTypeRepository.findAllByTypeId(typeId);
    }

}
