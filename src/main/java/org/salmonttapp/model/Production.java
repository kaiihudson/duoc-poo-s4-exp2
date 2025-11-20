package org.salmonttapp.model;

/**
 * Class to represent a row in the file to read
 */
public class Production {
    private Cultivator cultivator;
    private int production;
    private SalmonType salmonType;
    private int stock;

    public Production() {
    }

    /**
     * @param centroCultivo the "centro de cultivo" name
     * @param production the production number the "centro de cultivo" has
     * @param salmonType the type of production a "centro de cultivo" has
     * @param stock the current stock a "centro de cultivo" has
     */
    public Production(Cultivator centroCultivo, int production, SalmonType salmonType, int stock) {
        this.cultivator = centroCultivo;
        this.production = production;
        this.salmonType = salmonType;
        this.stock = stock;
    }

    public Cultivator getCentroCultivo() {
        return cultivator;
    }

    public void setCentroCultivo(Cultivator centroCultivo){
        this.cultivator = centroCultivo;
    }

    public int getProduction() {
        return production;
    }

    public void setProduction(int production) {
        this.production = production;
    }

    public SalmonType getSalmonType() {
        return salmonType;
    }

    public void setType(SalmonType salmonType) {
        this.salmonType = salmonType;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return  cultivator +
                "Producción: " + production +
                ", Tipo de Salmón: " + salmonType.toString().toLowerCase() +
                ", Stock: " + stock;
    }
}
