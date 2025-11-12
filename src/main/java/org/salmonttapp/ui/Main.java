package org.salmonttapp.ui;

import org.salmonttapp.data.GestorDatos;
import org.salmonttapp.model.Data;
import org.salmonttapp.model.Type;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        GestorDatos gestor = new GestorDatos();
        // muestra todos los elementos
        List<Data> data = gestor.readFromFile("data.txt");
        System.out.println("=".repeat(20));
        for (Data datalet: data){
            System.out.println(datalet);
        }
        // filtra según una condición
        // prod > 1000
        List<Data> filteredByProduction = new ArrayList<>();
        for (Data filterProd: data){
            if (filterProd.getProduction() > 1000){
                filteredByProduction.add(filterProd);
            }
        }

        System.out.println("=".repeat(20));
        System.out.println("Filtering by: producción > 1000");
        System.out.println("=".repeat(20));
        for (Data datalet: filteredByProduction){
            System.out.println(datalet);
        }
        // tipo == "agricola"
        List<Data> filteredByType = new ArrayList<>();
        for (Data filterType: data){
            if (filterType.getType() == Type.AGRICOLA){
                filteredByType.add(filterType);
            }
        }

        System.out.println("=".repeat(20));
        System.out.println("Filtering by: type == agricola");
        System.out.println("=".repeat(20));
        for (Data datalet: filteredByType){
            System.out.println(datalet);
        }
        // 100 < stock < 500
        List<Data> filteredByStock = new ArrayList<>();
        for (Data filterStock: data){
            if (filterStock.getStock() > 100 && filterStock.getStock() < 500){
                filteredByStock.add(filterStock);
            }
        }
        System.out.println("=".repeat(20));
        System.out.println("Filtering by: 100 < stock < 500");
        System.out.println("=".repeat(20));
        for (Data datalet: filteredByStock){
            System.out.println(datalet);
        }

    }
}
