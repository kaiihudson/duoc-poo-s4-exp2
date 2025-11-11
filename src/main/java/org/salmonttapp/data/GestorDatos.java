package org.salmonttapp.data;

import org.salmonttapp.model.Data;
import org.salmonttapp.model.Type;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestorDatos {
    public File loadFile(String filePath) throws IllegalAccessException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(filePath);
        if (resource == null) {
            throw new IllegalAccessException("file not found");
        } else {
            return new File(resource.toURI());
        }
    }
    public List<Data> readFromFile(String filePath){
        List<Data> dataArray = new ArrayList<>();
        int counter = 1;
        try {
            File readFile = loadFile(filePath);
            try (Scanner reader = new Scanner(readFile)){
                while  (reader.hasNextLine()){
                    // leer archivo por linea
                    String data = reader.nextLine();
                    // separar los datos con split
                    String[] dataParts = data.split(";");
                    // crear objetos de esos datos
                    int production = -1;
                    int stock = -1;
                    Type tipo;
                    try {
                        production = Integer.parseInt(dataParts[2]);
                        stock = Integer.parseInt(dataParts[4]);
                    } catch (NumberFormatException e) {
                        System.err.println("Error en valores numericos ingresados en linea: " + counter );
                        continue;
                    }
                    if (dataParts[3].equalsIgnoreCase("agricola")){
                        tipo = Type.AGRICOLA;
                    } else if (dataParts[3].equalsIgnoreCase("ganadero")) {
                        tipo = Type.GANADERO;
                    } else {
                        tipo = null;
                        System.err.println("Tipo no soportado en linea " + counter);
                    }
                    Data data1 = new Data(dataParts[0], dataParts[1], production, tipo, stock);
                    // almacena en un array list
                    if (data1.getProduccion() == -1 || data1.getStock() == -1 || data1.getType() == null){
                        continue;
                    } else {
                        if (data1.getProduccion() < data1.getStock()){
                            System.err.println("Produccion < Stock en linea: " + counter);
                            continue;
                        }
                        dataArray.add(data1);
                    }
                    counter++;
                }
            } catch (FileNotFoundException e) {
                System.err.println("file not found");
            }
        } catch (IllegalAccessException e) {
            System.err.println(e.getMessage());
        } catch (URISyntaxException e) {
            System.err.println(e.getStackTrace());
            System.err.println("URI syntax error on " + filePath);
        }
        return dataArray;
    }
}
