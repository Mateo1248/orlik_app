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
        try {
            userRepository.save(user);
        }
        catch (UserNotFoundException e) { /*
            1) co łapać, własne wyjątki czy wbudowane (custom)
            2) czy łapać to w ten sposób że try i kilka catchy tutaj w serwisie
            3) czy dobrze jest stworzony własny wyjątek
            4) czy robić to na zasadzie @ControllerAdvice w @ResponseStatus

            */

        }
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
