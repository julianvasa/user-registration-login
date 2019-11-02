package com.staxter.repository;

import com.staxter.exception.UserAlreadyExistsException;
import com.staxter.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Local user storage
 *
 * @author Julian Vasa
 */
public interface UserRepository{
    User createUser( User user ) throws UserAlreadyExistsException;
    User findByUsername (String username) throws UsernameNotFoundException;
}
