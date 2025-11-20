package org.salmonttapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cultivator {
    private final String name;
    private final String commune;
    private final Map<String, List<Integer>> errors = new HashMap<>();

    public Cultivator(String name, String commune) {
        this.name = name;
        this.commune = commune;
    }

    public String getName() {
        return name;
    }

    public String getCommune() {
        return commune;
    }

    /**
     * Method to interact with the error map, in order to create errors on invalid data lines
     * @param filePath file from which the data was gathered
     * @param lineNum file line in which the data was gathered
     */
    public void addNewError(String filePath, int lineNum){
        List<Integer> listedErrors = new ArrayList<>();
        if (errors.get(filePath) == null){
            listedErrors.add(lineNum);
            errors.put(filePath, listedErrors);
        } else {
            List<Integer> previouslyListed = errors.get(filePath);
            previouslyListed.add(lineNum);
            errors.replace(filePath, previouslyListed);
        }
    }

    @Override
    public String toString() {
        StringBuilder error = new StringBuilder();
        if (!errors.isEmpty()){
            for (String filename: errors.keySet()){
                error.append("Error encontrado en: ").append(filename).append(", lineas: ").append(errors.get(filename)).append(". ");
            }
        }
        return "Centro de cultivo: " + name + ". En comuna: " + commune + ". " + error;
    }
}
