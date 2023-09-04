/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trendy.accesoadatos;

/**
 *
 * @author MINEDUCYT
 */
import java.util.*;
import java.sql.*;
import java.time.LocalDate;
import trendy.entidadesdenegocio.*;

public class AdministradorDAL
{
    public static String encriptarMD5(String txt) throws Exception 
   {
    try {
      StringBuffer sb;
      java.security.MessageDigest md = java.security.MessageDigest
            .getInstance("MD5");
      byte[] array = md.digest(txt.getBytes());
      sb = new StringBuffer();
      for (int i = 0; i < array.length; ++i) 
      {
      sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
      .substring(1, 3));
     }
     return sb.toString();
    } catch (java.security.NoSuchAlgorithmException ex) {
    throw ex;
    }
  }
    
    static String obtenerCampos()
    {
        return "u.Id, u.IdAdministrador, u.Nombre, u.Apellido, u.Login"
                + ", u.Estatus, u.FechaRegistro";
    }
    
    private static String obtenerSelect(Administrador pAdmin)
    {
        String sql;
        sql = "Select ";
        if(pAdmin.getTop_aux() > 0 && 
           ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Top " + pAdmin.getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " From Usuario u");
        return sql;
    }
    
    private static String agregarOrderBy(Administrador pAdmin)
    {
        String sql = " Order by u.Id Desc";
        if(pAdmin.getTop_aux() > 0 && 
        ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Limit " + pAdmin.getTop_aux() + " ";
        }
        return sql;
    }
    
    private static boolean existeLogin(Administrador pAdmin) throws Exception {
        boolean existe = false;
        ArrayList<Administrador> administradores = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(pAdmin);  // Obtener la consulta SELECT de la tabla Administrador
            // Concatenar a la consulta SELECT de la tabla Usuario el WHERE y el filtro para saber si existe el login
            sql += " WHERE u.Id<>? AND u.Login=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                ps.setInt(1, pAdmin.getId());  // Agregar el parametros a la consulta donde estan el simbolo ? #1 
                ps.setString(2, pAdmin.getLogin());  // Agregar el parametros a la consulta donde estan el simbolo ? #2 
                obtenerDatos(ps, administradores); // Llenar el ArrayList de Administrador con las fila que devolvera la consulta SELECT a la tabla de Administrador
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex;  // Enviar al siguiente metodo el error al ejecutar PreparedStatement el en el caso que suceda
            }
            conn.close(); // Cerrar la conexion a la base de datos
        }
        catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        if (administradores.size() > 0) { // Verificar si el ArrayList de Usuario trae mas de un registro en tal caso solo debe de traer uno
            Administrador admin;
             // Se solucciono tenia valor de 1 cuando debe de ser cero
            admin = administradores.get(0); // Si el ArrayList de Administrador trae un registro o mas obtenemos solo el primero 
            if (admin.getId() > 0 && admin.getLogin().equals(pAdmin.getLogin())) {
                // Si el Id de Usuario es mayor a cero y el Login que se busco en la tabla de Administrador es igual al que solicitamos
                // en los parametros significa que el login ya existe en la base de datos y devolvemos true en la variable "existe"
                existe = true;
            }
        }
        return existe; //Devolver la variable "existe" con el valor true o false si existe o no el Login en la tabla de Administrador de la base de datos

    }
    
    public static int crear(Administrador pAdmin) throws Exception
    {
        int result;
        String sql;
        boolean existe = existeLogin(pAdmin);
        if(existe == false)
        {
            try(Connection conn = ComunDB.obtenerConexion();)
            {
                sql = "Insert Into Administrador(IdAdministrador, Nombre, Apellido, Login"
                        + ", Password, Estatus, FechaRegistro) Values"
                        + "(?,?,?,?,?,?,?)";
                try(PreparedStatement st = 
                    ComunDB.createPreparedStatement(conn, sql);)
                {
                    st.setInt(1, pAdmin.getId());
                    st.setString(2, pAdmin.getNombre());
                    st.setString(3, pAdmin.getApellido());
                    st.setString(4, pAdmin.getLogin());
                    st.setString(5, encriptarMD5(pAdmin.getPassword()));
                    st.setByte(6, pAdmin.getEstatus());
                    st.setDate(7, java.sql.Date.valueOf(LocalDate.now()));
                    result = st.executeUpdate();
                    st.close();
                }
                catch(SQLException ex)
                {
                    throw ex;
                }
            }
            catch(SQLException ex)
            {
                throw ex;
            }
        }
        else
        {
            result=0;
            throw new RuntimeException("Login ya Existe");
        }
        return result;
    }
    
    // Metodo para poder actualizar un registro en la tabla de Administrador 
    public static int modificar(Administrador pAdmin) throws Exception {
        int result;
        String sql;
        boolean existe = existeLogin(pAdmin); // verificar si el Administrador que se va a modificar ya existe en nuestra base de datos
        if (existe == false) {
            try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
                //Definir la consulta UPDATE a la tabla de Administrador utilizando el simbolo ? para enviar parametros
                sql = "UPDATE Administrador ?, Nombre=?, Apellido=?, Login=?, Estatus=? WHERE Id=?";
                try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // obtener el PreparedStatement desde la clase ComunDB
                    ps.setString(1, pAdmin.getNombre()); // agregar el parametro a la consulta donde estan el simbolo ? #2  
                    ps.setString(2, pAdmin.getApellido()); // agregar el parametro a la consulta donde estan el simbolo ? #3  
                    ps.setString(3, pAdmin.getLogin()); // agregar el parametro a la consulta donde estan el simbolo ? #4  
                    ps.setByte(4, pAdmin.getEstatus()); // agregar el parametro a la consulta donde estan el simbolo ? #5  
                    ps.setInt(5, pAdmin.getId()); // agregar el parametro a la consulta donde estan el simbolo ? #6  
                    result = ps.executeUpdate(); // ejecutar la consulta UPDATE en la base de datos
                    ps.close(); // cerrar el PreparedStatement
                } catch (SQLException ex) {
                    throw ex; // enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda 
                }
                conn.close(); // cerrar la conexion a la base de datos
            } 
            catch (SQLException ex) {
                throw ex; // enviar al siguiente metodo el error al obtener la conexion en el caso que suceda 
            }
        } else {
            result = 0;
            throw new RuntimeException("Login ya existe"); // enviar una exception para notificar que el login existe
        }
        return result; // Retornar el numero de fila afectadas en el UPDATE en la base de datos 
    }

    // Metodo para poder eliminar un registro en la tabla de Administrador
    public static int eliminar(Administrador pAdmin) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            sql = "DELETE FROM Administrador WHERE Id=?"; //definir la consulta DELETE a la tabla de Administrador utilizando el simbolo ? para enviar parametros
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {  // obtener el PreparedStatement desde la clase ComunDB
                ps.setInt(1, pAdmin.getId()); // agregar el parametro a la consulta donde estan el simbolo ? #1 
                result = ps.executeUpdate(); // ejecutar la consulta DELETE en la base de datos
                ps.close(); // cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex; // enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // cerrar la conexion a la base de datos
        }
        catch (SQLException ex) {
            throw ex;  // enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda 
        }
        return result; // Retornar el numero de fila afectadas en el DELETE en la base de datos 
    }
    
    static int asignarDatosResultSet(Administrador pAdmin, ResultSet pResultSet, int pIndex) throws Exception {
        // u.Estatus(indice 5), u.FechaRegistro(indice 7) * FROM Administrador
        pIndex++;
        pAdmin.setId(pResultSet.getInt(pIndex)); // index 1
        pIndex++;
        pAdmin.setNombre(pResultSet.getString(pIndex)); // index 2
        pIndex++;
        pAdmin.setApellido(pResultSet.getString(pIndex)); // index 3
        pIndex++;
        pAdmin.setLogin(pResultSet.getString(pIndex)); // index 4
        pIndex++;
        pAdmin.setEstatus(pResultSet.getByte(pIndex)); // index 5 
        pIndex++;
        pAdmin.setFechaRegistro(pResultSet.getDate(pIndex).toLocalDate()); // index 6
        return pIndex;
    }

     private static void obtenerDatos(PreparedStatement pPS, ArrayList<Administrador> pAdmin) throws Exception {
        try (ResultSet resultSet = ComunDB.obtenerResulSet(pPS);) { // obtener el ResultSet desde la clase ComunDB
            while (resultSet.next()) { // Recorrer cada una de la fila que regresa la consulta  SELECT de la tabla Administrador
                Administrador admin = new Administrador();
                // Llenar las propiedaddes de la Entidad Administrador con los datos obtenidos de la fila en el ResultSet
                asignarDatosResultSet(admin, resultSet, 0);
                pAdmin.add(admin); // agregar la entidad Usuario al ArrayList de Administrador
            }
            resultSet.close(); // cerrar el ResultSet
        } catch (SQLException ex) {
            throw ex;// enviar al siguiente metodo el error al obtener ResultSet de la clase ComunDB   en el caso que suceda 
        }
    }
    
     // Metodo para obtener por Id un registro de la tabla de Administrador 
    public static Administrador obtenerPorId(Administrador pAdmin) throws Exception {
        Administrador admin = new Administrador();
        ArrayList<Administrador> administradores = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(pAdmin); // obtener la consulta SELECT de la tabla Administrador
             // Concatenar a la consulta SELECT de la tabla Administrador el WHERE  para comparar el campo Id
            sql += " WHERE u.Id=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // obtener el PreparedStatement desde la clase ComunDB
                ps.setInt(1, pAdmin.getId()); // agregar el parametro a la consulta donde estan el simbolo ? #1 
                obtenerDatos(ps, administradores); // Llenar el ArrayList de Usuario con las fila que devolvera la consulta SELECT a la tabla de Administrador
                ps.close(); // cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex; // enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // cerrar la conexion a la base de datos
        }
        catch (SQLException ex) {
            throw ex; // enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        if (administradores.size() > 0) { // verificar si el ArrayList de Administradores trae mas de un registro en tal caso solo debe de traer uno
            admin = administradores.get(0); // Si el ArrayList de Usuario trae un registro o mas obtenemos solo el primero
        }
        return admin; // devolver el Administradores encontrado por Id 
    }
    
    // Metodo para obtener todos los registro de la tabla de Administrador
    public static ArrayList<Administrador> obtenerTodos() throws Exception {
        ArrayList<Administrador> administradores;
        administradores = new ArrayList<>();
        try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(new Administrador()); // obtener la consulta SELECT de la tabla Administrador
            sql += agregarOrderBy(new Administrador()); // concatenar a la consulta SELECT de la tabla Administrador el ORDER BY por Id 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // obtener el PreparedStatement desde la clase ComunDB
                obtenerDatos(ps, administradores); // Llenar el ArrayList de Administrador con las fila que devolvera la consulta SELECT a la tabla de Administrador
                ps.close(); // cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex; // enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // cerrar la conexion a la base de datos
        }
        catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        return administradores; // devolver el ArrayList de Usuario
    }

    // Metodo para asignar los filtros de la consulta SELECT de la tabla de Usuario de forma dinamica
    static void querySelect(Administrador pAdmin, ComunDB.UtilQuery pUtilQuery) throws SQLException {
        PreparedStatement statement = pUtilQuery.getStatement(); // obtener el PreparedStatement al cual aplicar los parametros
        if (pAdmin.getId() > 0) { // verificar si se va incluir el campo Id en el filtro de la consulta SELECT de la tabla de Usuario
            pUtilQuery.AgregarWhereAnd(" u.Id=? "); // agregar el campo Id al filtro de la consulta SELECT y agregar el WHERE o AND
            if (statement != null) {
                 // agregar el parametro del campo Id a la consulta SELECT de la tabla de Usuario
                statement.setInt(pUtilQuery.getNumWhere(), pAdmin.getId());
            }
        }
     
        // verificar si se va incluir el campo Nombre en el filtro de la consulta SELECT de la tabla de Administrador
        if (pAdmin.getNombre() != null && pAdmin.getNombre().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" u.Nombre LIKE ? "); // agregar el campo Nombre al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) {
                 // agregar el parametro del campo Nombre a la consulta SELECT de la tabla de Administrador
                statement.setString(pUtilQuery.getNumWhere(), "%" + pAdmin.getNombre() + "%");
            }
        }
        // Verificar si se va incluir el campo Apellido en el filtro de la consulta SELECT de la tabla de Administrador
        if (pAdmin.getApellido() != null && pAdmin.getApellido().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" u.Apellido LIKE ? "); // agregar el campo Apellido al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) {
                 // agregar el parametro del campo Apellido a la consulta SELECT de la tabla de Administrador
                statement.setString(pUtilQuery.getNumWhere(), "%" + pAdmin.getApellido() + "%");
            }
        }
        // Verificar si se va incluir el campo Login en el filtro de la consulta SELECT de la tabla de Administrador
        if (pAdmin.getLogin() != null && pAdmin.getLogin().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" u.Login=? "); // agregar el campo Login al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) {
                 // agregar el parametro del campo Login a la consulta SELECT de la tabla de Administrador
                statement.setString(pUtilQuery.getNumWhere(), pAdmin.getLogin());
            }
        }
        // Verificar si se va incluir el campo Estatus en el filtro de la consulta SELECT de la tabla de Administrador
        if (pAdmin.getEstatus() > 0) {
            pUtilQuery.AgregarWhereAnd(" u.Estatus=? "); // agregar el campo Estatus al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) {
                 // agregar el parametro del campo Estatus a la consulta SELECT de la tabla de Administrador
                statement.setInt(pUtilQuery.getNumWhere(), pAdmin.getEstatus());
            }
        }
    }

     // Metodo para obtener todos los registro de la tabla de Usuario que cumplan con los filtros agregados 
     // a la consulta SELECT de la tabla de Usuario 
    public static ArrayList<Administrador> buscar(Administrador pAdmin) throws Exception {
        ArrayList<Administrador> usuarios = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(pAdmin); // obtener la consulta SELECT de la tabla Usuario
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = comundb.new UtilQuery(sql, null, 0);
            querySelect(pAdmin, utilQuery); // Asignar el filtro a la consulta SELECT de la tabla de Usuario 
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pAdmin); // Concatenar a la consulta SELECT de la tabla Usuario el ORDER BY por Id
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // obtener el PreparedStatement desde la clase ComunDB
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pAdmin, utilQuery); // Asignar los parametros al PreparedStatement de la consulta SELECT de la tabla de Administrador
                obtenerDatos(ps, usuarios); // Llenar el ArrayList de Usuario con las fila que devolvera la consulta SELECT a la tabla de Administrador
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex; // Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // Cerrar la conexion a la base de datos
        } 
        catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        return usuarios; // Devolver el ArrayList de Administrador
    }
    
    // Metodo para verificar si el Administrador puede ser autorizado en el sistema
    // comparando el Login, Password, Estatus en la base de datos


    public static Administrador login(Administrador pAdmin) throws Exception
    {
        Administrador admin = new Administrador();
        ArrayList<Administrador> administradores = new ArrayList();
        String password = encriptarMD5(pAdmin.getPassword());
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pAdmin);
            sql += " Where u.Login = ? And u.Password = ? And "
                    + "u.Estatus = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setString(1, pAdmin.getLogin());
                ps.setString(2, password);
                ps.setByte(3, Byte.parseByte("1"));
                obtenerDatos(ps, administradores);
                ps.close();
            }
            catch(SQLException ex)
            {
                throw ex;
            }
            conn.close();
        }
        catch(SQLException ex)
        {
            throw ex;
        }
        if(administradores.size() > 0)
        {
            admin = administradores.get(0);
        }
        return admin;
    }
    
    // Metodo para cambiar el password de un Administrador el cual solo se puede cambiar si envia el password actual correctamente
    public static int cambiarPassword(Administrador pAdmin, String pPasswordAnt) throws Exception {
        int result;
        String sql;
        Administrador adminAnt = new Administrador();
        adminAnt.setLogin(pAdmin.getLogin());
        adminAnt.setPassword(pPasswordAnt);
        Administrador adminAut = login(adminAnt); // Obtenemos el Usuario autorizado validandolo en el metodo de login
        // Si el usuario que retorno el metodo de login tiene el Id mayor a cero y el Login es igual que el Login del Usuario que viene
        // en el parametro es un Usuario Autorizado
        if (adminAut.getId() > 0 && adminAut.getLogin().equals(pAdmin.getLogin())) {
            try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
                sql = "UPDATE Administrador SET Password=? WHERE Id=?"; // Crear la consulta Update a la tabla de Usuario para poder modificar el Password
                try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                    // Agregar el parametro a la consulta donde estan el simbolo ? #1 pero antes encriptar el password para enviarlo encriptado
                    ps.setString(1, encriptarMD5(pAdmin.getPassword())); //
                    ps.setInt(2, pAdmin.getId()); // Agregar el parametro a la consulta donde estan el simbolo ? #2 
                    result = ps.executeUpdate(); // Ejecutar la consulta UPDATE en la base de datos
                    ps.close(); // Cerrar el PreparedStatement
                } catch (SQLException ex) {
                    throw ex; // Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
                }
                conn.close(); // Cerrar la conexion a la base de datos
            }
            catch (SQLException ex) {
                throw ex;// Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
            }
        } else {
            result = 0;
            // Enviar la excepcion en el caso que el Administrador que intenta modificar el password ingresa un password incorrecto
            throw new RuntimeException("El password actual es incorrecto");
        }
        return result; // Retornar el numero de fila afectadas en el UPDATE en la base de datos 
    }

    // Metodo para obtener todos los registro de la tabla de Usuario que cumplan con los filtros agregados 
     // a la consulta SELECT de la tabla de Administrador 
    public static ArrayList<Administrador> buscarIncluirAdministrador(Administrador pAdmin) throws Exception {
        ArrayList<Administrador> administradores = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = "SELECT "; // Iniciar la variables para el String de la consulta SELECT
            if (pAdmin.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER) {
                sql += "TOP " + pAdmin.getTop_aux() + " "; // Agregar el TOP en el caso que se este utilizando SQL SERVER
            }
            sql += obtenerCampos(); // Obtener los campos de la tabla de Administrador que iran en el SELECT
            sql += ",";
            sql += " FROM Administrador u";
            sql += " JOIN Rol r on (u.IdRol=r.Id)"; // agregar el join para unir la tabla de Usuario con Rol
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = comundb.new UtilQuery(sql, null, 0);
            querySelect(pAdmin, utilQuery); // Asignar el filtro a la consulta SELECT de la tabla de Usuario 
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pAdmin); // Concatenar a la consulta SELECT de la tabla Usuario el ORDER BY por Id
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pAdmin, utilQuery); // Asignar los parametros al PreparedStatement de la consulta SELECT de la tabla de Administrador
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex;// Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // Cerrar la conexion a la base de datos
        } catch (SQLException ex) {
            throw ex;// Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        return administradores; // Devolver el ArrayList de Usuario
    }  
}
