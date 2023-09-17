package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        System.out.println("Inside 'UserServiceImpl.getAllUsers()'");
        List<User> all = userRepository.findAll();
        all.forEach(System.out::println);
        return all;
    }

    @Override
    public User getUser(Long id) {
        System.out.println("Inside 'UserServiceImpl.getUser()'");
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User getUserByUsername(String username) {
        System.out.println("Inside 'UserServiceImpl.getUserByUsername()'");
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        System.out.println("Inside 'UserServiceImpl.saveUser()'");
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(Long id, User user) {
        System.out.println("Inside 'UserServiceImpl.updateUser()'");
        user.setId(id);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        System.out.println("Inside 'UserServiceImpl.deleteUser()'");
        userRepository.deleteById(id);
    }
}
