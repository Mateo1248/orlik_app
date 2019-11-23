package com.orlikteam.orlikbackend.user;

import com.orlikteam.orlikbackend.user.exception.UserAlreadyExistsException;
import com.orlikteam.orlikbackend.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserLoginDto addUser(UserDto userDto) {
        Optional<User> maybeUser = userRepository.findById(userDto.getUserLogin());
        if(maybeUser.isPresent())
            throw new UserAlreadyExistsException();
        User user = getBuiltUser(userDto);
        return getUserResponseDtoOf(userRepository.save(user).getUserLogin());
    }

    public void removeUser(String userLogin) {
        Optional<User> user = userRepository.findById(userLogin);
        if (user.isEmpty())
            throw new UserNotFoundException();
        userRepository.deleteById(userLogin);
    }

    public String getUser(String userLogin) {
        Optional<User> user = userRepository.findById(userLogin);
        if (user.isEmpty())
            throw new UserNotFoundException();
        return user.get().getUserLogin();
    }

    public void updateUser(UserDto userDto) {
        Optional<User> user = userRepository.findById(userDto.getUserLogin());
        if (user.isEmpty())
            throw new UserNotFoundException();
        User userToModify = user.get();
        userToModify.setUserPassword(userDto.getUserPassword());
        userRepository.save(userToModify);
    }


    private static UserLoginDto getUserResponseDtoOf(String userLogin){
        return UserLoginDto
                .builder()
                .userLogin(userLogin)
                .build();
    }

    private User getBuiltUser(UserDto userDto) {
        return User
                .builder()
                .userLogin(userDto.getUserLogin())
                .userPassword(userDto.getUserPassword())
                .build();
    }


}
