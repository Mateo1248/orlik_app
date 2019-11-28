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


    /**
     * method is used to add a new user to app
     * @param userDto is a object made from given by user login and password (which is encoded)
     * @throws @UserAlreadyExistsException
     * @return user login as a response of properly added user given from repository
     */
    public UserLoginDto addUser(UserDto userDto) {
        Optional<User> maybeUser = userRepository.findById(userDto.getUserLogin());
        if(maybeUser.isPresent())
            throw new UserAlreadyExistsException();
        User user = getBuiltUser(userDto);
        return getUserResponseDtoOf(userRepository.save(user).getUserLogin());
    }


    /**
     * method is used to remove an existing user from app
     * @param userLogin is a login of user who we are eager to delete from app
     * @throws @UserNotFoundException
     */
    public void removeUser(String userLogin) {
        Optional<User> user = userRepository.findById(userLogin);
        if (user.isEmpty())
            throw new UserNotFoundException();
        userRepository.deleteById(userLogin);
    }

    /**
     * method is used to get an existing user from app
     * @param userLogin is a login of user who we are eager to get from app
     * @throws @UserNotFoundException
     * @return user login as response of really existing user given from repository
     */
    public String getUser(String userLogin) {
        Optional<User> user = userRepository.findById(userLogin);
        if (user.isEmpty())
            throw new UserNotFoundException();
        return user.get().getUserLogin();
    }

    /**
     * method is used to update an existing user (his password) in app
     * @param userDto is an object made from given by user login and new password (which is encoded)
     * @throws @UserNotFoundException
     */
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
