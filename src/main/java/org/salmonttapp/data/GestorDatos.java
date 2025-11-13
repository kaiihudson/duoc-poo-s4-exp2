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
    /**
     * @param filePath the name of the file in resources folder
     * @return File object
     * @see File
     * @throws IllegalAccessException if name is null
     * @throws URISyntaxException if this URL is not formatted strictly according to RFC2396 and cannot be converted to a URI.
     */
    public File loadFile(String filePath) throws IllegalAccessException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(filePath);
        if (resource == null) {
            throw new IllegalAccessException("file not found");
        } else {
            return new File(resource.toURI());
        }
    }

    /**
     * Read data from a file in filepath, adding parse checks for data purge
     * This method validates:
     * <ul>
     *     <li> stock <= production </li>
     *     <li> type is valid </li>
     *     <li> numbers are valid</li>
     * </ul>
     * @param filePath the name of the file to parse in the resources folder
     * @return List with all the valid data from the file
     */
    public List<Data> parseDataFromFile(String filePath){
        List<Data> dataArray = new ArrayList<>();
        int counter = 0;
        try {
            File readFile = loadFile(filePath);
            try (Scanner reader = new Scanner(readFile)){
                while  (reader.hasNextLine()){
                    counter++;
                    // leer archivo por línea
                    String data = reader.nextLine();
                    // separar los datos con split
                    String[] dataParts = data.split(";");
                    if (dataParts.length == 5){
                        // crear objetos de esos datos
                        int production = -1;
                        int stock = -1;
                        Type tipo;
                        try {
                            production = Integer.parseInt(dataParts[2]);
                            stock = Integer.parseInt(dataParts[4]);
                        } catch (NumberFormatException e) {
                            System.err.println("Error en valores numéricos ingresados en linea: " + counter + ". Leyendo: Producción=" + dataParts[2] + " Stock=" + dataParts[4]);
                            continue;
                        }
                        //TODO: this could be a switch if more cases are needed
                        if (dataParts[3].equalsIgnoreCase("agricola")){
                            tipo = Type.AGRICOLA;
                        } else if (dataParts[3].equalsIgnoreCase("ganadero")) {
                            tipo = Type.GANADERO;
                        } else {
                            tipo = null;
                            System.err.println("Tipo no soportado en linea: " + counter + ". Leyendo: " + dataParts[3]);
                        }
                        // creamos el dato
                        Data data1 = new Data(dataParts[0], dataParts[1], production, tipo, stock);
                        // almacena en un array list
                        if ((data1.getProduction() != -1) && (data1.getStock() != -1) && (data1.getType() != null)) {
                            if (data1.getProduction() < data1.getStock()){
                                System.err.println("Producción < Stock en linea: " + counter);
                                continue;
                            }
                            dataArray.add(data1);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.err.println("file not found");
            }
        } catch (IllegalAccessException e) {
            System.err.println(e.getMessage());
        } catch (URISyntaxException e) {
            System.err.println("URI syntax error on " + filePath);
        }
        return dataArray;
    }
}
