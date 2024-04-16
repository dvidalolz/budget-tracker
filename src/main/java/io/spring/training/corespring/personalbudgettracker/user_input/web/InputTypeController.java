package io.spring.training.corespring.personalbudgettracker.user_input.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.spring.training.corespring.personalbudgettracker.user_input.internal.InputTypeService;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_subtype.InputSubType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputType;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/users/{userId}")
public class InputTypeController {
    
    private InputTypeService inputTypeService;

    public InputTypeController(InputTypeService inputTypeService) {
        this.inputTypeService = inputTypeService;
    }

        /**
         * Add Input Type for a specific user
         * @return response entity which holds the created inputtype as well as its location
         */
        @PostMapping("/input-types")
        public ResponseEntity<InputType> addInputTypeForUser(@PathVariable Long userId, @RequestBody String inputTypeName) {
            InputType inputType = inputTypeService.addInputTypeForUser(userId, inputTypeName);
            return entityWithLocation(inputType, inputType.getId());
        }
    
        // Update Input Type by typeId. Note: This breaks the base path convention as typeId is not user-specific.
        @PutMapping("/input-types/{typeId}")
        public ResponseEntity<InputType> updateInputType(@PathVariable Long typeId, @RequestBody String newTypeName) {
            InputType updatedInputType = inputTypeService.updateInputType(typeId, newTypeName);
            return ResponseEntity.ok(updatedInputType);
        }
    
        /**
        * Delete Input Type by TypeId. Note: This also breaks the base path convention.
        * @return an empty response indicated success of deletion
        */
        @DeleteMapping("/input-types/{typeId}")
        public ResponseEntity<Void> deleteInputTypeById(@PathVariable Long typeId) {
            inputTypeService.deleteInputTypeById(typeId);
            return ResponseEntity.noContent().build();
        }
    
        /**
         * Add Input SubType for a specific input subtype
         * @return response entity which holds the created input subtype as well as its location
         */
        // 
        @PostMapping("/input-types/{typeId}/subtypes")
        public ResponseEntity<InputSubType> addInputSubType(@PathVariable Long userId, @PathVariable Long typeId, @RequestBody String inputSubTypeName) {
            InputSubType inputSubType = inputTypeService.addInputSubType(typeId, inputSubTypeName);
            return entityWithLocation(inputSubType, inputSubType.getId());
        }
    
        // Update Input SubType by subTypeId. Note: This also breaks the base path convention.
        @PutMapping("/input-subtypes/{subTypeId}")
        public ResponseEntity<InputSubType> updateInputSubType(@PathVariable Long subTypeId, @RequestBody String newSubTypeName) {
            InputSubType updatedInputSubType = inputTypeService.updateInputSubType(subTypeId, newSubTypeName);
            return ResponseEntity.ok(updatedInputSubType);
        }
    
        /**
        * Delete Input SubType by subTypeId. Note: This also breaks the base path convention.
        * @return an empty response indicated success of deletion
        */
        @DeleteMapping("/input-subtypes/{subTypeId}")
        public ResponseEntity<Void> deleteInputSubType(@PathVariable Long subTypeId) {
            inputTypeService.deleteInputSubType(subTypeId);
            return ResponseEntity.noContent().build();
        }
    
        // Get All Input Types by User ID
        @GetMapping("/input-types")
        public ResponseEntity<List<InputType>> getAllInputTypesByUserId(@PathVariable Long userId) {
            List<InputType> inputTypes = inputTypeService.findAllInputTypesByUserId(userId);
            return ResponseEntity.ok(inputTypes);
        }
    
        // Get All Input SubTypes by Type ID. Note: This maintains the base path but may require additional context to ensure it's user-specific.
        @GetMapping("/input-types/{typeId}/subtypes")
        public ResponseEntity<List<InputSubType>> getAllInputSubTypesByTypeId(@PathVariable Long typeId) {
            List<InputSubType> inputSubTypes = inputTypeService.findAllInputSubTypesByTypeId(typeId);
            return ResponseEntity.ok(inputSubTypes);
        }


        private <T> ResponseEntity<T> entityWithLocation(T resource, Object resourceId) {
            // Determines URL of child resource based on the full URL of the given
            // request, appending the path info with the given resource Identifier
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequestUri()
                    .path("/{resourceId}")
                    .buildAndExpand(resourceId)
                    .toUri();
        
            // Return an HttpEntity object with the resource - it will be used to build the
            // HttpServletResponse. The body of the response is set to the resource object.
            return ResponseEntity.created(location).body(resource);
        }

        // Exception Handlers
        @ExceptionHandler(InputTypeExceptions.InputTypeCreationException.class)
        public ResponseEntity<Object> handleInputTypeNotCreatedException(InputTypeExceptions.InputTypeCreationException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        

        
}
