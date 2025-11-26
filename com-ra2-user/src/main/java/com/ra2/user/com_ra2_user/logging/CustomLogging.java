package com.ra2.user.com_ra2_user.logging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component

public class CustomLogging {
    public final String directoryPath = "logs/";

    public CustomLogging() {

    }
    
    public void error(String className, String metothName, String message){

        Path path = Paths.get(createFileName());

        try (var writer = Files.newBufferedWriter(path,
                                            StandardOpenOption.CREATE,
                                            StandardOpenOption.APPEND)) {
            writer.write(""+LocalDateTime.now() + " ERROR - " + className+ " - " + metothName + " - " + message);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void info(String className, String metothName, String message){

        Path path = Paths.get(createFileName());

        try (var writer = Files.newBufferedWriter(path,
                                            StandardOpenOption.CREATE,
                                            StandardOpenOption.APPEND)) {
            writer.write(""+LocalDateTime.now() + " INFO - " + className+ " - " + metothName + " - " + message);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String createFileName(){
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String data = hoy.format(formato);

        String nomFitxer = "aplicacio-" + data + ".log";

        return nomFitxer;

    }
}
