<%-- 
    Document   : details.jsp
    Created on : 29 ago. 2023, 14:32:37
    Author     : Linda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="trendy.entidadesdenegocio.Producto" %>
<% 
    Producto producto = (Producto) request.getAttribute("producto");
%>
<!DOCTYPE html>
<html>
    <head>
       <jsp:include page="/Views/Shared/title.jsp" />
        <title>Detalle producto</title>
    </head>
    <body>
       <jsp:include page="/Views/Shared/headerBody.jsp" />
        <main>
            <h5>Detalle Producto</h5>
            <form action="Producto" method="post">
                <input type="text" name="accion" 
                       value="<%=request.getAttribute("accion")%>" 
                       id="txtHidden">
                <input type="hidden" name="id" value="<%=producto.getId()%>">
                <div class="row">
                    <div class="input-field col 14 s12">
                        <input type="text" disabled id="txtNombre" name ="nombre" required 
                               class="validate" maxlength="30"
                               value="<%=producto.getNombre()%>"> 
                        <label for="txtNombre">Nombre</label>
                    </div>
                </div>
                <div class="row">
                    <div class="col 112 s12">
                        <a href="Producto?accion=edit&id=<%=producto.getId()%>" class="waves-effect waves-light btn blue">
                            <i class="material-icons right">save</i>Ir a Modificar
                        </a>
                        <a href="Producto" class="waves-effect waves-light btn blue">
                            <i class="material-icons right">list</i>Cancelar
                        </a>
                    </div>
                </div>
            </form>
        </main>
        <jsp:include page="/Views/Shared/footerBody.jsp" />
    </body>
</html>
