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

public class Categorias 
{
 private int id;
 private int idProducto;
 private int top_aux; 
 private String nombreCategorias;

    public Categorias() {
    }

    public Categorias(int id, int idProducto, int top_aux, String nombreCategoria) {
        this.id = id;
        this.idProducto = idProducto;
        this.top_aux = top_aux;
        this.nombreCategorias = nombreCategoria;
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

    public int getTop_aux() {
        return top_aux;
    }

    public void setTop_aux(int top_aux) {
        this.top_aux = top_aux;
    }

    public String getNombreCategoria() {
        return nombreCategorias;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategorias = nombreCategoria;
    }

} 
