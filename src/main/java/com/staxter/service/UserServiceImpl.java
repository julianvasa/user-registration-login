package com.staxter.service;

import com.staxter.exception.UserAlreadyExistsException;
import com.staxter.model.User;
import com.staxter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User service handles all operations with users (in this case, just create new user)
 *
 * @author Julian Vasa
 */
@Service
public class UserServiceImpl implements UserService {
    /* Autowire userRepository, will forward user creation*/
    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) throws UserAlreadyExistsException {
        /* Forward user creation to userRepository, or throw UserAlreadyExistsException, will be handled in UserController */
        return userRepository.createUser(user);
    }
}
