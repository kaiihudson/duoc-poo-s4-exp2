package org.salmonttapp.ui;

import org.salmonttapp.data.GestorDatos;
import org.salmonttapp.model.Cultivator;
import org.salmonttapp.model.Production;

public class Main {
    public static void main(String[] args){
        GestorDatos gestor = new GestorDatos();
        gestor.parseDataFromFile("data.txt");
        gestor.parseDataFromFile("Book1.xlsx");
        System.out.println("Showing all valid data");
        for (Production prod: gestor.getAllProduction()){
            System.out.println(prod);
        }
        System.out.println("filtering by type: coho");
        for (Production prod: gestor.filterBySalmonType("coho")){
            System.out.println(prod);
        }
        System.out.println("filtering by prod > 1000");
        for (Production prod: gestor.filterByProduction("1000", "gt")){
            System.out.println(prod);
        }
        System.out.println("filtering by stock < 150");
        for (Production prod: gestor.filterByStock("150", "lt")){
            System.out.println(prod);
        }
        System.out.println("Showing all cultivators including errors");
        for (Cultivator cult: gestor.getAllCultivators()){
            System.out.println(cult.printErrrors());
        }
    }
}
