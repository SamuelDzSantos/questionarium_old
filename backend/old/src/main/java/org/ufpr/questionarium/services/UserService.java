package org.ufpr.questionarium.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ufpr.questionarium.dtos.RegisterForm;
import org.ufpr.questionarium.dtos.UserData;
import org.ufpr.questionarium.dtos.UserPatch;
import org.ufpr.questionarium.exceptions.exceptions.UserNotFoundException;
import org.ufpr.questionarium.mapper.UserMapper;
import org.ufpr.questionarium.models.SecurityUser;
import org.ufpr.questionarium.models.User;
import org.ufpr.questionarium.repositories.UserRepository;
import org.ufpr.questionarium.types.Role;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private final PatchUtils patcher;

    UserService(UserRepository userRepository, PasswordEncoder encoder, UserMapper userMapper, PatchUtils patcher) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userMapper = userMapper;
        this.patcher = patcher;
    }

    public UserData addUser(RegisterForm registerForm) {

        User user = new User(null, registerForm.getName(), registerForm.getEmail(), registerForm.getPassword(),
                LocalDate.now(), List.of(Role.ROLE_TEACHER));

        user.setPassword(encoder.encode(user.getPassword()));

        user = this.userRepository.save(user);

        return userMapper.userToUserData(user);

    }

    public void addUser(String name, String email, String password) {

        User user = new User(null, name, email, password, LocalDate.now(), List.of(Role.ROLE_TEACHER));

        user.setPassword(encoder.encode(user.getPassword()));

        this.userRepository.save(user).getName();

    }

    public UserData getCurrentUser(Authentication authentication) {

        String email = authentication.getName();

        return userMapper.userToUserData(this.findUser(email));

    }

    public UserData getUserById(Authentication authentication, Long id) {

        User user = this.findUser(id);

        checkIsAllowed(authentication, user);

        UserData userData = userMapper.userToUserData(user);

        return userData;

    }

    public UserData getUserByEmail(Authentication authentication, String email) {

        User user = this.findUser(email);

        checkIsAllowed(authentication, user);

        UserData userData = userMapper.userToUserData(user);

        return userData;

    }

    public SecurityUser updateUserFields(Authentication authentication, UserPatch userPatch, Long id) {

        User existingUser = this.findUser(id);

        checkIsAllowed(authentication, existingUser);

        if (userPatch.getPassword() != null && userPatch.getNewPassword() != null
                && userPatch.getConfirmPassword() != null) {
            existingUser = this.updateUserPassword(userPatch, existingUser);
        }

        User incompleteUser = userMapper.userPatchToUser(userPatch);

        patcher.userPatch(existingUser, incompleteUser);

        User user = this.userRepository.save(existingUser);

        return new SecurityUser(user);
    }

    public void deleteUser(Authentication authentication, Long id) {
        User user = this.findUser(id);
        this.userRepository.delete(user);
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    private void checkIsAllowed(Authentication authentication, User user) {
        if (!authentication.getName().equals(user.getEmail()))
            throw new RuntimeException("");
    }

    private User updateUserPassword(UserPatch userPatch, User existingUser) {

        if (!encoder.matches(userPatch.getPassword(), existingUser.getPassword()))
            throw new RuntimeException();
        if (!userPatch.getConfirmPassword().equals(userPatch.getNewPassword())) {
            throw new RuntimeException();
        }
        String encodedPassword = encoder.encode(userPatch.getNewPassword());
        existingUser.setPassword(encodedPassword);
        return existingUser;

    }

}