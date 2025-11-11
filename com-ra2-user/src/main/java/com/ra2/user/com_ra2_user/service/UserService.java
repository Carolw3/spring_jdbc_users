package com.ra2.user.com_ra2_user.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ra2.user.com_ra2_user.model.User;
import com.ra2.user.com_ra2_user.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    //-1-
    public List<User>findAll() {
        List<User> llista = userRepository.findAll();
        return llista;
    }

    //-2- -6-
    public User findById(Long user_id) {
        User user = userRepository.findById(user_id);
        return user;
    }

    //-3-
    public int insertUser(User user) {
        int result = userRepository.insertUser(user);
        return result;
    }

    //-5-
    public void updateUser(User user, Long user_id) {
        userRepository.updateUser(user, user_id);
    }

    //-6-
    public void patchUser(String name, Long user_id) {
        userRepository.patchUser(name, user_id);
    }

    //-7-
    public void deleteUser(Long user_id){
        userRepository.deleteUser(user_id);
    }

    //-4-
    public String insertImage(Long user_id, MultipartFile imageFile) throws Exception{
        
        //Busca si existeix l'usuari i si no existeix acaba la execució
        User user = userRepository.findById(user_id);
        if (user == null) {
            return null;
        }

        //Crea la carpeta si aquesta no existeix
        Path folder = Paths.get("src/main/resources/public/images");
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }

        //Crea un nom per a l'arxiu
        String fileName = "image_user_" + user_id + imageFile.getOriginalFilename();
        //Crea el fitxer amb el nom d'abans
        Path destinationFile = folder.resolve(fileName);

        try (InputStream inputStream = imageFile.getInputStream()){
            //Sobreescriu
            Files.copy(inputStream,destinationFile, StandardCopyOption.REPLACE_EXISTING);
        }

        //Ruta de la imatge
        String imagePath = "/images/" + fileName;

        //Guarda la imatge a la BBDD
        int result = userRepository.updateUserImagePath(user_id, imagePath);
        System.out.println(result);
        if(result == 0){
            return "Error en la inserció de la imatge";
        }

        return "/public/" + imagePath;
    }

}
