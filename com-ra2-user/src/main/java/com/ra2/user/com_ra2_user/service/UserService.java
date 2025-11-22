package com.ra2.user.com_ra2_user.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra2.user.com_ra2_user.model.User;
import com.ra2.user.com_ra2_user.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper mapper;

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
        Path folder = Paths.get("private/images");
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

        return "/private" + imagePath;
    }



    //Inserta mes d'un usuari a l'hora amb un csv
    public String insertCsv(MultipartFile csv) throws Exception{
        int contaUsuarisIngresats = 0;
        int contaUsuarisDescartats = 0;
        List<String> errorList = new ArrayList<>();
        
        try(BufferedReader input = new BufferedReader(new InputStreamReader(csv.getInputStream()))){
            int contaLineas = 0;
            while(contaLineas > -1){
                String linea = input.readLine();
                contaLineas++;
                if (linea == null){ contaLineas = -1; continue; }
                if(contaLineas == 1){ continue;} //Salta la primera linea que es la capçelera
                String[] info = linea.split(",");
                if(info.length != 4 ){
                    contaUsuarisDescartats ++;
                    errorList.add(info[0]);

                    continue;
                }
                User user = new User(info[0],info[1],info[2],info[3]);
                userRepository.insertUser(user);
                contaUsuarisIngresats++;
            }
        }
        if(contaUsuarisIngresats == 0){
            return null;
        }

        return "S'an ingresat correctament " + contaUsuarisIngresats + " usuaris.\n" +
                "S'han produit " + contaUsuarisDescartats + " errors:\n" +
                errorList;
                
    }

    public int insertJson(MultipartFile json){
        int usersCounter=0;

        try {
            
            JsonNode arrel = mapper.readTree(json.getInputStream());
            JsonNode data = arrel.path("data");
            int count = data.path("count").asInt();
            String control = data.path("control").asText();
            JsonNode users = data.path("users");

            for (JsonNode userF : users) {
                String name = userF.path("name").asText();
                String descripcion = userF.path("descripcion").asText();
                String email = userF.path("email").asText();
                String password = userF.path("password").asText();
                
                User user = new User(name, descripcion, email, password);
                if (user == null){
                    continue;
                }
                userRepository.insertUser(user);
                usersCounter++;
            }

        } catch (Exception e) {
            return 0;
        }

        return usersCounter;
    }

}
