package com.orlikteam.orlikbackend.user;

import com.orlikteam.orlikbackend.user.exception.UserAlreadyExistsException;
import com.orlikteam.orlikbackend.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User addUser(User user) {
        Optional<User> maybeUser = userRepository.findById(user.getUserLogin());
        if(maybeUser.isPresent())
            throw new UserAlreadyExistsException();
        return userRepository.save(user);
    }

    @Transactional
    public void removeUser(String userLogin) {
        Optional<User> user = userRepository.findById(userLogin);
        if (user.isEmpty())
            throw new UserNotFoundException();
        userRepository.deleteById(userLogin);
    }

    @Transactional
    public User getUser(String userLogin) {
        Optional<User> user = userRepository.findById(userLogin);
        if (user.isEmpty())
            throw new UserNotFoundException();
        return user.get();
    }


}
