<%-- 
    Document   : delete.jsp
    Created on : 28 ago 2023, 10:34:41
    Author     : MINEDUCYT
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="trendy.entidadesdenegocio.Categorias" %>
<% 
    Categorias categorias = (Categorias) request.getAttribute("categorias");
%>

<!DOCTYPE html>
<html>
    <head>
         <jsp:include page="/Views/Shared/title.jsp" />
        <title>Eliminar Categorias</title>
    </head>
    <body>
         <jsp:include page="/Views/Shared/headerBody.jsp" />
        <main>
            <h5>Eliminar Categorias</h5>
            <form action="Rol" method="post">
                <input type="text" name="accion" 
                       value="<%=request.getAttribute("accion")%>" 
                       id="txtHidden">
                <input type="hidden" name="id" value="<%=categoria.getId()%>">
                <div class="row">
                    <div class="input-field col 14 s12">
                        <input type="text" disabled id="txtNombre" name ="nombre" required 
                               class="validate" maxlength="30"
                               value="<%=categoria.getNombre()%>"> 
                        <label for="txtNombre">Nombre</label>
                    </div>
                </div>
                <div class="row">
                    <div class="col 112 s12">
                        <button type="submit" class="waves-effect waves-light btn blue">
                            <i class="material-icons right">delete</i>Eliminar
                        </button>
                        <a href="Categoria" class="waves-effect waves-light btn blue">
                            <i class="material-icons right">list</i>Cancelar
                        </a>
                    </div>
                </div>
            </form>
        </main>
        <jsp:include page="/Views/Shared/footerBody.jsp" />
    </body>
</html>
