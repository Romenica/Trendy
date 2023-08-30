<%-- 
    Document   : headerBody.jsp
    Created on : 28 ago 2023, 16:05:05
    Author     : MINEDUCYT
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="trendy.appweb.utils.*"%>
<nav>
    <div class="nav-wrapper blue">
        <a href="Home" class="brand-logo">Trendy</a>
        <a href="#" data-target="mobile-demo" class="sidenav-trigger"><i class="material-icons">menu</i></a>       
        <ul class="right hide-on-med-and-down">  
            <% if (SessionUser.isAuth(request)) {  %>
            <li><a href="Home">Inicio</a></li>
            <li><a href="Administrador">Administrador</a></li>
            <li><a href="Categorias">Categorias</a></li>
            <li><a href="Cliente">Cliente</a></li>
            <li><a href="Descuento">Descuento</a></li>
            <li><a href="Empleado">Empleado</a></li>
            <li><a href="Producto">Producto</a></li>
            <li><a href="Proveedor">Proveedor</a></li>
            <li><a href="Administrador?accion=cambiarpass">Cambiar password</a></li>
            <li><a href="Administrador?accion=login">Cerrar sesión</a></li>
            <%}%>
        </ul>
    </div>
</nav>

 <ul class="sidenav" id="mobile-demo">
    <% if (SessionUser.isAuth(request)) {  %>
    <li><a href="Home">Inicio</a></li>
    <li><a href="Administrador">Administrador</a></li>
    <li><a href="Categorias">Categorias</a></li>
    <li><a href="Cliente">Cliente</a></li>
    <li><a href="Descuento">Descuento</a></li>
    <li><a href="Empleado">Empleado</a></li>
    <li><a href="Producto">Producto</a></li>
    <li><a href="Proveedor">Proveedor</a></li>
    <li><a href="Administrador?accion=cambiarpass">Cambiar password</a></li>
    <li><a href="Administrador?accion=login">Cerrar sesión</a></li>
    <%}%>
</ul>
