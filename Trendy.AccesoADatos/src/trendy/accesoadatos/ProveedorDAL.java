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

public class ProveedorDAL {
    
       static String obtenerCampos()
    {
        return "r.Id, r.IdAdministrador, r.Nombre, r.Apellido, r.Telefono, r.Dirección";
    }
    
    private static String obtenerSelect(Proveedor pProveedor) /*pProveedor ase referencia aun parametro*/
    {
        String sql;
        sql = "Select ";
        if(pProveedor.getTop_aux() > 0 && 
           ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Top " + pProveedor.getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " From Proveedor r");
        return sql;
    } 
    
     private static String agregarOrderBy(Proveedor pProveedor)
    {
        String sql = " Order by r.Id Desc";
        if(pProveedor.getTop_aux() > 0 && 
        ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Limit " + pProveedor.getTop_aux() + " ";
        }
        return sql;
    }
    
    public static int crear(Proveedor pProveedor) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Insert Into Proveedor(Nombre) Values(?)";
            try(PreparedStatement st = 
                ComunDB.createPreparedStatement(conn, sql);)
            {
                st.setString(1, pProveedor.getNombre());
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
    
    public static int modificar(Proveedor pProveedor) throws Exception 
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Update Proveedor Set Nombre = ? Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setString(1, pProveedor.getNombre());
                ps.setInt(2, pProveedor.getId());
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
    
    public static int eliminar(Proveedor pProveedor) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Delete From Proveedor Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pProveedor.getId());
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
    
    static int asignarDatosResultSet(Proveedor pProveedor, ResultSet pResultSet, int pIndex) throws Exception
    {
        pIndex++;
        pProveedor.setId(pResultSet.getInt(pIndex));
        pIndex++;
        pProveedor.setNombre(pResultSet.getString(pIndex));
        return pIndex;
    }
    
    private static void obtenerDatos(PreparedStatement pPS, ArrayList<Proveedor> pProveedor) throws Exception
    {
        try(ResultSet resultset = ComunDB.obtenerResulSet(pPS);)
        {
            while(resultset.next())
            {
                Proveedor proveedores = new Proveedor();
                asignarDatosResultSet(proveedores,resultset,0);
                pProveedor.add(proveedores);
            }
            resultset.close();
        }
        catch(SQLException ex)
        {
            throw ex;
        }
    }
    
    public static Proveedor obtenerPorId(Proveedor pProveedor) throws Exception
    {
        Proveedor proveedores = new Proveedor();
        ArrayList<Proveedor> proveedor = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pProveedor);
            sql += " Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pProveedor.getId());
                obtenerDatos(ps, proveedor);
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
        if(proveedor.size() > 0)
        {
            proveedores = proveedor.get(0);
        }
        return proveedores;
    }

    public static ArrayList<Proveedor> obtenerTodos() throws Exception
    {
        Proveedor proveedores = new Proveedor();
        ArrayList<Proveedor> proveedor = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(new Proveedor());
            sql += agregarOrderBy(new Proveedor());
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                obtenerDatos(ps, proveedor);
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
        
        return proveedor;
    }
    
    static void querySelect(Proveedor pProveedor, ComunDB.UtilQuery pUtilQuery) throws Exception
    {
        PreparedStatement statement = pUtilQuery.getStatement();
        if(pProveedor.getId() > 0)
        {
            pUtilQuery.AgregarWhereAnd(" r.Id = ? ");
            if(statement != null)
            {
                statement.setInt(pUtilQuery.getNumWhere(), 
                        pProveedor.getId());
            }
        }
        
        if(pProveedor.getNombre() != null && 
           pProveedor.getNombre().trim().isEmpty() == false)
        {
            pUtilQuery.AgregarWhereAnd(" r.Nombre Like ? ");
            if(statement != null)
            {
                statement.setString(pUtilQuery.getNumWhere(), 
                        "%" + pProveedor.getNombre() + "%");
            }
        }
    }
    
    public static ArrayList<Proveedor> buscar(Proveedor pProveedor) throws Exception
    {
        ArrayList<Proveedor> proveedor = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pProveedor);
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = 
            comundb.new UtilQuery(sql,null,0);
            querySelect(pProveedor, utilQuery);
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pProveedor);
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pProveedor, utilQuery);
                obtenerDatos(ps, proveedor);
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
        
        return proveedor;
    } 
}
