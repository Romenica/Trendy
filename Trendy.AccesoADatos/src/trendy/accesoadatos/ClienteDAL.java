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

public class ClienteDAL {
    
     static String obtenerCampos()
    {
        return "r.Id, r.IdEmpleado, r.Nombre, r.Dirección, r.Telefono, r.Dui";
    }
    
    private static String obtenerSelect(Cliente pCliente) /*pCliente ase referencia aun parametro*/
    {
        String sql;
        sql = "Select ";
        if(pCliente.getTop_aux() > 0 && 
           ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Top " + pCliente.getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " From Cliente r");
        return sql;
    } 
    
     private static String agregarOrderBy(Cliente pCliente)
    {
        String sql = " Order by r.Id Desc";
        if(pCliente.getTop_aux() > 0 && 
        ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Limit " + pCliente.getTop_aux() + " ";
        }
        return sql;
    }
    
    public static int crear(Cliente pCliente) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Insert Into Cliente(Nombre) Values(?)";
            try(PreparedStatement st = 
                ComunDB.createPreparedStatement(conn, sql);)
            {
                st.setString(1, pCliente.getNombre());
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
    
    public static int modificar(Cliente pCliente) throws Exception 
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Update Cliente Set Nombre = ? Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setString(1, pCliente.getNombre());
                ps.setInt(2, pCliente.getId());
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
    
    public static int eliminar(Cliente pCliente) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Delete From Cliente Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pCliente.getId());
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
    
    static int asignarDatosResultSet(Cliente pCliente, ResultSet pResultSet, int pIndex) throws Exception
    {
        pIndex++;
        pCliente.setId(pResultSet.getInt(pIndex));
        pIndex++;
        pCliente.setNombre(pResultSet.getString(pIndex));
        return pIndex;
    }
    
    private static void obtenerDatos(PreparedStatement pPS, ArrayList<Cliente> pCliente) throws Exception
    {
        try(ResultSet resultset = ComunDB.obtenerResulSet(pPS);)
        {
            while(resultset.next())
            {
                Cliente clientes = new Cliente();
                asignarDatosResultSet(clientes,resultset,0);
                pCliente.add(clientes);
            }
            resultset.close();
        }
        catch(SQLException ex)
        {
            throw ex;
        }
    }
    
    public static Cliente obtenerPorId(Cliente pCliente) throws Exception
    {
        Cliente cliente = new Cliente();
        ArrayList<Cliente> clientes = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pCliente);
            sql += " Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pCliente.getId());
                obtenerDatos(ps, clientes);
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
        if(clientes.size() > 0)
        {
            cliente = clientes.get(0);
        }
        return cliente;
    }

    public static ArrayList<Cliente> obtenerTodos() throws Exception
    {
        Cliente cliente = new Cliente();
        ArrayList<Cliente> clientes = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(new Cliente());
            sql += agregarOrderBy(new Cliente());
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                obtenerDatos(ps, clientes);
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
        
        return clientes;
    }
    
    static void querySelect(Cliente pCliente, ComunDB.UtilQuery pUtilQuery) throws Exception
    {
        PreparedStatement statement = pUtilQuery.getStatement();
        if(pCliente.getId() > 0)
        {
            pUtilQuery.AgregarWhereAnd(" r.Id = ? ");
            if(statement != null)
            {
                statement.setInt(pUtilQuery.getNumWhere(), 
                        pCliente.getId());
            }
        }
        
        if(pCliente.getNombre() != null && 
           pCliente.getNombre().trim().isEmpty() == false)
        {
            pUtilQuery.AgregarWhereAnd(" r.Nombre Like ? ");
            if(statement != null)
            {
                statement.setString(pUtilQuery.getNumWhere(), 
                        "%" + pCliente.getNombre() + "%");
            }
        }
    }
    
    public static ArrayList<Cliente> buscar(Cliente pCliente) throws Exception
    {
        ArrayList<Cliente> cliente = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pCliente);
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = 
            comundb.new UtilQuery(sql,null,0);
            querySelect(pCliente, utilQuery);
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pCliente);
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pCliente, utilQuery);
                obtenerDatos(ps, cliente);
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
        
        return cliente; 
    } 
}
