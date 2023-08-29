-<%-- 
    Document   : select.jsp
    Created on : 28 ago. 2023, 19:07:09
    Author     : Linda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="trendy.entidadesdenegocio.Empleado" %>
<%@page import="trendy.accesoadatos.EmpleadoDAL" %>
<%@page import="java.util.ArrayList" %>
<%
    ArrayList<Empleado> empleado = empleadoDAL.obtenerTodos();
    int id = Integer.parseInt(request.getParameter("id"));
%>
<select id="slEmpleado" name="idEmpleado">
    <option <%=(id == 0) ? "selected" : ""%> value="0">Seleccionar</option>
    <% 
        for(Empleado Empleado:empleado)
        {
    %>
    <option <%=(id == empleado.getId()) ? "selected" : "" %>
        value="<%=empleado.getId()%>">
        <%=empleados.getNombre()%>
    </option>
    <% } %>
</select>
<label for="slEmpleados">Empleado</label>