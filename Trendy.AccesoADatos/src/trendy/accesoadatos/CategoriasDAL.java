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

public class CategoriasDAL 
{
        static String obtenerCampos()
    {
        return "r.Id, r.Nombre, r.Apellido, r,Dirección, r.Telefono, r.Cargo";
    }
    
    private static String obtenerSelect(Empleado pEMPL)
    {
        String sql;
        sql = "Select ";
        if(pEMPL.getTop_aux() > 0 && 
           ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Top " + pEMPL .getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " From Empleado r");
        return sql;
    }
    
    private static String agregarOrderBy(Empleado pEMPL)
    {
        String sql = " Order by r.Id Desc";
        if(pEMPL.getTop_aux() > 0 && 
        ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Limit " + pEMPL.getTop_aux() + " ";
        }
        return sql;
    }
    
    
    public static int crear(Empleado pEMPL) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Insert Into Empleado(Nombre) Values(?)";
            try(PreparedStatement st = 
                ComunDB.createPreparedStatement(conn, sql);)
            {
                st.setString(1, pEMPL.getNombre());
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
    
    public static int modificar(Empleado pEMPL) throws Exception 
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Update Empleado Set Nombre = ? Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setString(1, pEMPL.getNombre());
                ps.setInt(2, pEMPL.getId());
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
    
    public static int eliminar(Empleado pEMPL) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Delete From Empleado Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pEMPL.getId());
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
    
    static int asignarDatosResultSet(Empleado pEMPL, ResultSet pResultSet, int pIndex) throws Exception
    {
        pIndex++;
        pEMPL.setId(pResultSet.getInt(pIndex));
        pIndex++;
        pEMPL.setNombre(pResultSet.getString(pIndex));
        return pIndex;
    }
    
    private static void obtenerDatos(PreparedStatement pPS, ArrayList<Empleado> pEMPL) throws Exception
    {
        try(ResultSet resultset = ComunDB.obtenerResulSet(pPS);)
        {
            while(resultset.next())
            {
                Empleado employee = new Empleado();
                asignarDatosResultSet(employee,resultset,0);
                pEMPL.add(employee);
            }
            resultset.close();
        }
        catch(SQLException ex)
        {
            throw ex;
        }
    }
    
    public static Empleado obtenerPorId(Empleado pEMPL) throws Exception
    {
        Empleado empleoyee = new Empleado();
        ArrayList<Empleado> empleoyees = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pEMPL);
            sql += " Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pEMPL.getId());
                obtenerDatos(ps, empleoyees);
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
        if(empleoyees.size() > 0)
        {
            empleoyee = empleoyees.get(0);
        }
        return empleoyee;
    }

    public static ArrayList<Empleado> obtenerTodos() throws Exception
    {
        Empleado empleoyee = new Empleado();
        ArrayList<Empleado> empleoyees = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(new Empleado());
            sql += agregarOrderBy(new Empleado());
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                obtenerDatos(ps, empleoyees);
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
        
        return empleoyees;
    }
    
    static void querySelect(Empleado pEMPL, ComunDB.UtilQuery pUtilQuery) throws Exception
    {
        PreparedStatement statement = pUtilQuery.getStatement();
        if(pEMPL.getId() > 0)
        {
            pUtilQuery.AgregarWhereAnd(" r.Id = ? ");
            if(statement != null)
            {
                statement.setInt(pUtilQuery.getNumWhere(), 
                        pEMPL.getId());
            }
        }
        
        if(pEMPL.getNombre() != null && 
           pEMPL.getNombre().trim().isEmpty() == false)
        {
            pUtilQuery.AgregarWhereAnd(" r.Nombre Like ? ");
            if(statement != null)
            {
                statement.setString(pUtilQuery.getNumWhere(), 
                        "%" + pEMPL.getNombre() + "%");
            }
        }
    }
    
    public static ArrayList<Empleado> buscar(Empleado pEMPL) throws Exception
    {
        ArrayList<Empleado> employees = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pEMPL);
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = 
            comundb.new UtilQuery(sql,null,0);
            querySelect(pEMPL, utilQuery);
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pEMPL);
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pEMPL, utilQuery);
                obtenerDatos(ps, employees);
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
        
        return employees; 
    }
}
