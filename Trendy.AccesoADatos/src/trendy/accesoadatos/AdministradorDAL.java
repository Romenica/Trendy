/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trendy.accesoadatos;

/**
 *
 * @author MINEDUCYT
 */
import java.sql.*;
import java.util.*;
import java.time.LocalDate;
import trendy.entidadesdenegocio.Administrador;

public class AdministradorDAL {
    
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
    
    private static String obtenerSelect(Administrador pAdministrador)
    {
        String sql;
        sql = "Select ";
        if(pAdministrador.getTop_aux() > 0 && 
           ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Top " + pAdministrador.getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " From Administrador u");
        return sql;
    }
    
    private static String agregarOrderBy(Administrador pAdministrador)
    {
        String sql = " Order by u.Id Desc";
        if(pAdministrador.getTop_aux() > 0 && 
        ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Limit " + pAdministrador.getTop_aux() + " ";
        }
        return sql;
    }
    
    private static boolean existeLogin(Administrador pAdministrador) throws Exception {
        boolean existe = false;
        ArrayList<Administrador> administradores = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(pAdministrador);  // Obtener la consulta SELECT de la tabla Usuario
            // Concatenar a la consulta SELECT de la tabla Usuario el WHERE y el filtro para saber si existe el login
            sql += " WHERE u.Id<>? AND u.Login=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                ps.setInt(1, pAdministrador.getId());  // Agregar el parametros a la consulta donde estan el simbolo ? #1 
                ps.setString(2, pAdministrador.getLogin());  // Agregar el parametros a la consulta donde estan el simbolo ? #2 
                obtenerDatos(ps, administradores); // Llenar el ArrayList de USuario con las fila que devolvera la consulta SELECT a la tabla de Usuario
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
            Administrador administrador;
             // Se solucciono tenia valor de 1 cuando debe de ser cero
            administrador = administradores.get(0); // Si el ArrayList de Usuario trae un registro o mas obtenemos solo el primero 
            if (administrador.getId() > 0 && administrador.getLogin().equals(pAdministrador.getLogin())) {
                // Si el Id de Usuario es mayor a cero y el Login que se busco en la tabla de Usuario es igual al que solicitamos
                // en los parametros significa que el login ya existe en la base de datos y devolvemos true en la variable "existe"
                existe = true;
            }
        }
        return existe; //Devolver la variable "existe" con el valor true o false si existe o no el Login en la tabla de Usuario de la base de datos

    }
    
    public static int crear(Administrador pAdministrador) throws Exception
    {
        int result;
        String sql;
        boolean existe = existeLogin(pAdministrador);
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
                    st.setInt(1, pAdministrador.getId());
                    st.setString(2, pAdministrador.getNombre());
                    st.setString(3, pAdministrador.getApellido());
                    st.setString(4, pAdministrador.getLogin());
                    st.setString(5, encriptarMD5(pAdministrador.getPassword()));
                    st.setByte(6, pAdministrador.getEstatus());
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
    
    // Metodo para poder actualizar un registro en la tabla de Usuario
    public static int modificar(Administrador pAdministrador) throws Exception {
        int result;
        String sql;
        boolean existe = existeLogin(pAdministrador); // verificar si el usuario que se va a modificar ya existe en nuestra base de datos
        if (existe == false) {
            try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
                //Definir la consulta UPDATE a la tabla de Usuario utilizando el simbolo ? para enviar parametros
                sql = "UPDATE Usuario SET IdRol=?, Nombre=?, Apellido=?, Login=?, Estatus=? WHERE Id=?";
                try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // obtener el PreparedStatement desde la clase ComunDB
                    ps.setString(2, pAdministrador.getNombre()); // agregar el parametro a la consulta donde estan el simbolo ? #2  
                    ps.setString(3, pAdministrador.getApellido()); // agregar el parametro a la consulta donde estan el simbolo ? #3  
                    ps.setString(4, pAdministrador.getLogin()); // agregar el parametro a la consulta donde estan el simbolo ? #4  
                    ps.setByte(5, pAdministrador.getEstatus()); // agregar el parametro a la consulta donde estan el simbolo ? #5  
                    ps.setInt(6, pAdministrador.getId()); // agregar el parametro a la consulta donde estan el simbolo ? #6  
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

    // Metodo para poder eliminar un registro en la tabla de Usuario
    public static int eliminar(Administrador pAdministrador) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            sql = "DELETE FROM Administrador WHERE Id=?"; //definir la consulta DELETE a la tabla de Usuario utilizando el simbolo ? para enviar parametros
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {  // obtener el PreparedStatement desde la clase ComunDB
                ps.setInt(1, pAdministrador.getId()); // agregar el parametro a la consulta donde estan el simbolo ? #1 
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
    
    static int asignarDatosResultSet(Administrador pAdministrador, ResultSet pResultSet, int pIndex) throws Exception {
        //  SELECT u.Id(indice 1), u.IdRol(indice 2), u.Nombre(indice 3), u.Apellido(indice 4), u.Login(indice 5), 
        // u.Estatus(indice 6), u.FechaRegistro(indice 7) * FROM Usuario
        pIndex++;
        pAdministrador.setId(pResultSet.getInt(pIndex)); // index 1
        pIndex++;
        pAdministrador.setNombre(pResultSet.getString(pIndex)); // index 3
        pIndex++;
        pAdministrador.setApellido(pResultSet.getString(pIndex)); // index 4
        pIndex++;
        pAdministrador.setLogin(pResultSet.getString(pIndex)); // index 5
        pIndex++;
        pAdministrador.setEstatus(pResultSet.getByte(pIndex)); // index 6
        pIndex++;
        pAdministrador.setFechaRegistro(pResultSet.getDate(pIndex).toLocalDate()); // index 7
        return pIndex;
    }

     private static void obtenerDatos(PreparedStatement pPS, ArrayList<Administrador> pAdministrador) throws Exception {
        try (ResultSet resultSet = ComunDB.obtenerResulSet(pPS);) { // obtener el ResultSet desde la clase ComunDB
            while (resultSet.next()) { // Recorrer cada una de la fila que regresa la consulta  SELECT de la tabla Usuario
                Administrador administrador = new Administrador();
                // Llenar las propiedaddes de la Entidad Usuario con los datos obtenidos de la fila en el ResultSet
                asignarDatosResultSet(administrador, resultSet, 0);
                pAdministrador.add(administrador); // agregar la entidad Usuario al ArrayList de Usuario
            }
            resultSet.close(); // cerrar el ResultSet
        } catch (SQLException ex) {
            throw ex;// enviar al siguiente metodo el error al obtener ResultSet de la clase ComunDB   en el caso que suceda 
        }
    }
     
    // Metodo para  ejecutar el ResultSet de la consulta SELECT a la tabla de Usuario y JOIN a la tabla de Rol
    private static void obtenerDatosIncluirAdministrador(PreparedStatement pPS, ArrayList<Administrador> pAdministrador) throws Exception {
        try (ResultSet resultSet = ComunDB.obtenerResulSet(pPS);) { // obtener el ResultSet desde la clase ComunDB
            while (resultSet.next()) { // Recorrer cada una de la fila que regresa la consulta  SELECT de la tabla Usuario JOIN a la tabla de Rol
                Administrador administrador = new Administrador();
            }
            resultSet.close(); // cerrar el ResultSet
        } catch (SQLException ex) {
            throw ex; // enviar al siguiente metodo el error al obtener ResultSet de la clase ComunDB   en el caso que suceda 
        }
    }
    
     // Metodo para obtener por Id un registro de la tabla de Usuario 
    public static Administrador obtenerPorId(Administrador pAdministrador) throws Exception {
        Administrador administrador = new Administrador();
        ArrayList<Administrador> administradores = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(pAdministrador); // obtener la consulta SELECT de la tabla Usuario
             // Concatenar a la consulta SELECT de la tabla Usuario el WHERE  para comparar el campo Id
            sql += " WHERE u.Id=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // obtener el PreparedStatement desde la clase ComunDB
                ps.setInt(1, pAdministrador.getId()); // agregar el parametro a la consulta donde estan el simbolo ? #1 
                obtenerDatos(ps, administradores); // Llenar el ArrayList de Usuario con las fila que devolvera la consulta SELECT a la tabla de Usuario
                ps.close(); // cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex; // enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // cerrar la conexion a la base de datos
        }
        catch (SQLException ex) {
            throw ex; // enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        if (administradores.size() > 0) { // verificar si el ArrayList de Usuario trae mas de un registro en tal caso solo debe de traer uno
            administrador = administradores.get(0); // Si el ArrayList de Usuario trae un registro o mas obtenemos solo el primero
        }
        return administrador; // devolver el Usuario encontrado por Id 
    }
    
    // Metodo para obtener todos los registro de la tabla de Usuario
    public static ArrayList<Administrador> obtenerTodos() throws Exception {
        ArrayList<Administrador> administradores;
        administradores = new ArrayList<>();
        try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(new Administrador()); // obtener la consulta SELECT de la tabla Usuario
            sql += agregarOrderBy(new Administrador()); // concatenar a la consulta SELECT de la tabla Usuario el ORDER BY por Id 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // obtener el PreparedStatement desde la clase ComunDB
                obtenerDatos(ps, administradores); // Llenar el ArrayList de Usuario con las fila que devolvera la consulta SELECT a la tabla de Usuario
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
    static void querySelect(Administrador pAdministrador, ComunDB.UtilQuery pUtilQuery) throws SQLException {
        PreparedStatement statement = pUtilQuery.getStatement(); // obtener el PreparedStatement al cual aplicar los parametros
        if (pAdministrador.getId() > 0) { // verificar si se va incluir el campo Id en el filtro de la consulta SELECT de la tabla de Usuario
            pUtilQuery.AgregarWhereAnd(" u.Id=? "); // agregar el campo Id al filtro de la consulta SELECT y agregar el WHERE o AND
            if (statement != null) {
                 // agregar el parametro del campo Id a la consulta SELECT de la tabla de Usuario
                statement.setInt(pUtilQuery.getNumWhere(), pAdministrador.getId());
            }
        }
        
        // verificar si se va incluir el campo Nombre en el filtro de la consulta SELECT de la tabla de Usuario
        if (pAdministrador.getNombre() != null && pAdministrador.getNombre().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" u.Nombre LIKE ? "); // agregar el campo Nombre al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) {
                 // agregar el parametro del campo Nombre a la consulta SELECT de la tabla de Usuario
                statement.setString(pUtilQuery.getNumWhere(), "%" + pAdministrador.getNombre() + "%");
            }
        }
        // Verificar si se va incluir el campo Apellido en el filtro de la consulta SELECT de la tabla de Usuario
        if (pAdministrador.getApellido() != null && pAdministrador.getApellido().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" u.Apellido LIKE ? "); // agregar el campo Apellido al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) {
                 // agregar el parametro del campo Apellido a la consulta SELECT de la tabla de Usuario
                statement.setString(pUtilQuery.getNumWhere(), "%" + pAdministrador.getApellido() + "%");
            }
        }
        // Verificar si se va incluir el campo Login en el filtro de la consulta SELECT de la tabla de Usuario
        if (pAdministrador.getLogin() != null && pAdministrador.getLogin().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" u.Login=? "); // agregar el campo Login al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) {
                 // agregar el parametro del campo Login a la consulta SELECT de la tabla de Usuario
                statement.setString(pUtilQuery.getNumWhere(), pAdministrador.getLogin());
            }
        }
        // Verificar si se va incluir el campo Estatus en el filtro de la consulta SELECT de la tabla de Usuario
        if (pAdministrador.getEstatus() > 0) {
            pUtilQuery.AgregarWhereAnd(" u.Estatus=? "); // agregar el campo Estatus al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) {
                 // agregar el parametro del campo Estatus a la consulta SELECT de la tabla de Usuario
                statement.setInt(pUtilQuery.getNumWhere(), pAdministrador.getEstatus());
            }
        }
    }

     // Metodo para obtener todos los registro de la tabla de Usuario que cumplan con los filtros agregados 
     // a la consulta SELECT de la tabla de Usuario 
    public static ArrayList<Administrador> buscar(Administrador pAdministrador) throws Exception {
        ArrayList<Administrador> usuarios = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(pAdministrador); // obtener la consulta SELECT de la tabla Usuario
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = comundb.new UtilQuery(sql, null, 0);
            querySelect(pAdministrador, utilQuery); // Asignar el filtro a la consulta SELECT de la tabla de Usuario 
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pAdministrador); // Concatenar a la consulta SELECT de la tabla Usuario el ORDER BY por Id
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // obtener el PreparedStatement desde la clase ComunDB
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pAdministrador, utilQuery); // Asignar los parametros al PreparedStatement de la consulta SELECT de la tabla de Usuario
                obtenerDatos(ps, usuarios); // Llenar el ArrayList de Usuario con las fila que devolvera la consulta SELECT a la tabla de Usuario
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex; // Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // Cerrar la conexion a la base de datos
        } 
        catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        return usuarios; // Devolver el ArrayList de Usuario
    }
    
    // Metodo para verificar si el Usuario puede ser autorizado en el sistema
    // comparando el Login, Password, Estatus en la base de datos


    public static Administrador login(Administrador pAdministrador) throws Exception
    {
        Administrador administrador = new Administrador();
        ArrayList<Administrador> administradores = new ArrayList();
        String password = encriptarMD5(pAdministrador.getPassword());
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pAdministrador);
            sql += " Where u.Login = ? And u.Password = ? And "
                    + "u.Estatus = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setString(1, pAdministrador.getLogin());
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
            administrador = administradores.get(0);
        }
        return administrador;
    }
    
    // Metodo para cambiar el password de un Usuario el cual solo se puede cambiar si envia el password actual correctamente
    public static int cambiarPassword(Administrador pAdministrador, String pPasswordAnt) throws Exception {
        int result;
        String sql;
        Administrador administradorAnt = new Administrador();
        administradorAnt.setLogin(pAdministrador.getLogin());
        administradorAnt.setPassword(pPasswordAnt);
        Administrador administradorAut = login(administradorAnt); // Obtenemos el Usuario autorizado validandolo en el metodo de login
        // Si el usuario que retorno el metodo de login tiene el Id mayor a cero y el Login es igual que el Login del Usuario que viene
        // en el parametro es un Usuario Autorizado
        if (administradorAut.getId() > 0 && administradorAut.getLogin().equals(pAdministrador.getLogin())) {
            try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
                sql = "UPDATE Administrador SET Password=? WHERE Id=?"; // Crear la consulta Update a la tabla de Usuario para poder modificar el Password
                try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                    // Agregar el parametro a la consulta donde estan el simbolo ? #1 pero antes encriptar el password para enviarlo encriptado
                    ps.setString(1, encriptarMD5(pAdministrador.getPassword())); //
                    ps.setInt(2, pAdministrador.getId()); // Agregar el parametro a la consulta donde estan el simbolo ? #2 
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
            // Enviar la excepcion en el caso que el usuario que intenta modificar el password ingresa un password incorrecto
            throw new RuntimeException("El password actual es incorrecto");
        }
        return result; // Retornar el numero de fila afectadas en el UPDATE en la base de datos 
    }
}
