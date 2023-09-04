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

public class DescuentoDAL 
{
      static String obtenerCampos()
    {
        return "r.Id, r.IdProducto, r.Cantidad, r,Precio, r.Talla, r.Descuento";
    }
    
    private static String obtenerSelect(Descuento pDS)
    {
        String sql;
        sql = "Select ";
        if(pDS.getTop_aux() > 0 && 
           ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Top " + pDS .getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " From Descuento r");
        return sql;
    }
    
    private static String agregarOrderBy(Descuento pDS)
    {
        String sql = " Order by r.Id Desc";
        if(pDS.getTop_aux() > 0 && 
        ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Limit " + pDS.getTop_aux() + " ";
        }
        return sql;
    }
    
    
    public static int crear(Descuento pDS) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Insert Into Descuento(Talla) Values(?)";
            try(PreparedStatement st = 
                ComunDB.createPreparedStatement(conn, sql);)
            {
                st.setString(1, pDS.getTalla());
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
    
    public static int modificar(Descuento pDS) throws Exception 
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Update Descuento Set Cantidad = ? Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setString(1, pDS.getTalla());
                ps.setInt(2, pDS.getId());
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
    
    public static int eliminar(Descuento pDS) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Delete From Descuento Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pDS.getId());
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
    
    static int asignarDatosResultSet(Descuento pDS, ResultSet pResultSet, int pIndex) throws Exception
    {
        pIndex++;
        pDS.setId(pResultSet.getInt(pIndex));
        pIndex++;
        pDS.setTalla(pResultSet.getString(pIndex));
        return pIndex;
    }
    
    private static void obtenerDatos(PreparedStatement pPS, ArrayList<Descuento> pDS) throws Exception
    {
        try(ResultSet resultset = ComunDB.obtenerResulSet(pPS);)
        {
            while(resultset.next())
            {
                Descuento discount = new Descuento();
                asignarDatosResultSet(discount,resultset,0);
                pDS.add(discount);
            }
            resultset.close();
        }
        catch(SQLException ex)
        {
            throw ex;
        }
    }
    
    public static Descuento obtenerPorId(Descuento pDS) throws Exception
    {
        Descuento discount = new Descuento();
        ArrayList<Descuento> discounts = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pDS);
            sql += " Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pDS.getId());
                obtenerDatos(ps, discounts);
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
        if(discounts.size() > 0)
        {
            discount = discounts.get(0);
        }
        return discount;
    }

    public static ArrayList<Descuento> obtenerTodos() throws Exception
    {
        Descuento discount = new Descuento();
        ArrayList<Descuento> discounts = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(new Descuento());
            sql += agregarOrderBy(new Descuento());
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                obtenerDatos(ps, discounts);
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
        
        return discounts;
    }
    
    static void querySelect(Descuento pDS, ComunDB.UtilQuery pUtilQuery) throws Exception
    {
        PreparedStatement statement = pUtilQuery.getStatement();
        if(pDS.getId() > 0)
        {
            pUtilQuery.AgregarWhereAnd(" r.Id = ? ");
            if(statement != null)
            {
                statement.setInt(pUtilQuery.getNumWhere(), 
                        pDS.getId());
            }
        }
        
        if(pDS.getTalla() != null && 
           pDS.getTalla().trim().isEmpty() == false)
        {
            pUtilQuery.AgregarWhereAnd(" r.Nombre Like ? ");
            if(statement != null)
            {
                statement.setString(pUtilQuery.getNumWhere(), 
                        "%" + pDS.getTalla() + "%");
            }
        }
    }
    
    public static ArrayList<Descuento> buscar(Descuento pDS) throws Exception
    {
        ArrayList<Descuento> discounts = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pDS);
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = 
            comundb.new UtilQuery(sql,null,0);
            querySelect(pDS, utilQuery);
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pDS);
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pDS, utilQuery);
                obtenerDatos(ps, discounts);
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
        
        return discounts; 
    }    
}
