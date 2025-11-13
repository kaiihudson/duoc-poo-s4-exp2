package org.salmonttapp.data;

import org.salmonttapp.data.exceptions.InvalidTypeException;
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
                        // creamos el dato
                        Data data1 = new Data();
                        try {
                            data1.setCentroCultivo(dataParts[0]);
                            data1.setComuna(dataParts[1]);
                            data1.setProduction(validateInteger(dataParts[2]));
                            data1.setType(validateType(dataParts[3]));
                            data1.setStock(validateInteger(dataParts[4]));
                        } catch (NumberFormatException ex) {
                            System.err.println("Valores numéricos erroneos en línea: " + counter);
                            continue;
                        } catch (InvalidTypeException ex) {
                            System.err.println(ex.getMessage() + " en línea: " + counter);
                            continue;
                        }

                        // almacena en un array list
                        if (data1.getProduction() < data1.getStock()){
                            System.err.println("Stock > Produccion en línea: " + counter);
                            continue;
                        }
                        dataArray.add(data1);
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

    /**
     * Validation to translate type to enum.
     * @param rawType string indicating the type
     * @return type parsed by enum
     * @throws InvalidTypeException if type is not added to this validation
     */
    public Type validateType(String rawType) throws InvalidTypeException {
        return switch (rawType) {
            case "agricola" -> Type.AGRICOLA;
            case "ganadero" -> Type.GANADERO;
            default -> throw new InvalidTypeException("Tipo no válido");
        };
    }

    /**
     * Validation to assure a number is a number and acts like a number
     * @param rawNumber the string containing a suspected number
     * @return the number in int format
     * @throws NumberFormatException if the number does not parse
     */
    public int validateInteger(String rawNumber) throws NumberFormatException {
        return Integer.parseInt(rawNumber);
    }
}
