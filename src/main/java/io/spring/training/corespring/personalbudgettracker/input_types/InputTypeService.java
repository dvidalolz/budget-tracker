package io.spring.training.corespring.personalbudgettracker.input_types;

import java.util.List;

import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_subtype.InputSubType;
import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_type.InputType;

/**
 * Manages input types and subtypes, including creation and removal, tied to
 * specific users.
 * 
 * (When refactoring : it may be useful to break this interface into a service for crud and a service for fetch)
 */
public interface InputTypeService {

    InputType createInputTypeForUser(Long userId, String inputTypeName);

    InputType updateInputType(Long typeId, String newTypeName);

    void deleteInputTypeById(Long typeId);

    InputSubType createInputSubType(Long typeId, String inputSubTypeName);

    InputSubType updateInputSubType(Long subTypeId, String newSubTypeName);

    void deleteInputSubType(Long subTypeId);

    // Fetch all InputTypes associated with a specific User account
    List<InputType> findAllInputTypesByUserId(Long userId);

    // Fetch all InputSubTypes associated with a specific InputType
    List<InputSubType> findAllInputSubTypesByTypeId(Long typeId);
}
