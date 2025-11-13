package org.salmonttapp.model;

/**
 * Data class to represent a row in the file to read
 */
public class Data {
    private String centroCultivo;
    private String comuna;
    private int production;
    private Type type;
    private int stock;

    /**
     * @param centroCultivo the "centro de cultivo" name
     * @param comuna the "comuna" the "centro de cultivo" is at
     * @param production the production number the "centro de cultivo" has
     * @param type the type of production a "centro de cultivo" has
     * @param stock the current stock a "centro de cultivo" has
     */
    public Data(String centroCultivo, String comuna, int production, Type type, int stock) {
        this.centroCultivo = centroCultivo;
        this.comuna = comuna;
        this.production = production;
        this.type = type;
        this.stock = stock;
    }

    public String getCentroCultivo() {
        return centroCultivo;
    }

    public void setCentroCultivo(String centroCultivo) {
        this.centroCultivo = centroCultivo;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public int getProduction() {
        return production;
    }

    public void setProduction(int production) {
        this.production = production;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "centroCultivo: '" + centroCultivo + '\'' +
                ", comuna: '" + comuna + '\'' +
                ", produccion: " + production +
                ", type: " + type.toString().toLowerCase() +
                ", stock: " + stock;
    }
}
