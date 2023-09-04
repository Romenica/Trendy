<%-- 
    Document   : select.jsp
    Created on : 28 ago 2023, 15:36:35
    Author     : MINEDUCYT
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="trnedy.entidadesdenegocio.Categorias" %>
<%@page import="trendy.accesoadatos.CategoriasDAL" %>
<%@page import="java.util.ArrayList" %>
<%
    ArrayList<Categorias> categoria = CategoriasDAL.obtenerTodos();
    int id = Integer.parseInt(request.getParameter("id"));
%>
<select id="slCategorias" name="idCategorias">
    <option <%=(id == 0) ? "selected" : ""%> value="0">Seleccionar</option>
    <% 
        for(Categorias categorias:categoria)
        {
    %>
    <option <%=(id == categorias.getId()) ? "selected" : "" %>
        value="<%=rol.getId()%>">
        <%=categorias.getNombre()%>
    </option>
    <% } %>
</select>
<label for="slRol">Categorias</label>