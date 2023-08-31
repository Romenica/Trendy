<%-- 
    Document   : select.jsp
    Created on : 30 ago. 2023, 10:49:14
    Author     : Linda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="trendy.entidadesdenegocio.Proveedor" %>
<%@page import="trendy.accesoadatos.ProveedorDAL" %>
<%@page import="java.util.ArrayList" %>
<%
    ArrayList<Proveedor> Proveedor = proveedorDAL.obtenerTodos();
    int id = Integer.parseInt(request.getParameter("id"));
%>
<select id="slProveedor" name="idProveedor">
    <option <%=(id == 0) ? "selected" : ""%> value="0">Seleccionar</option>
    <% 
        for(Proveedor proveedor:proveedor)
        {
    %>
    <option <%=(id == proveedor.getId()) ? "selected" : "" %>
        value="<%=proveedor.getId()%>">
        <%=proveedor.getNombre()%>
    </option>
    <% } %>
</select>
<label for="slProveedor">proveedor</label>