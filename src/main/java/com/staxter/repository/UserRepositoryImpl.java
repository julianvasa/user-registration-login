package com.staxter.repository;

import com.staxter.exception.UserAlreadyExistsException;
import com.staxter.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Local user storage
 *
 * @author Julian Vasa
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    /* This dependency is used to encrypt the plain text password */
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /* Thread-safe data structure - ConcurrentHashMap - to hold the list of registered users */
    private final Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public User createUser(User user) throws UserAlreadyExistsException {
        /* If username does not exists in the ConcurrentHashMap then proceed */
        if(!users.containsKey(user.getUserName())){
            /* Encrypt plain text password and set it to the HashPassword field */
            user.setHashedPassword(bCryptPasswordEncoder.encode(user.getPlainTextPassword()));
            /* Set id based on the hashCode of the user data provided in the request */
            user.setId(user.hashCode());
            /* Store in the ConcurrentHashMap the updated user */
            users.put(user.getUserName(), user);
            return user;
        }
        /* If username already exists throw UserAlreadyExistsException, will be handled in the UserController */
        throw new UserAlreadyExistsException();
    }

    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        /* If username exists in the ConcurrentHashMap then proceed */
        if(users.containsKey(username)){
            return users.get(username);
        }
        /* If username does not exist throw UserDoesNotExistException, will be handled in the UserController */
        throw new UsernameNotFoundException("The user with the provided username not found");
    }
}
