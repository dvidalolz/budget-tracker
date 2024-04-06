package io.spring.training.corespring.personalbudgettracker.user_input.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.spring.training.corespring.personalbudgettracker.user_input.internal.UserService;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.User;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.UserDetails;

import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class UserController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Add a new user
     */
    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody UserDetails userDetails) { 
        User addedUser = userService.addUser(userDetails);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(addedUser.getId())
                    .toUri();

        return ResponseEntity.created(location).body(addedUser);

    }
}
