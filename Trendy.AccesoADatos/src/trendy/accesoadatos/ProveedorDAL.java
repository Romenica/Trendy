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

public class ProveedorDAL 
{
         static String obtenerCampos()
    {
        return "r.Id, r.IdAdministrador, r.Nombre, r.Apellido, r.Telefono, r,Dirección ";
    }
    
    private static String obtenerSelect(Proveedor pVDR)
    {
        String sql;
        sql = "Select ";
        if(pVDR.getTop_aux() > 0 && 
           ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Top " + pVDR .getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " From Proveedor r");
        return sql;
    }
    
    private static String agregarOrderBy(Proveedor pVDR)
    {
        String sql = " Order by r.Id Desc";
        if(pVDR.getTop_aux() > 0 && 
        ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Limit " + pVDR.getTop_aux() + " ";
        }
        return sql;
    }
    
    
    public static int crear(Proveedor pVDR) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Insert Into Proveedor(Nombre) Values(?)";
            try(PreparedStatement st = 
                ComunDB.createPreparedStatement(conn, sql);)
            {
                st.setString(1, pVDR.getNombre());
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
    
    public static int modificar(Proveedor pVDR) throws Exception 
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Update Proveedor Set Nombre = ? Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setString(1, pVDR.getNombre());
                ps.setInt(2, pVDR.getId());
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
    
    public static int eliminar(Proveedor pVDR) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Delete From Proveedor Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pVDR.getId());
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
    
    static int asignarDatosResultSet(Proveedor pVDR, ResultSet pResultSet, int pIndex) throws Exception
    {
        pIndex++;
        pVDR.setId(pResultSet.getInt(pIndex));
        pIndex++;
        pVDR.setNombre(pResultSet.getString(pIndex));
        return pIndex;
    }
    
    private static void obtenerDatos(PreparedStatement pPS, ArrayList<Proveedor> pVDR) throws Exception
    {
        try(ResultSet resultset = ComunDB.obtenerResulSet(pPS);)
        {
            while(resultset.next())
            {
                Proveedor supplier = new Proveedor();
                asignarDatosResultSet(supplier,resultset,0);
                pVDR.add(supplier);
            }
            resultset.close();
        }
        catch(SQLException ex)
        {
            throw ex;
        }
    }
    
    public static Proveedor obtenerPorId(Proveedor pVDR) throws Exception
    {
        Proveedor supplier = new Proveedor();
        ArrayList<Proveedor> providers = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pVDR);
            sql += " Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pVDR.getId());
                obtenerDatos(ps, providers);
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
        if(providers.size() > 0)
        {
           supplier = providers.get(0);
        }
        return supplier;
    }

    public static ArrayList<Proveedor> obtenerTodos() throws Exception
    {
        Proveedor supplier = new Proveedor();
        ArrayList<Proveedor> providers = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(new Proveedor());
            sql += agregarOrderBy(new Proveedor());
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                obtenerDatos(ps, providers);
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
        
        return providers;
    }
    
    static void querySelect(Proveedor pVDR, ComunDB.UtilQuery pUtilQuery) throws Exception
    {
        PreparedStatement statement = pUtilQuery.getStatement();
        if(pVDR.getId() > 0)
        {
            pUtilQuery.AgregarWhereAnd(" r.Id = ? ");
            if(statement != null)
            {
                statement.setInt(pUtilQuery.getNumWhere(), 
                        pVDR.getId());
            }
        }
        
        if(pVDR.getNombre() != null && 
           pVDR.getNombre().trim().isEmpty() == false)
        {
            pUtilQuery.AgregarWhereAnd(" r.Nombre Like ? ");
            if(statement != null)
            {
                statement.setString(pUtilQuery.getNumWhere(), 
                        "%" + pVDR.getNombre() + "%");
            }
        }
    }
    
    public static ArrayList<Proveedor> buscar(Proveedor pVDR) throws Exception
    {
        ArrayList<Proveedor> providers = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pVDR);
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = 
            comundb.new UtilQuery(sql,null,0);
            querySelect(pVDR, utilQuery);
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pVDR);
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pVDR, utilQuery);
                obtenerDatos(ps, providers);
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
        
        return providers; 
    }
}
