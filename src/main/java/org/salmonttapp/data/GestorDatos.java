package org.salmonttapp.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.salmonttapp.common.exceptions.InvalidOperationException;
import org.salmonttapp.common.exceptions.InvalidTypeException;
import org.salmonttapp.model.Cultivator;
import org.salmonttapp.model.Production;
import org.salmonttapp.model.SalmonType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import static org.salmonttapp.common.utils.Utils.validateInteger;
import static org.salmonttapp.common.utils.Utils.validateType;

public class GestorDatos {
    private static final Logger log = LogManager.getLogger(GestorDatos.class);
    private final Map<Integer, Cultivator> cultivatorArray = new HashMap<>();
    private final List<Production> dataArray = new ArrayList<>();

    /**
     * Checks whether a cultivator exists in the cultivatorArray map or not
     * @param cultivator cultivator object to be checked in instance map
     * @return boolean response based on existance on the cultivator map
     */
    public boolean checkIfCultivatorExists(Cultivator cultivator){
        boolean itExists = false;
        for (Cultivator cult: cultivatorArray.values()){
            if (Objects.equals(cult.getName(), cultivator.getName())){
                itExists = Objects.equals(cult.getCommune(), cultivator.getCommune());
                if (itExists){
                    break;
                }
            }
        }
        return itExists;
    }

    /**
     * Add new Cultivator to the map based on the last available index
     * @param newCultivator cultivator object
     */
    public void addToMap(Cultivator newCultivator){
        Integer nextInt = cultivatorArray.size()+1;
        cultivatorArray.put(nextInt, newCultivator);
    }

    /**
     * Get a cultivator object or referenced object based on the name and the commune
     * of the cultivator
     * @param name cultivator's name
     * @param commune cultivator's commune
     * @return Cultivator object
     */
    public Cultivator getCultivator(String name, String commune){
        Cultivator cultivator = new Cultivator(name, commune);
        if (checkIfCultivatorExists(cultivator)){
            for (Cultivator cult: cultivatorArray.values()){
                if (Objects.equals(cult.getName(), cultivator.getName())){
                    if(Objects.equals(cult.getCommune(), cultivator.getCommune())){
                        return cult;
                    }
                }
            }
        }
        addToMap(cultivator);
        return cultivator;
    }
    /**
     * Load txt file from filepath
     * @param filePath the name of the file in resources folder
     * @return File object
     * @see File
     * @throws IllegalAccessException if name is null
     * @throws URISyntaxException if this URL is not formatted strictly according to RFC2396 and cannot be converted to a URI.
     */
    public File loadTxtFile(String filePath) throws IllegalAccessException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(filePath);
        if (resource == null) {
            throw new IllegalAccessException("file not found");
        } else {
            return new File(resource.toURI());
        }
    }
    /**
     * Read data from an Excel file in filepath, adding parse checks for data purge
     * @param filePath the name of the file in the resources folder
     */
    public void parseDataFromExcelFile(String filePath){

        try (FileInputStream fis = new FileInputStream(filePath)){
            XSSFWorkbook book = new XSSFWorkbook(fis);
            for (int i = 0; i < book.getNumberOfSheets(); i ++){
                XSSFSheet sheet = book.getSheetAt(i);
                for (int si = 1; si <= sheet.getLastRowNum(); si++) {
                    // validate cultivator
                    XSSFRow row = sheet.getRow(si);
                    String cultivatorName = row.getCell(0).getStringCellValue();
                    String cultivatorCommune = row.getCell(1).getStringCellValue();
                    Cultivator finalCultivator = getCultivator(cultivatorName, cultivatorCommune);
                    try {
                        // validate data
                        int production = (int) row.getCell(2).getNumericCellValue();
                        String salmonType = row.getCell(3).getStringCellValue();
                        int stock = (int) row.getCell(4).getNumericCellValue();
                        if (production < stock) {
                            finalCultivator.addNewError(filePath, si+1);
                        } else {
                            addtoList(finalCultivator, production, salmonType, stock, filePath, si+1);
                        }
                    } catch (IllegalStateException e) {
                        finalCultivator.addNewError(filePath, si+1);
                        log.error("{} IllegalStateException: ", si, e);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            log.error("File not found", e);
        } catch (IOException e) {
            log.error("IO Exception", e);
        }
    }
    /** Read data from a plain text file in filepath, adding parse checks for data purge
     * @param filePath the name of the file in the resources folder
     */
    public void parseDataFromTextFile(String filePath) {
        int counter = 0;
        try (Scanner reader = new Scanner(loadTxtFile(filePath))) {
            while (reader.hasNextLine()) {
                counter++;
                // leer archivo por línea
                String data = reader.nextLine();
                // separar los datos con split
                String[] dataParts = data.split(";");
                if (dataParts.length == 5) {
                    // crear objetos de esos datos
                    // creamos el dato
                    Production prod1 = new Production();
                    try {
                        //set data parts
                        //check if cultivator already exists
                        Cultivator finalCultivator = getCultivator(dataParts[0], dataParts[1]);
                        prod1.setCentroCultivo(finalCultivator);
                        prod1.setProduction(validateInteger(dataParts[2]));
                        prod1.setType(validateType(dataParts[3]));
                        prod1.setStock(validateInteger(dataParts[4]));
                    } catch (NumberFormatException ex) {
                        prod1.getCentroCultivo().addNewError(filePath, counter);
                        log.warn("Valores numericos no validos en linea {}", counter);
                        continue;
                    } catch (InvalidTypeException ex) {
                        prod1.getCentroCultivo().addNewError(filePath, counter);
                        log.warn("invalid type", ex);
                        log.warn("en linea: {}", counter);
                        continue;
                    }
                    // almacena en un array list
                    if (prod1.getProduction() < prod1.getStock()) {
                        prod1.getCentroCultivo().addNewError(filePath, counter);
                        log.warn("Stock > Produccion en linea: {}", counter);
                        continue;
                    }
                    addtoList(prod1);
                } else {
                    log.warn("Linea no valida en : {}", counter);
                }
            }
        } catch (FileNotFoundException e) {
            log.error("File not found");
        } catch (URISyntaxException ex) {
            log.error("URI Syntax error on: {}", filePath);
            System.err.println("URI syntax error on " + filePath);
        } catch (IllegalAccessException ex) {
            log.error("Illegal access exception.", ex);
        }
    }

    /** Add data to the productionList with checking if the data is valid or not
     * @param cult cultivator object
     * @param production production int value
     * @param type string type to be checked
     * @param stock stock in value
     * @param filepath filepath reference for error logging
     * @param row row reference for error logging
     */
    public void addtoList(Cultivator cult, int production, String type, int stock, String filepath, int row){
        SalmonType newType;
        try {
            newType = validateType(type);
        } catch (InvalidTypeException e){
            cult.addNewError(filepath, row);
            return;
        }
        Production prod1 = new Production(cult, production, newType, stock);
        dataArray.add(prod1);
    }
    /** Overloaded method to add to the production list
     * @param prod production object
     */
    public void addtoList(Production prod){
        dataArray.add(prod);
    }
    /**
     * Check which version of the parser to use
     * @param filePath the name of the file to parse in the resources folder
     */
    public void parseDataFromFile(String filePath){
        String[] filePathParts = filePath.split("\\.");
        switch (filePathParts[filePathParts.length - 1].toLowerCase()) {
            case "txt", "csv":
                parseDataFromTextFile(filePath);
                return;
            case "xlsx", "xls":
                parseDataFromExcelFile(filePath);
                return;
            default:
                log.error("Tipo de archivo {} no soportado", filePathParts[filePathParts.length - 1]);
        }
    }

    /** Accessor for the cultivators map as a list
     * @return a list with the cultivators
     */
    public List<Cultivator> getAllCultivators(){
        return new ArrayList<>(cultivatorArray.values());
    }

    /** Accessor for the production list
     * @return a list with the productions
     */
    public List<Production> getAllProduction(){
        return dataArray;
    }

    /** Filter for the production list based on its type
     * @param type String indicating its type
     * @return a sub-list of the production list filtered by type param
     */
    public List<Production> filterBySalmonType(String type){
        List<Production> productionFiltered = new ArrayList<>();
        try{
            SalmonType salmonType = validateType(type);
            for (Production prod: dataArray){
                if (prod.getSalmonType() == salmonType){
                    productionFiltered.add(prod);
                }
            }
        } catch (InvalidTypeException e) {
            log.error("invalid type", e);
        }
        return productionFiltered;
    }

    /** Filter for the prodction list based on its production value
     * @param production String indicating the production threshold
     * @param comparative "lt" less-than "gt" greater-than "eq" equals
     * @return a sub-list of the production list filtered by a production threshold and a comparative
     */
    public List<Production> filterByProduction(String production, String comparative){
        List<Production> productionFiltered = new ArrayList<>();
        try{
            int prodThreshold = validateInteger(production);
            for (Production prod: dataArray){
                switch (comparative.toLowerCase()){
                    case "gt":
                        if (prod.getProduction() > prodThreshold){
                            productionFiltered.add(prod);
                        }
                        break;
                    case "lt":
                        if (prod.getProduction() < prodThreshold){
                            productionFiltered.add(prod);
                        }
                        break;
                    case "eq":
                        if (prod.getProduction() == prodThreshold) {
                            productionFiltered.add(prod);
                        }
                        break;
                    default:
                        throw new InvalidOperationException("Operación no válida");
                }
            }
        } catch (NumberFormatException | InvalidOperationException e) {
            log.error("Invalid data", e);
        }
        return productionFiltered;
    }

    /** Filter for the production list based on its stock value
     * @param production String indicating the stock threshold
     * @param comparative "lt" less-than "gt" greater-than "eq" equals
     * @return a sub-list of the production list filtered by a stock threshold and a comparative
     */
    public List<Production> filterByStock(String production, String comparative){
        List<Production> productionFiltered = new ArrayList<>();
        try{
            int stockThreshold = validateInteger(production);
            for (Production prod: dataArray){
                switch (comparative.toLowerCase()){
                    case "gt":
                        if (prod.getStock() > stockThreshold){
                            productionFiltered.add(prod);
                        }
                        break;
                    case "lt":
                        if (prod.getStock() < stockThreshold){
                            productionFiltered.add(prod);
                        }
                        break;
                    case "eq":
                        if (prod.getStock() == stockThreshold) {
                            productionFiltered.add(prod);
                        }
                        break;
                    default:
                        throw new InvalidOperationException("Operación no válida");
                }
            }
        } catch (NumberFormatException | InvalidOperationException e) {
            log.error(e);
        }
        return productionFiltered;
    }
}
