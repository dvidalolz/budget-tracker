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


import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/users")
public class UserController {

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
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
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
    @PatchMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UserDetails userDetails) {
        User updatedUser = userService.updateUser(userId, userDetails);

        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Delete the user
     * 
     * @implSpec inputvalidation occuring at service level, userdeletion exception
     *           occurs if unable to delete
     * @return an empty response indicated success of deletion
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * find the user by id
     * 
     * @implNote userService.findUserById() handles optional
     * @implSpec if user not found, catch and return exception status, other
     *           exceptions have other status
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> findUserById(@PathVariable Long userId) {
        User user = userService.findUserById(userId);
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

    @ExceptionHandler(UserExceptions.UserDeletionException.class)
    public ResponseEntity<Object> handleUserDeletionException(UserExceptions.UserDeletionException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
