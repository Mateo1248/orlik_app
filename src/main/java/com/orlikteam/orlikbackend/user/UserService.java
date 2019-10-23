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
        User tmpUser = userRepository.getOne(userLogin);
        if(tmpUser==null) {
            throw new UserNotFoundException();
        }
        return tmpUser;
    }

}



//obługa w User i w UserResource adnotacjami valid, not blank