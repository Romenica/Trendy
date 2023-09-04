package trendy.accesoadatos;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MINEDUCYT
 */
import java.util.*;
import java.sql.*;
import trendy.entidadesdenegocio.*;

public class ClienteDAL 
{
      static String obtenerCampos()
    {
        return "r.Id, r.IdEmpleado, r.Nombre, r,Dirección, r.Telefono, r.Dui";
    }
    
    private static String obtenerSelect(Cliente pCL)
    {
        String sql;
        sql = "Select ";
        if(pCL.getTop_aux() > 0 && 
           ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Top " + pCL .getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " From Cliente r");
        return sql;
    }
    
    private static String agregarOrderBy(Cliente pCL)
    {
        String sql = " Order by r.Id Desc";
        if(pCL.getTop_aux() > 0 && 
        ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Limit " + pCL.getTop_aux() + " ";
        }
        return sql;
    }
    
    
    public static int crear(Cliente pCL) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Insert Into Cliente(Nombre) Values(?)";
            try(PreparedStatement st = 
                ComunDB.createPreparedStatement(conn, sql);)
            {
                st.setString(1, pCL.getNombre());
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
    
    public static int modificar(Cliente pCL) throws Exception 
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Update Cliente Set Nombre = ? Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setString(1, pCL.getNombre());
                ps.setInt(2, pCL.getId());
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
    
    public static int eliminar(Cliente pCL) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Delete From Cliente Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pCL.getId());
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
    
    static int asignarDatosResultSet(Cliente pCL, ResultSet pResultSet, int pIndex) throws Exception
    {
        pIndex++;
        pCL.setId(pResultSet.getInt(pIndex));
        pIndex++;
        pCL.setNombre(pResultSet.getString(pIndex));
        return pIndex;
    }
    
    private static void obtenerDatos(PreparedStatement pPS, ArrayList<Cliente> pCL) throws Exception
    {
        try(ResultSet resultset = ComunDB.obtenerResulSet(pPS);)
        {
            while(resultset.next())
            {
                Cliente customer = new Cliente();
                asignarDatosResultSet(customer,resultset,0);
                pCL.add(customer);
            }
            resultset.close();
        }
        catch(SQLException ex)
        {
            throw ex;
        }
    }
    
    public static Cliente obtenerPorId(Cliente pCL) throws Exception
    {
        Cliente customer = new Cliente();
        ArrayList<Cliente> customers = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pCL);
            sql += " Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pCL.getId());
                obtenerDatos(ps, customers);
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
        if(customers.size() > 0)
        {
            customer = customers.get(0);
        }
        return customer;
    }

    public static ArrayList<Cliente> obtenerTodos() throws Exception
    {
        Cliente customer = new Cliente();
        ArrayList<Cliente> customers = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(new Cliente());
            sql += agregarOrderBy(new Cliente());
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                obtenerDatos(ps, customers);
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
        
        return customers;
    }
    
    static void querySelect(Cliente pCL, ComunDB.UtilQuery pUtilQuery) throws Exception
    {
        PreparedStatement statement = pUtilQuery.getStatement();
        if(pCL.getId() > 0)
        {
            pUtilQuery.AgregarWhereAnd(" r.Id = ? ");
            if(statement != null)
            {
                statement.setInt(pUtilQuery.getNumWhere(), 
                        pCL.getId());
            }
        }
        
        if(pCL.getNombre() != null && 
           pCL.getNombre().trim().isEmpty() == false)
        {
            pUtilQuery.AgregarWhereAnd(" r.Nombre Like ? ");
            if(statement != null)
            {
                statement.setString(pUtilQuery.getNumWhere(), 
                        "%" + pCL.getNombre() + "%");
            }
        }
    }
    
    public static ArrayList<Cliente> buscar(Cliente pCL) throws Exception
    {
        ArrayList<Cliente> customers = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pCL);
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = 
            comundb.new UtilQuery(sql,null,0);
            querySelect(pCL, utilQuery);
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pCL);
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pCL, utilQuery);
                obtenerDatos(ps, customers);
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
        
        return customers; 
    }   
}
