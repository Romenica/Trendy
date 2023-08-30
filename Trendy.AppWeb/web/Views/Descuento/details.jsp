<%-- 
    Document   : details.jsp
    Created on : 28 ago. 2023, 15:44:16
    Author     : Linda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="trendy.entidadesdenegocio.Descuento" %>
<% 
    Descuento descuento = (Descuento) request.getAttribute("descuento");
%>
<!DOCTYPE html>
<html>
    <head>
       <jsp:include page="/Views/Shared/title.jsp" />
        <title>Detalle descuento</title>
    </head>
    <body>
       <jsp:include page="/Views/Shared/headerBody.jsp" />
        <main>
            <h5>Detalle Descuento</h5>
            <form action="Descuento" method="post">
                <input type="text" name="accion" 
                       value="<%=request.getAttribute("accion")%>" 
                       id="txtHidden">
                <input type="hidden" name="id" value="<%=descuento.getId()%>">
                <div class="row">
                    <div class="input-field col 14 s12">
                        <input type="text" disabled id="txtNombre" name ="nombre" required 
                               class="validate" maxlength="30"
                               value="<%=descuento.getNombre()%>"> 
                        <label for="txtNombre">Nombre</label>
                    </div>
                </div>
                <div class="row">
                    <div class="col 112 s12">
                        <a href="Descuento?accion=edit&id=<%=descuento.getId()%>" class="waves-effect waves-light btn blue">
                            <i class="material-icons right">save</i>Ir a Modificar
                        </a>
                        <a href="Descuento" class="waves-effect waves-light btn blue">
                            <i class="material-icons right">list</i>Cancelar
                        </a>
                    </div>
                </div>
            </form>
        </main>
        <jsp:include page="/Views/Shared/footerBody.jsp" />
    </body>
</html>
