package org.salmonttapp.model;

public class Data {
    private String centroCultivo;
    private String comuna;
    private int produccion;
    private Type type;
    private int stock;

    public Data(String centroCultivo, String comuna, int produccion, Type type, int stock) {
        this.centroCultivo = centroCultivo;
        this.comuna = comuna;
        this.produccion = produccion;
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

    public int getProduccion() {
        return produccion;
    }

    public void setProduccion(int produccion) {
        this.produccion = produccion;
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
                ", produccion: " + produccion +
                ", type: " + type.toString().toLowerCase() +
                ", stock: " + stock;
    }
}
