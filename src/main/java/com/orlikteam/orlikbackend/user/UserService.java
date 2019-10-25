package com.orlikteam.orlikbackend.user;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void removeUser(String userLogin) {
        Optional<User> tmpUser = userRepository.findById(userLogin);
        if (tmpUser.isEmpty())
            throw new UserNotFoundException();
        userRepository.deleteById(userLogin);
    }

    @Transactional
    public User getUser(String userLogin) {
        //return userRepository.findById(userLogin).orElseThrow(UserNotFoundException::new);
        Optional<User> tmpUser = userRepository.findById(userLogin);
        if (tmpUser.isEmpty())
            throw new UserNotFoundException();
        return tmpUser.get();
    }

}
