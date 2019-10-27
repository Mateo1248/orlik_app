package com.orlikteam.orlikbackend.user;

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
        if(!emailValidator(user.userLogin))
            throw new UserBadMailException();
        Optional<User> ifExistUser = userRepository.findById(user.userLogin);
        if(ifExistUser.isPresent())
            throw new UserAlreadyInDBException();
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

    public boolean emailValidator(String mail) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
        Matcher matcher = pattern.matcher(mail);
        if (matcher.find() && matcher.group().equals(mail)) {
            return true;
        }
        else
            return false;
    }

}
