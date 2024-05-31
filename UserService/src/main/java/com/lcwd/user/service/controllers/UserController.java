package com.lcwd.user.service.controllers;

import com.lcwd.user.service.DTO.UserDTO;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    //    craate user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User user1 = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    //    single user get
    int retryCount = 1;

    @GetMapping(value = "/{userId}")
//    @CircuitBreaker(name = "" +"" , fallbackMethod = "ratingHotelFallback")
//    @Retry(name="ratingHotelService" , fallbackMethod = "ratingHotelFallback")
    @RateLimiter(name = "userRatingLimiter", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId) {
        logger.info("retryCount : {}" + retryCount);
        retryCount++;
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    // rating fallback method for circuit breaker
    public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex) {
        logger.info("fallback is expected because service is down : " + ex.getMessage());
        User user = User.builder().
                email("dummy@gmail.com").
                name("dummy").
                about("This user is created dummy beacuse some services is down").
                userId("123456").
                build();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //    all users get
    @PreAuthorize("hasAuthority('normal')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") String userId, @RequestBody UserDTO userDTO) {
        User updatedUser = userService.updateUser(userId, userDTO);
        return ResponseEntity.ok(updatedUser);
    }
}
