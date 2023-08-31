<%-- 
    Document   : edit.jsp
    Created on : 29 ago. 2023, 14:33:04
    Author     : Linda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="trendy.entidadesdenegocio.Producto" %>
<% 
    Producto producto  = (Producto) request.getAttribute("Producto");
%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/Views/Shared/title.jsp" />
        <title>Editar Producto</title>
    </head>
    <body>
     <jsp:include page="/Views/Shared/headerBody.jsp" />
        <main>
            <h5>Editar Producto</h5>
            <form action="Producto" method="post">
                <input type="hidden" name="accion" 
                       value="<%=request.getAttribute("accion")%>" 
                       id="txtHidden">
                <input type="hidden" name="id" value="<%=producto.getId()%>">
                <div class="row">
                    <div class="input-field col 14 s12">
                        <input type="text" id="txtNombre" name ="nombre" required 
                               class="validate" maxlength="30"
                               value="<%=producto.getNombre()%>"> 
                        <label for="txtNombre">Nombre</label>
                    </div>
                </div>
                <div class="row">
                    <div class="col 112 s12">
                        <button type="submit" class="waves-effect waves-light btn blue">
                            <i class="material-icons right">save</i>Guardar
                        </button>
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
   