package com.ra2.user.com_ra2_user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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

import com.ra2.user.com_ra2_user.service.UserService;




@RestController
@RequestMapping("/api")

public class UserController {

    @Autowired
    UserService userService;

    //-1-Retorna un allista Json amb tots els usuaris i si no hi ha usuaris dona error
    @GetMapping("/users")
    public ResponseEntity<List<User>>getUser() {
        List<User> llista = userService.findAll();
        if (llista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
        }
    
    }

    //-2-
    @GetMapping("/users/{user_id}") // Per exemple: --> http://localhost:8081/api/users/2 <--
    public ResponseEntity<User> getUserById(@PathVariable Long user_id) {
        
        User user = userService.findById(user_id); //Crea un usauari amb l'id proporcionat

        if (user != null){   //Comproba si l'usuari s'ha creat. Si no existeix cap usuari amb l'id donat crea un user null
            return ResponseEntity.ok(user); // 200 OK + usuari en JSON al body
        }else{
            return ResponseEntity.badRequest().body(null); // Retorna aixó si l'user es null
        }

    }

    //-3- Punt 4. Crea un nou usuari
    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        int result = userService.insertUser(user);

        if(result > 0 ){
            return ResponseEntity.status(HttpStatus.OK).body("El usuario ha sido creado correctamente: " + user.getName().toString());
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ha avido un error al insertar el usuario");
        }
        
    }

    // -4-Afegeix una imatge a l'usuari
    @PostMapping("/users/{user_id}/image")
    public ResponseEntity<String> addImageUser(@PathVariable Long user_id, @RequestParam("image") MultipartFile imageFile) throws Exception{
        String result = userService.insertImage(user_id, imageFile);

        if(result != null ){
            return ResponseEntity.status(HttpStatus.OK).body("La imagen del usuario con id " + user_id + ", ha sido ingresada correctamente en : " + result);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null );
        }
        
    }

    // -5- Punt 7. Modifica un usuari buscant per la seva id
    @PutMapping("/users/{user_id}")
    public ResponseEntity<String> putUser(@PathVariable Long user_id, @RequestBody User user) {

        try {
            userService.updateUser(user, user_id);
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
    
    // -6- 8. Modifica nomes el nom de un suari nuscat per id i nom
    @PatchMapping("/users/{user_id}/name")  // http://localhost:8081/api/users/2?name=Carol
    public ResponseEntity<String> changeNameUser(@PathVariable Long user_id, @RequestParam() String name) {
        User user = userService.findById(user_id);

        if(name.length() > 100){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("El nom no pot tenir mes de 100 caracters");
        }

        try {
            userService.patchUser(name, user_id);
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

    // -7- 9. Aquest metode eliminara completament un usuari amb l'id que s'ndiqui en el path
    @DeleteMapping("/users/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long user_id){
        try {
            userService.deleteUser(user_id);
            return ResponseEntity.ok(
                "L'usuari amb l'id " + user_id + " ha estat eliminat correctament de la base de dades"
            );
        } catch(EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest()
                    .body("ERROR: L'usuari amb id " + user_id + " no existeix");
        }
    }

    // -8- Arxiu csv---- @RequestParam("csv")-> aqui "csv" es la Key
    @PostMapping("/users/csv")
    public ResponseEntity<String> addCsv(@RequestParam("csv") MultipartFile csv) throws Exception{
        String result = userService.insertCsv(csv);

        if(result != null ){
            return ResponseEntity.status(HttpStatus.OK).body( result );
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null );
        }
        
    }

    @PostMapping("/users/upload-json")
    public ResponseEntity<String> addJson(@RequestParam MultipartFile json) {
        int result = userService.insertJson(json);

        if(result > 0 ){
            return ResponseEntity.status(HttpStatus.OK).body( "Usuaris afegits per Json : " + result  );
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null );
        }
        
    }


}
