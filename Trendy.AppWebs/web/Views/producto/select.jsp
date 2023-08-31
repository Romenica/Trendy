<%-- 
    Document   : select.jsp
    Created on : 29 ago. 2023, 14:34:02
    Author     : Linda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="trendy.entidadesdenegocio.Producto" %>
<%@page import="trendy.accesoadatos.ProductoDAL" %>
<%@page import="java.util.ArrayList" %>
<%
    ArrayList<Producto> producto = productoDAL.obtenerTodos();
    int id = Integer.parseInt(request.getParameter("id"));
%>
<select id="slProducto" name="idProducto">
    <option <%=(id == 0) ? "selected" : ""%> value="0">Seleccionar</option>
    <% 
        for(producto Producto:producto)
        {
    %>
    <option <%=(id == producto.getId()) ? "selected" : "" %>
        value="<%=producto.getId()%>">
        <%=producto .getNombre()%>
    </option>
    <% } %>
</select>
<label for="slEmpleados">Producto</label> 