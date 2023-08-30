<%-- 
    Document   : create.jsp
    Created on : 28 ago. 2023, 10:25:38
    Author     : Linda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
          <jsp:include page="/Views/Shared/title.jsp" />
        <title>Crear Cliente</title>
    </head>
    <body>
        <jsp:include page="/Views/Shared/headerBody.jsp" />
        <main>
            <h5>Crear Cliente</h5>
            <form action="Cliente" method="post">
                <input type="hidden" name="accion" 
                       value="<%=request.getAttribute("accion")%>" id="txtHidden">
                <div class="row">
                    <div class="input-field col 14 s12">
                        <input type="text" id="txtNombre" name ="nombre" required 
                               class="validate" maxlength="30">
                        <label for="txtNombre">Nombre</label>
                    </div>
                </div>
                <div class="row">
                    <div class="col 112 s12">
                        <button type="submit" class="waves-effect waves-light btn blue">
                            <i class="material-icons right">save</i>Guardar
                        </button>
                        <a href="Cliente" class="waves-effect waves-light btn blue">
                            <i class="material-icons right">list</i>Cancelar
                        </a>
                    </div>
                </div>
            </form>
        </main>
        <jsp:include page="/Views/Shared/footerBody.jsp" />
    </body>
</html>
