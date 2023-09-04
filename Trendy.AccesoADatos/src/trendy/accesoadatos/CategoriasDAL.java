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
        return "r.Id, r.IdProducto, r.NombreCategorias";
    }
    
    private static String obtenerSelect(Categorias pC)
    {
        String sql;
        sql = "Select ";
        if(pC.getTop_aux() > 0 && 
           ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Top " + pC.getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " From Categorias r");
        return sql;
    }
    
    private static String agregarOrderBy(Categorias pC)
    {
        String sql = " Order by r.Id Desc";
        if(pC.getTop_aux() > 0 && 
        ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Limit " + pC.getTop_aux() + " ";
        }
        return sql;
    }
    
    
    public static int crear(Categorias pC) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Insert Into Categorias(NombreCategorias) Values(?)";
            try(PreparedStatement st = 
                ComunDB.createPreparedStatement(conn, sql);)
            {
                st.setString(1, pC.getNombreCategoria());
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
    
    public static int modificar(Categorias pC) throws Exception 
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Update Categorias Set Nombre = ? Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setString(1, pC.getNombreCategoria());
                ps.setInt(2, pC.getId());
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
    
    public static int eliminar(Categorias pC) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Delete From Categorias Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pC.getId());
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
    
    static int asignarDatosResultSet(Categorias pC, ResultSet pResultSet, int pIndex) throws Exception
    {
        pIndex++;
        pC.setId(pResultSet.getInt(pIndex));
        pIndex++;
        pC.setNombreCategoria(pResultSet.getString(pIndex));
        return pIndex;
    }
    
    private static void obtenerDatos(PreparedStatement pPS, ArrayList<Categorias> pC) throws Exception
    {
        try(ResultSet resultset = ComunDB.obtenerResulSet(pPS);)
        {
            while(resultset.next())
            {
                Categorias category = new Categorias();
                asignarDatosResultSet(category,resultset,0);
                pC.add(category);
            }
            resultset.close();
        }
        catch(SQLException ex)
        {
            throw ex;
        }
    }
    
    public static Categorias obtenerPorId(Categorias pC) throws Exception
    {
        Categorias category = new Categorias();
        ArrayList<Categorias> categories = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pC);
            sql += " Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pC.getId());
                obtenerDatos(ps, categories);
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
        if(categories.size() > 0)
        {
            category = categories.get(0);
        }
        return category;
    }

    public static ArrayList<Categorias> obtenerTodos() throws Exception
    {
        Categorias category = new Categorias();
        ArrayList<Categorias> categories = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(new Categorias());
            sql += agregarOrderBy(new Categorias());
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                obtenerDatos(ps, categories);
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
        
        return categories;
    }
    
    static void querySelect(Categorias pC, ComunDB.UtilQuery pUtilQuery) throws Exception
    {
        PreparedStatement statement = pUtilQuery.getStatement();
        if(pC.getId() > 0)
        {
            pUtilQuery.AgregarWhereAnd(" r.Id = ? ");
            if(statement != null)
            {
                statement.setInt(pUtilQuery.getNumWhere(), 
                        pC.getId());
            }
        }
        
        if(pC.getNombreCategoria() != null && 
           pC.getNombreCategoria().trim().isEmpty() == false)
        {
            pUtilQuery.AgregarWhereAnd(" r.Nombre Like ? ");
            if(statement != null)
            {
                statement.setString(pUtilQuery.getNumWhere(), 
                        "%" + pC.getNombreCategoria() + "%");
            }
        }
    }
    
    public static ArrayList<Categorias> buscar(Categorias pC) throws Exception
    {
        ArrayList<Categorias> categories = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pC);
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = 
            comundb.new UtilQuery(sql,null,0);
            querySelect(pC, utilQuery);
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pC);
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pC, utilQuery);
                obtenerDatos(ps, categories);
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
        
        return categories; 
    }
}
