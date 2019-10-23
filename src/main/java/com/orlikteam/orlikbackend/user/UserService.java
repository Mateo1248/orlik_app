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
        //spr czy delete to załatwia za nas
        this.userRepository.deleteById(userLogin);
    }

    @Transactional
    public User getUser(String userLogin) {
        Optional<User> tmpUser = userRepository.findById(userLogin);
        if(!tmpUser.isPresent()) {
            throw new UserNotFoundException();
        }
        return tmpUser.get();
    }

}

//obługa w User i w UserResource adnotacjami valid, not blank