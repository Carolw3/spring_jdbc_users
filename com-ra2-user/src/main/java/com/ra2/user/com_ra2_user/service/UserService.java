package com.ra2.user.com_ra2_user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ra2.user.com_ra2_user.model.User;
import com.ra2.user.com_ra2_user.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User>findAll() {
        List<User> llista = userRepository.findAll();
        return llista;
    }

    public User findById(Long user_id) {
        User user = userRepository.findById(user_id);
        return user;
    }

    public int insertUser(User user) {
        int result = userRepository.insertUser(user);
        return result;
    }

    public void updateUser(User user, Long user_id) {
        userRepository.updateUser(user, user_id);
    }

    public void patchUser(String name, Long user_id) {
        userRepository.patchUser(name, user_id);
    }

    public void deleteUser(Long user_id){
        userRepository.deleteUser(user_id);
    }
}
