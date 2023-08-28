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
import trendy.entidadesdenegocio.*;


public class EmpleadoDAL {
    
       static String obtenerCampos()
    {
        return "r.Id, r.Nombre, r.IdApellido, r.Dirección, r.Telefono, r.Cargo";
    }
    
    private static String obtenerSelect(Empleado pEmpleado) /*pEmpleado ase referencia aun parametro*/
    {
        String sql;
        sql = "Select ";
        if(pEmpleado.getTop_aux() > 0 && 
           ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Top " + pEmpleado.getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " From Empleado r");
        return sql;
    } 
    
     private static String agregarOrderBy(Empleado pEmpleado)
    {
        String sql = " Order by r.Id Desc";
        if(pEmpleado.getTop_aux() > 0 && 
        ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Limit " + pEmpleado.getTop_aux() + " ";
        }
        return sql;
    }
    
    public static int crear(Empleado pEmpleado) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Insert Into Empleado(Nombre) Values(?)";
            try(PreparedStatement st = 
                ComunDB.createPreparedStatement(conn, sql);)
            {
                st.setString(1, pEmpleado.getNombre());
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
        return result;
    }
    
    public static int modificar(Empleado pEmpleado) throws Exception 
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Update Empleado Set Nombre = ? Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setString(1, pEmpleado.getNombre());
                ps.setInt(2, pEmpleado.getId());
                result = ps.executeUpdate();
                ps.close();
            }
            catch(Exception ex)
            {
                throw ex;
            }
        }
        catch(SQLException ex)
        {
            throw ex;
        }
        return result;
    }
    
    public static int eliminar(Empleado pEmpleado) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Delete From Empleado Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pEmpleado.getId());
                result = ps.executeUpdate();
                ps.close();
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
        return result;
    }
    
    static int asignarDatosResultSet(Empleado pEmpleado, ResultSet pResultSet, int pIndex) throws Exception
    {
        pIndex++;
        pEmpleado.setId(pResultSet.getInt(pIndex));
        pIndex++;
        pEmpleado.setNombre(pResultSet.getString(pIndex));
        return pIndex;
    }
    
    private static void obtenerDatos(PreparedStatement pPS, ArrayList<Empleado> pEmpleado) throws Exception
    {
        try(ResultSet resultset = ComunDB.obtenerResulSet(pPS);)
        {
            while(resultset.next())
            {
                Empleado empleados = new Empleado();
                asignarDatosResultSet(empleados,resultset,0);
                pEmpleado.add(empleados);
            }
            resultset.close();
        }
        catch(SQLException ex)
        {
            throw ex;
        }
    }
    
    public static Empleado obtenerPorId(Empleado pEmpleado) throws Exception
    {
        Empleado empleado = new Empleado();
        ArrayList<Empleado> empleados = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pEmpleado);
            sql += " Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pEmpleado.getId());
                obtenerDatos(ps, empleados);
                ps.close();
            }
            catch(Exception ex)
            {
                throw ex;
            }
        }
        catch(SQLException ex)
        {
            throw ex;
        }
        if(empleados.size() > 0)
        {
            empleado = empleados.get(0);
        }
        return empleado;
    }

    public static ArrayList<Empleado> obtenerTodos() throws Exception
    {
        Empleado empleado = new Empleado();
        ArrayList<Empleado> empleados = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(new Empleado());
            sql += agregarOrderBy(new Empleado());
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                obtenerDatos(ps, empleados);
                ps.close();
            }
            catch(Exception ex)
            {
                throw ex;
            }
        }
        catch(SQLException ex)
        {
            throw ex;
        }
        
        return empleados;
    }
    
    static void querySelect(Empleado pEmpleado, ComunDB.UtilQuery pUtilQuery) throws Exception
    {
        PreparedStatement statement = pUtilQuery.getStatement();
        if(pEmpleado.getId() > 0)
        {
            pUtilQuery.AgregarWhereAnd(" r.Id = ? ");
            if(statement != null)
            {
                statement.setInt(pUtilQuery.getNumWhere(), 
                        pEmpleado.getId());
            }
        }
        
        if(pEmpleado.getNombre() != null && 
           pEmpleado.getNombre().trim().isEmpty() == false)
        {
            pUtilQuery.AgregarWhereAnd(" r.Nombre Like ? ");
            if(statement != null)
            {
                statement.setString(pUtilQuery.getNumWhere(), 
                        "%" + pEmpleado.getNombre() + "%");
            }
        }
    }
    
    public static ArrayList<Empleado> buscar(Empleado pEmpleado) throws Exception
    {
        ArrayList<Empleado> empleado = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pEmpleado);
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = 
            comundb.new UtilQuery(sql,null,0);
            querySelect(pEmpleado, utilQuery);
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pEmpleado);
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pEmpleado, utilQuery);
                obtenerDatos(ps, empleado);
                ps.close();
            }
            catch(Exception ex)
            {
                throw ex;
            }
        }
        catch(SQLException ex)
        {
            throw ex;
        }
        
        return empleado; 
    }  
}
