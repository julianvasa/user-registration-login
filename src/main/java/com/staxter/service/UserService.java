package com.staxter.service;

import com.staxter.exception.UserAlreadyExistsException;
import com.staxter.model.User;

/**
 * User service handles all operations with users (in this case, just create new user)
 *
 * @author Julian Vasa
 */
public interface UserService {
    User createUser(User user) throws UserAlreadyExistsException;
}
