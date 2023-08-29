<%-- 
    Document   : select.jsp
    Created on : 28 ago. 2023, 15:26:41
    Author     : Linda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="trendy.entidadesdenegocio.Cliente" %>
<%@page import="trendy.accesoadatos.ClienteDAL" %>
<%@page import="java.util.ArrayList" %>
<%
    ArrayList<Cliente> clientes = clientesDAL.obtenerTodos();
    int id = Integer.parseInt(request.getParameter("id"));
%>
<select id="slCliente" name="idCliente">
    <option <%=(id == 0) ? "selected" : ""%> value="0">Seleccionar</option>
    <% 
        for(Cliente cliente:clientes)
        {
    %>
    <option <%=(id == cliente.getId()) ? "selected" : "" %>
        value="<%=cliente.getId()%>">
        <%=cliente.getNombre()%>
    </option>
    <% } %>
</select>
<label for="slCliente">Cliente</label>