<%-- 
    Document   : detailsjsp
    Created on : 3 sept 2023, 20:21:41
    Author     : MINEDUCYT
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="trendy.entidadesdenegocio.Administrador" %>
<% Administrador administrador = (Administrador) request.getAttribute("administrador");  %>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/Views/Shared/title.jsp" />
        <title>Detalle Administrador</title>
    </head>
    <body>
        <jsp:include page="/Views/Shared/headerBody.jsp" />
        <main>
            <h5>Detalle Administrador</h5>
                <input type="hidden" name="accion" 
                       value="<%=request.getAttribute("accion")%>" id="txtHidden">
                <input type="hidden" name="id" value="<%=administrador.getId()%>"
                <div class="row">
                    <div class="input-field col 14 s12">
                        <input type="text" id="txtNombre" name ="nombre" required 
                               class="validate" maxlength="30"
                               value="<%=usuario.getNombre()%>"
                               disabled>
                        <label for="txtNombre">Nombre</label>
                    </div>
                    <div class="input-field col 14 s12">
                        <input type="text" id="txtApellido" name ="apellido" required 
                               class="validate" maxlength="30"
                               value="<%=administrador.getApellido()%>"
                               disabled>
                        <label for="txtApellido">Apellido</label>
                    </div>
                    <div class="input-field col 14 s12">
                        <input type="text" id="txtLogin" name ="login" required 
                               class="validate" maxlength="25"
                               value="<%=administrador.getLogin()%>"
                               disabled>
                        <label for="txtLogin">Login</label>
                    </div>
                    <div class="input-field col 14 s12">
                        <select id="slEstatus" name="estatus" class="validate"
                                disabled>
                            <option value="0" <%=administrador.getEstatus() == 0 ? "selected":""%>>--SELECCIONAR--</option>
                            <option value="1" <%=administrador.getEstatus() == 1 ? "selected":""%>>ACTIVO</option>
                            <option value="2" <%=administrador.getEstatus() == 2 ? "selected":""%>>INACTIVO</option>
                        </select>
                        <label for="slEstatus" >Estatus</label>
                    </div>
                </div>
                <div class="row">
                    <div class="col 112 s12">
                        <a href="Administrador?accion=edit&id=<%=administrador.getId()%>" class="waves-effect waves-light btn blue">
                            <i class="material-icons right">edit</i>Modificar
                        </a>
                        <a href="Administrador" class="waves-effect waves-light btn blue">
                            <i class="material-icons right">list</i>Cancelar
                        </a>
                    </div>
                </div>
        </main>
        <jsp:include page="/Views/Shared/footerBody.jsp" />
    </body>
</html>

