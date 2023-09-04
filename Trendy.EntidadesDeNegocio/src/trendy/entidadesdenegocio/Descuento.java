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
 private String cantidad;
 private String precio;
 private String talla;
 private String descuento;
 private int top_aux;
 private ArrayList<Producto> productos;

    public Descuento() {
    }

    public Descuento(int id, int idProducto, String cantidad, String precio, String talla, String descuento, int top_aux, ArrayList<Producto> productos) {
        this.id = id;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.talla = talla;
        this.descuento = descuento;
        this.top_aux = top_aux;
        this.productos = productos;
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

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public int getTop_aux() {
        return top_aux;
    }

    public void setTop_aux(int top_aux) {
        this.top_aux = top_aux;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }

   
}
