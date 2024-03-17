package io.spring.training.corespring.personalbudgettracker.user_input.internal;

import java.util.List;

import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_subtype.InputSubType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputType;

/**
 * Manages crud operations for input types and subtypes tied to specific users.
 * 
 * (When refactoring : it may be useful to break this interface into a service
 * for crud and a service for fetch)
 */
public interface InputTypeService {

    /**
     * @param userId        used to associate inputtype to user
     * @param inputTypeName (different users may have same types)
     */
    InputType createInputTypeForUser(Long userId, String inputTypeName);

    /**
     * @param typeId      unique id for types with same names across users
     * @param newTypeName update name
     */
    InputType updateInputType(Long typeId, String newTypeName);

    void deleteInputTypeById(Long typeId);

    /**
     * @param userId           used to associate inputsubtype to user
     * @param inputSubTypeName (different users may have same subtypes)
     */
    InputSubType createInputSubType(Long typeId, String inputSubTypeName);

    /**
     * @param subTypeId      unique id for subttypesF with same names across users
     * @param newSubTypeName update name
     */
    InputSubType updateInputSubType(Long subTypeId, String newSubTypeName);

    void deleteInputSubType(Long subTypeId);

    /**
     * Fetch all InputTypes associated with a specific User account
     * 
     * @param userId inputtypes are connected to users
     */
    List<InputType> findAllInputTypesByUserId(Long userId);

    /**
     * Fetch all InputSubTypes associated with a specific type (and user)
     * @param typeId inputSubTypes are connected to inputTypes
     */
    List<InputSubType> findAllInputSubTypesByTypeId(Long typeId);
}
