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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra2.user.com_ra2_user.logging.CustomLogging;
import com.ra2.user.com_ra2_user.model.User;
import com.ra2.user.com_ra2_user.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    CustomLogging customLogging;

    //-1-
    public List<User>findAll() throws IOException {
        List<User> llista = userRepository.findAll();

        if(llista.size() < 1){
            customLogging.error(
                "UserService",
                "findById",
                "No hi ha usuaris registrats");
        }else{
            customLogging.info(
                "UserService",
                "findById",
                "Consultant tots els usuaris." );
        }

        return llista;
    }

    //-2- -6-
    public User findById(Long user_id) throws IOException {

        User user = userRepository.findById(user_id);
        if(user == null){
            customLogging.error(
                "UserService",
                "findById",
                "L'usuari amb id " + user_id + " no existeix.");
        }else{
            customLogging.info(
                "UserService",
                "findById",
                "Consultant l'usuari amb id: " + user_id );
        }

        return user;
    }

    //-3-
    public int insertUser(User user) throws IOException {

        customLogging.info(
                "UserService",
                "insertUser",
                "Creant un usuari.");

        int result = userRepository.insertUser(user);
        if(result < 1){
            customLogging.error(
                "UserService",
                "insertUser",
                "L'usuari amb el nom: " + user.getName() + ", no s'ha creat correctament. Missatge d'"+ ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error intern del servidor"));
        }else{
            customLogging.info(
                "UserService",
                "insertUser",
                "Usuari creat correctament" );
        }

        return result;
    }

    //-5- complet
    public int updateUser(User user, Long user_id) throws IOException {
        
        customLogging.info(
                "UserService",
                "updateUser",
                "Modificant l'usuari amb id: "+ user_id);

        int result = userRepository.updateUser(user, user_id);
        
        if(result > 0){
            customLogging.info(
                "UserService",
                "updateUser",
                "Usuari modificat correctament" );
                return 1;
        }else{
            customLogging.error(
                "UserService",
                "updateUser",
                "L'usuari amb id: " + user_id + " no existeix.");
                return 0;
        }

    }

    //-6- parcial
    public int patchUser(String name, Long user_id) throws IOException {
        customLogging.info(
                "UserService",
                "patchUser",
                "Modificant l'usuari amb id: "+ user_id);
        
        int result = userRepository.patchUser(name, user_id);
        if(result > 0) {
            customLogging.info(
                "UserService",
                "patchUser",
                "Usuari modificat correctament." );
            return 1;

        } else{
            customLogging.error(
                "UserService",
                "patchUser",
                "L'usuari amb id: " + user_id + " no existeix.");
            return 0;
        }

    }

    //-7-
    public int deleteUser(Long user_id) throws IOException{
        customLogging.info(
                "UserService",
                "deleteUser",
                "Borrant l'usuari amb id: "+ user_id);

        int result = userRepository.deleteUser(user_id);

        if(result > 0 ){
            customLogging.info(
                "UserService",
                "deleteUser",
                "L'usuari amb id: " + user_id + " s'ha borrat correctament." );
            return 1;

        }else{
            customLogging.error(
                "UserService",
                "deleteUser",
                "L'usuari amb id: " + user_id + " no existeix.");
            return 0;
        }
    }

    //-4-
    public String insertImage(Long user_id, MultipartFile imageFile) throws Exception{
        customLogging.info(
                "UserService",
                "insertImage",
                "Afegint la imatge " + imageFile.getOriginalFilename() + " per a l'estudiant amb id " + user_id);

        //Busca si existeix l'usuari i si no existeix acaba la execució
        User user = userRepository.findById(user_id);
        if (user == null) {
            customLogging.error(
                "UserService",
                "insertImage",
                "L'usuari amb id: " + user_id + " no existeix.");
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
        customLogging.info(
                "UserService",
                "insertImage",
                "La imatge s'ha guardat correctament. El path es: " + imagePath );
        return "/private" + imagePath;
    }



    //Inserta mes d'un usuari a l'hora amb un csv
    public String insertCsv(MultipartFile csv) throws Exception {

        customLogging.info(
                "UserService",
                "insertCsv",
                "Carregant la informació del fitxer: " + csv.getOriginalFilename());

        int contaUsuarisIngresats = 0;
        int contaUsuarisDescartats = 0;
        List<String> errorList = new ArrayList<>();

        int usuariResponse;

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
        if(contaUsuarisIngresats > 0){
            customLogging.info(
                "UserService",
                "insertCsv",
                "S'han guardat correctament " +contaUsuarisIngresats+ " registres i han donat error "+contaUsuarisDescartats+" registres.");

        return "S'an ingresat correctament " + contaUsuarisIngresats + " usuaris.\n" +
                "S'han produit " + contaUsuarisDescartats + " errors:\n" +
                errorList;
        }
        customLogging.error(
                "UserService",
                "insertCsv",
                "Error en la linea "+ errorList + " del fitxer. Missatge d'error: " + ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error intern del servidor") );
            return null;

    }

    public int insertJson(MultipartFile json) throws IOException{
        customLogging.info(
                "UserService",
                "insertJson",
                "Carregant la informació del fitxer: " + json.getOriginalFilename());

        int usersCounter=0;
        int errorCounter = 0;
        List<Integer> errors= new ArrayList();

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
                    errorCounter++;
                    errors.add(errorCounter);
                    continue;
                }
                userRepository.insertUser(user);
                usersCounter++;
            }
            if(usersCounter > 0){
                customLogging.info(
                "UserService",
                "insertJson",
                "S'han guardat correctament " +usersCounter+ " registres i han donat error "+errorCounter+" registres.");
            }else{
                customLogging.error(
                "UserService",
                "insertJson",
                "Error en la linea "+ errors + " del fitxer. Missatge d'error :" + ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error intern del servidor") );
            
            return 0;
            }
            

        } catch (Exception e) {

            customLogging.error(
                "UserService",
                "insertJson",
                "Error en la linea "+ errors + " del fitxer. Missatge d'" + ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error intern del servidor") );
            
            return 0;
        }

        return usersCounter;
    }

}
