/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trendy.entidadesdenegocio;

/**
 *
 * @author MINEDUCYT
 */
import java.time.LocalDate;

public class Administrador 
{
 private int id;
 private String nombre;
 private String apellido;
 private String login;
 private String password;
 private byte estatus;
 private LocalDate fechaRegistro;
 private int top_aux;
 private String confirmPassword_aux;

    public Administrador() {
    }

    public Administrador(int id, String nombre, String apellido, String login, String password, byte estatus, LocalDate fechaRegistro, int top_aux, String confirmPassword_aux) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.login = login;
        this.password = password;
        this.estatus = estatus;
        this.fechaRegistro = fechaRegistro;
        this.top_aux = top_aux;
        this.confirmPassword_aux = confirmPassword_aux;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte getEstatus() {
        return estatus;
    }

    public void setEstatus(byte estatus) {
        this.estatus = estatus;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public int getTop_aux() {
        return top_aux;
    }

    public void setTop_aux(int top_aux) {
        this.top_aux = top_aux;
    }

    public String getConfirmPassword_aux() {
        return confirmPassword_aux;
    }

    public void setConfirmPassword_aux(String confirmPassword_aux) {
        this.confirmPassword_aux = confirmPassword_aux;
    }

    public class EstatusUsuario{
        public static final byte ACTIVO = 1;
        public static final byte INACTIVO = 2;
    }
}