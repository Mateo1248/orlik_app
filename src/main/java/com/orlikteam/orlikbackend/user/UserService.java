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
        if(tmpUser.isPresent())
            userRepository.deleteById(userLogin);
        else
            throw new UserNotFoundException();
    }

    @Transactional
    public User getUser(String userLogin) {
        return userRepository.findById(userLogin).get();
    }

}
