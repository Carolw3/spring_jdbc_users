package com.ra2.user.com_ra2_user.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

    public int insertImage(Long user_id, MultipartFile imageFile) throws Exception{
        
        //Busca si existeix l'usuari i si no existeix acaba la execució
        User user = userRepository.findById(user_id);
        if (user == null) {
            return 0;
        }

        //Crea la carpeta si aquesta no existeix
        File folder = new File("src/main/resources/public/images");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        //Crea un nom per a l'arxiu
        String fileName = "image_user_" + user_id + ".jpg";
        //Crea el fitxer amb el nom d'abans
        File destinationFile = new File(folder, fileName);

        try {
            imageFile.transferTo(destinationFile);
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            return 0; // error al escriure l'arxiu
        }

        //Ruta de la imatge
        String imagePath = "/images/" + fileName;

        //Guarda la imatge a la BBDD
        int result = userRepository.updateUserImagePath(user_id, imagePath);
        return result;
    }

}
