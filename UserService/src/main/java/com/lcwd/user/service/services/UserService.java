package com.lcwd.user.service.services;

import com.lcwd.user.service.DTO.UserDTO;
import com.lcwd.user.service.entities.User;

import java.util.List;


public interface UserService {
    // user operations

    // save user
    User saveUser(User user) ;

    // get user by id
    User getUser(String userId);

    // get all users
    List<User> getAllUsers();

    User updateUser(String userId, UserDTO userDTO);

    // delete user
    void deleteUser(String userId);


}
