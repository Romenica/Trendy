/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trendy.entidadesdenegocio;

/**
 *
 * @author MINEDUCYT
 */
import java.util.ArrayList;

public class Descuento 
{
 private int id;
 private int idProducto;
 private float cantidad;
 private double precio;
 private String talla;
 private double descuento;

    public Descuento() {
    }

    public Descuento(int id, int idProducto, float cantidad, double precio, String talla, double descuento) {
        this.id = id;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.talla = talla;
        this.descuento = descuento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }
 
 
}