package com.ra2.user.com_ra2_user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ra2.user.com_ra2_user.model.User;
import com.ra2.user.com_ra2_user.repository.UserRepository;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api")

public class UserController {

    @Autowired
    UserRepository userRepository;

    //Retorna un allista Json amb tots els usuaris i si no hi ha usuaris dona error
    @GetMapping("/users")
    public ResponseEntity<List<User>>getUser() {
        List<User> llista = userRepository.findAll();
        if (llista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
        }
    
    }

    @GetMapping("/users/{user_id}") // Per exemple: --> http://localhost:8081/api/users/2 <--
    public ResponseEntity<?> getUserById(@PathVariable Long user_id) {
        

        try {
            User user = userRepository.findById(user_id);
            return ResponseEntity.ok(user); // 200 OK + usuari en JSON al body
            
        }catch(EmptyResultDataAccessException e){
            return ResponseEntity.badRequest().body("ERROR : L'usuari amb id " + user_id + " no existeix"); // Aixo es per si no existeix o no el troba
        }
    }

    // Punt 4. Crea un nou usuari
    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        int result = userRepository.insertUser(user);

        if(result > 0 ){
            return ResponseEntity.status(HttpStatus.OK).body("El usuario ha sido creado correctamente: " + user.getName().toString());
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ha avido un error al insertar el usuario");
        }
        
    }

    // Punt 7. Modifica un usuari buscant per la seva id
    @PutMapping("/users/{user_id}")
    public ResponseEntity<String> putUser(@PathVariable Long user_id, @RequestBody User user) {

        try {
            userRepository.updateUser(user, user_id);
            return ResponseEntity.ok(
                "El usuario " + user.getName() + " ha sido editado correctamente:\n" +
                "ID: " + user_id + "\n" +
                "Name: " + user.getName() + "\n" +
                "Descripcion: " + user.getDescripcion() + "\n" +
                "Email: " + user.getEmail()
            );
        } catch(EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest()
                    .body("ERROR: L'usuari amb id " + user_id + " no existeix");
        }
    }
    
    // 8. Modifica nomes el nom de un suari nuscat per id i nom
    @PatchMapping("/users/{user_id}/name")
    public ResponseEntity<String> changeNameUser(@PathVariable Long user_id, @RequestParam() String name) {
        User user = userRepository.findById(user_id);

        if(name.length() > 100){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("El nom no pot tenir mes de 100 caracters");
        }

        try {
            userRepository.patchUser(name, user_id);
            return ResponseEntity.ok(
                "El usuario " + user.getName() + " ha sido editado correctamente:\n" +
                "ID: " + user_id + "\n" +
                "Name: " + name
            );
        } catch(EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest()
                    .body("ERROR: L'usuari amb id " + user_id + " no existeix");
        }
    }

    // 9. Aquest metode eliminara completament un usuari amb l'id que s'ndiqui en el path
    @DeleteMapping("/users/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long user_id){
        try {
            userRepository.deleteUser(user_id);
            return ResponseEntity.ok(
                "L'usuari amb l'id " + user_id + " ha estat eliminat correctament de la base de dades"
            );
        } catch(EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest()
                    .body("ERROR: L'usuari amb id " + user_id + " no existeix");
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
