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

public class Proveedor
{
        private int id;
    private int idAdministrador;
    private String nombre;
    private String apellido;
    private String telefono;
    private String dirección;
    private int top_aux;
    private ArrayList<Administrador> administrador;

    public Proveedor() {
    }

    public Proveedor(int id, int idAdministrador, String nombre, String apellido, String telefono, String dirección, int top_aux, ArrayList<Administrador> administrador) {
        this.id = id;
        this.idAdministrador = idAdministrador;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.dirección = dirección;
        this.top_aux = top_aux;
        this.administrador = administrador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(int idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDirección() {
        return dirección;
    }

    public void setDirección(String dirección) {
        this.dirección = dirección;
    }

    public int getTop_aux() {
        return top_aux;
    }

    public void setTop_aux(int top_aux) {
        this.top_aux = top_aux;
    }

    public ArrayList<Administrador> getAdministrador() {
        return administrador;
    }

    public void setAdministrador(ArrayList<Administrador> administrador) {
        this.administrador = administrador;
    } 
}
