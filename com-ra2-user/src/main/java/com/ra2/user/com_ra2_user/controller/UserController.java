package com.ra2.user.com_ra2_user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ra2.user.com_ra2_user.model.User;
import com.ra2.user.com_ra2_user.repository.UserRepository;



@RestController
@RequestMapping("/api")

public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/user")
    public String getUser() {
        return "retorna els usuaris";
    }

    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        int result = userRepository.insertUser(user);

        if(result > 0 ){
            return ResponseEntity.status(HttpStatus.OK).body("El usuario ha sido creado correctamente: " + user.getName().toString());
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ha avido un error al insertar el usuario");
        }
        
    }
    
    @PatchMapping("/user")
    public String updateUser(){
        
        return "modifica un usuari";
    }
    
    @DeleteMapping("/user")
    public String deleteUser(){
        
        return "Elimina un usuari";
    }


}
