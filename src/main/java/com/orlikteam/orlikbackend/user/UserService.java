package com.orlikteam.orlikbackend.user;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void removeUser(String userLogin) {
        this.userRepository.deleteById(userLogin);
    }

    @Transactional
    public User getUser(String userLogin) {
        try {
            return userRepository.getOne(userLogin);
        }
        catch (UserNotFoundException e) {
            throw new UserNotFoundException("User", "login", userLogin);
        }
    }

}
