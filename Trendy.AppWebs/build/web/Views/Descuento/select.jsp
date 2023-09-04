<%-- 
    Document   : select.jsp
    Created on : 28 ago. 2023, 15:45:38
    Author     : Linda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="trendy.entidadesdenegocio.Descuento" %>
<%@page import="trendy.accesoadatos.DescuentoDAL" %>
<%@page import="java.util.ArrayList" %>
<%
    ArrayList<Descuento> descuento = descuentosDAL.obtenerTodos();
    int id = Integer.parseInt(request.getParameter("id"));
%>
<select id="slDescuento" name="idDescuento">
    <option <%=(id == 0) ? "selected" : ""%> value="0">Seleccionar</option>
    <% 
        for(Descuento descuentos:descuento)
        {
    %>
    <option <%=(id == descuento.getId()) ? "selected" : "" %>
        value="<%=descuento.getId()%>">
        <%=descuentos.getNombre()%>
    </option>
    <% } %>
</select>
<label for="slDescuentos">Descuentos</label>