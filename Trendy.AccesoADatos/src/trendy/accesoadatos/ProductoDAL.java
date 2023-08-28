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


public class ProductoDAL {
    
      static String obtenerCampos()
    {
        return "r.Id, r.IdCliente, r.Nombre, r.Cantidad, r.Precio, r.Talla";
    }
    
    private static String obtenerSelect(Producto pProducto) /*pProducto ase referencia aun parametro*/
    {
        String sql;
        sql = "Select ";
        if(pProducto.getTop_aux() > 0 && 
           ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Top " + pProducto.getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " From Producto r");
        return sql;
    } 
    
     private static String agregarOrderBy(Producto pProducto)
    {
        String sql = " Order by r.Id Desc";
        if(pProducto.getTop_aux() > 0 && 
        ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Limit " + pProducto.getTop_aux() + " ";
        }
        return sql;
    }
    
    public static int crear(Producto pProducto) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Insert Into Producto(Nombre) Values(?)";
            try(PreparedStatement st = 
                ComunDB.createPreparedStatement(conn, sql);)
            {
                st.setString(1, pProducto .getNombre());
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
    
    public static int modificar(Producto pProducto) throws Exception 
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Update Producto Set Nombre = ? Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setString(1, pProducto.getNombre());
                ps.setInt(2, pProducto.getId());
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
    
    public static int eliminar(Producto pProducto) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Delete From Producto Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pProducto.getId());
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
    
    static int asignarDatosResultSet(Producto pProducto, ResultSet pResultSet, int pIndex) throws Exception
    {
        pIndex++;
        pProducto.setId(pResultSet.getInt(pIndex));
        pIndex++;
        pProducto.setNombre(pResultSet.getString(pIndex));
        return pIndex;
    }
    
    private static void obtenerDatos(PreparedStatement pPS, ArrayList<Producto> pProducto) throws Exception
    {
        try(ResultSet resultset = ComunDB.obtenerResulSet(pPS);)
        {
            while(resultset.next())
            {
                Producto productos = new Producto();
                asignarDatosResultSet(productos,resultset,0);
                pProducto.add(productos);
            }
            resultset.close();
        }
        catch(SQLException ex)
        {
            throw ex;
        }
    }
    
    public static Producto obtenerPorId(Producto pProducto) throws Exception
    {
        Producto producto = new Producto();
        ArrayList<Producto> productos = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pProducto);
            sql += " Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pProducto.getId());
                obtenerDatos(ps, productos);
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
        if(productos.size() > 0)
        {
            producto = productos.get(0);
        }
        return producto;
    }

    public static ArrayList<Producto> obtenerTodos() throws Exception
    {
        Producto producto = new Producto();
        ArrayList<Producto> productos = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(new Producto());
            sql += agregarOrderBy(new Producto());
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                obtenerDatos(ps, productos);
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
        
        return productos;
    }
    
    static void querySelect(Producto pProducto, ComunDB.UtilQuery pUtilQuery) throws Exception
    {
        PreparedStatement statement = pUtilQuery.getStatement();
        if(pProducto.getId() > 0)
        {
            pUtilQuery.AgregarWhereAnd(" r.Id = ? ");
            if(statement != null)
            {
                statement.setInt(pUtilQuery.getNumWhere(), 
                        pProducto.getId());
            }
        }
        
        if(pProducto.getNombre() != null && 
           pProducto.getNombre().trim().isEmpty() == false)
        {
            pUtilQuery.AgregarWhereAnd(" r.Nombre Like ? ");
            if(statement != null)
            {
                statement.setString(pUtilQuery.getNumWhere(), 
                        "%" + pProducto.getNombre() + "%");
            }
        }
    }
    
    public static ArrayList<Producto> buscar(Producto pProducto) throws Exception
    {
        ArrayList<Producto> producto = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pProducto);
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = 
            comundb.new UtilQuery(sql,null,0);
            querySelect(pProducto, utilQuery);
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pProducto);
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pProducto, utilQuery);
                obtenerDatos(ps, producto);
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
        
        return producto; 
    }   
}
