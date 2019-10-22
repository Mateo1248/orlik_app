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
        this.userRepository.save(user);
        //logika here, obsługa wyjątków
    }

    @Transactional
    public void removeUser(String userLogin) {
        this.userRepository.deleteById(userLogin);
    }

}
