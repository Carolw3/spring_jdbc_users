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
    
    public void error(String className, String metothName, String message) {
    try {
        if (!Files.exists(Paths.get(directoryPath))) {
            Files.createDirectory(Paths.get(directoryPath));
        }
        
        Path path = Paths.get(directoryPath+createFileName());

        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dataHora = ahora.format(formato);

        try (var writer = Files.newBufferedWriter(path,
                                            StandardOpenOption.CREATE,
                                            StandardOpenOption.APPEND)) {
            writer.write("["+ dataHora + "] ERROR - " + className+ " - " + metothName + " - " + message+"\n");
        }
    } catch (IOException e) {
        e.printStackTrace();
        // O usar un logger estándar como fallback
    }
}



    public void info(String className, String metothName, String message) throws IOException{

        if (!Files.exists(Paths.get(directoryPath))) {
            Files.createDirectory(Paths.get(directoryPath));
        }

        Path path = Paths.get(directoryPath+createFileName());

        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dataHora = ahora.format(formato);
    
        try (var writer = Files.newBufferedWriter(path,
                                            StandardOpenOption.CREATE,
                                            StandardOpenOption.APPEND)) {
            writer.write("["+ dataHora + "] INFO - " + className+ " - " + metothName + " - " + message+"\n");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String createFileName() {
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String data = hoy.format(formato);

        String nomFitxer = "aplicacio-" + data + ".log";

        return nomFitxer;

    }
}
