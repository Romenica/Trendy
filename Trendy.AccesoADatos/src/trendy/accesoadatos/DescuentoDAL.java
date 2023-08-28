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


public class DescuentoDAL {
    
      static String obtenerCampos()
    {
        return "r.Id, r.IdProducto, r.Cantidad, r.Precio, r.Talla, r.Descuento";
    }
    
    private static String obtenerSelect(Descuento pDescuento) /*pDescuento ase referencia aun parametro*/
    {
        String sql;
        sql = "Select ";
        if(pDescuento.getTop_aux() > 0 && 
           ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Top " + pDescuento.getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " From Talla r");
        return sql;
    } 
    
     private static String agregarOrderBy(Descuento pDescuento)
    {
        String sql = " Order by r.Id Desc";
        if(pDescuento.getTop_aux() > 0 && 
        ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER)
        {
            sql += "Limit " + pDescuento.getTop_aux() + " ";
        }
        return sql;
    }
    
    public static int crear(Descuento pDescuento) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Insert Into Descuento(Talla) Values(?)";
            try(PreparedStatement st = 
                ComunDB.createPreparedStatement(conn, sql);)
            {
                st.setString(1, pDescuento.getTalla());
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
    
    public static int modificar(Descuento pDescuento) throws Exception 
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Update Descuento Set Talla  = ? Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setString(1, pDescuento.getTalla());
                ps.setInt(2, pDescuento.getId());
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
    
    public static int eliminar(Descuento pDescuento) throws Exception
    {
        int result;
        String sql;
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            sql = "Delete From Descuento Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pDescuento.getId());
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
    
    static int asignarDatosResultSet(Descuento pDescuento, ResultSet pResultSet, int pIndex) throws Exception
    {
        pIndex++;
        pDescuento.setId(pResultSet.getInt(pIndex));
        pIndex++;
        pDescuento.setTalla(pResultSet.getString(pIndex));
        return pIndex;
    }
    
    private static void obtenerDatos(PreparedStatement pPS, ArrayList<Descuento> pDescuento) throws Exception
    {
        try(ResultSet resultset = ComunDB.obtenerResulSet(pPS);)
        {
            while(resultset.next())
            {
                Descuento descuentos = new Descuento();
                asignarDatosResultSet(descuentos,resultset,0);
                pDescuento.add(descuentos);
            }
            resultset.close();
        }
        catch(SQLException ex)
        {
            throw ex;
        }
    }
    
    public static Descuento obtenerPorId(Descuento pDescuento) throws Exception
    {
        Descuento descuento = new Descuento();
        ArrayList<Descuento> descuentos = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pDescuento);
            sql += " Where Id = ?";
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                ps.setInt(1, pDescuento.getId());
                obtenerDatos(ps, descuentos);
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
        if(descuentos.size() > 0)
        {
            descuento = descuentos.get(0);
        }
        return descuento;
    }

    public static ArrayList<Descuento> obtenerTodos() throws Exception
    {
        Descuento descuento = new Descuento();
        ArrayList<Descuento> descuentos = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(new Descuento());
            sql += agregarOrderBy(new Descuento());
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                obtenerDatos(ps, descuentos);
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
        
        return descuentos;
    }
    
    static void querySelect(Descuento pDescuento, ComunDB.UtilQuery pUtilQuery) throws Exception
    {
        PreparedStatement statement = pUtilQuery.getStatement();
        if(pDescuento.getId() > 0)
        {
            pUtilQuery.AgregarWhereAnd(" r.Id = ? ");
            if(statement != null)
            {
                statement.setInt(pUtilQuery.getNumWhere(), 
                        pDescuento.getId());
            }
        }
        
        if(pDescuento.getTalla() != null && 
           pDescuento.getTalla().trim().isEmpty() == false)
        {
            pUtilQuery.AgregarWhereAnd(" r.Talla Like ? ");
            if(statement != null)
            {
                statement.setString(pUtilQuery.getNumWhere(), 
                        "%" + pDescuento.getTalla() + "%");
            }
        }
    }
    
    public static ArrayList<Descuento> buscar(Descuento pDescuento) throws Exception
    {
        ArrayList<Descuento> descuento = new ArrayList();
        try(Connection conn = ComunDB.obtenerConexion();)
        {
            String sql = obtenerSelect(pDescuento);
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = 
            comundb.new UtilQuery(sql,null,0);
            querySelect(pDescuento, utilQuery);
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pDescuento);
            try(PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);)
            {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pDescuento, utilQuery);
                obtenerDatos(ps, descuento);
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
        
        return descuento; 
    } 
    
}
