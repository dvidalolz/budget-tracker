package io.spring.training.corespring.personalbudgettracker.user_input.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.spring.training.corespring.personalbudgettracker.user_input.internal.UserService;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.UserExceptions;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.User;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.UserDetails;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Add a new user
     * 
     * @implNote userDetails to be expected in the requestbody of requester
     * @return responseEntity contains '201 created' status code, location header
     *         point to new user uri, created user object
     */
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserDetails userDetails) {
        User addedUser = userService.addUser(userDetails);

        // Build the URI for the newly created user, appending the user's ID to the path
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(addedUser);
    }

    /**
     * Update the user
     * 
     * @implNote userService.updateUser() is already designed to handle partial
     *           updates
     * @implSpec input validation occuring at service level, userNotfoundException
     *           thrown if id sucks
     */
    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDetails userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);

        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Delete the user
     * 
     * @implSpec inputvalidation occuring at service level, userdeletion exception
     *           occurs if unable to delete
     * @return an empty responseentity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * find the user by id
     * 
     * @implNote userService.findUserById() handles optional
     * @implSpec if user not found, catch and return exception status, other
     *           exceptions have other status
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);

    }

    /**
     * Find the user by name
     */
    @GetMapping("/by-username/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userService.findUserByUserName(username);
        return ResponseEntity.ok(user);

    }

    // Exception handlers for different service level exceptions we may encounter
    @ExceptionHandler(UserExceptions.UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFoundException(UserExceptions.UserNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(UserExceptions.UserCreationException.class)
    public ResponseEntity<Object> handleUserNotCreatedException(UserExceptions.UserCreationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        // Consider logging the exception here
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
