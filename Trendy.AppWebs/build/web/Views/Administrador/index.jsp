<%-- 
    Document   : index
    Created on : 3 sept 2023, 20:24:54
    Author     : MINEDUCYT
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="trendy.entidadesdenegocio.Administrador"%>
<%@page import="java.util.ArrayList" %>
<%ArrayList<Administrador> administrador = (ArrayList<Administrador>) request
        .getAttribute("administradores");
    int numPage = 1;
    int numReg = 10;
    int countReg = 0;
    if(administradores == null)
    {
        administradores = new ArrayList();
    }
    else
        if(administradores.size() > numReg)
        {
            double divNumPage = (double) administradores.size() / (double) numReg;
            numPage = (int) Math.ceil(divNumPage);
        }
    String strTop_aux = request.getParameter("top_aux");
    int top_aux = 10;
    if(strTop_aux != null && strTop_aux.trim().length() > 0)
    {
        top_aux = Integer.parseInt(strTop_aux);
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/Views/Shared/title.jsp" />
        <title>Buscar Administrador</title>
    </head>
    <body>
        <jsp:include page="/Views/Shared/headerBody.jsp" /> 
        <main class="container">
           <h5>Buscar Administrador</h5>
           <form action="Administrador" method="post">
               <input type="hidden" name="accion" value="<%request.getAttribute("accion");%>">
               <div class="row">
                   <div class="input-field col 14 s12">
                       <input type="text" id="txtNombre" name="nombre">
                       <label for="txtNombre">Nombre</label>
                   </div>
                   <div class="input-field col 14 s12">
                       <input type="text" id="txtApellido" name="apellido">
                       <label for="txtApellido">Apellido</label>
                   </div>
                   <div class="input-field col 14 s12">
                       <input type="text" id="txtLogin" name="login">
                       <label for="txtLogin">Login</label>
                   </div>
                   <div class="input-field col 14 s12">
                       <jsp:include page="/Views/Shared/selectTop.jsp">
                           <jsp:param name="top_aux" value="<%=top_aux%>"/>
                       </jsp:include>
                   </div>
               </div>
               <div class="row">
                   <div class="input-field col 16 s12">
                       <button type="submit" class="waves-effect waves-ligth btn blue">Buscar</button>
                       <a href="Administrador?accion=create" class="waves-effect waves-ligth btn blue">Nuevo</a>
                   </div>
               </div>
           </form>
               
            <div class="row">
                <div class="col 112 s12">
                    <div style="overflow: auto;">
                        <table class="paginationjs">
                            <thead>
                                <tr>
                                    <th>Nombre</th>
                                    <th>Apellido</th>
                                    <th>Login</th>
                                    <th>Estatus</th>
                                    <th>Fecha de Registro</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                for(Administrador administrador:administradores)
                                {
                                   int tempNumPage = numPage;
                                   if(numPage > 1)
                                   {
                                        countReg++;
                                        double divTempNumPage = (double) countReg / (double) numReg;
                                        tempNumPage = (int) Math.ceil(divTempNumPage);
                                   }
                                   String estatus = "";
                                   switch(administrador.getEstatus())
                                   {
                                        case 1:
                                            estatus = "ACTIVO";
                                            break;
                                        case 2:
                                            estatus = "INACTIVO";
                                            break;
                                        default:
                                            estatus = "";
                                   }
                                %>
                                    <tr data-page="<%=tempNumPage%>">
                                        <td><%=administrador.getNombre()%></td>
                                        <td><%=administrador.getApellido()%></td>
                                        <td><%=administrador.getLogin()%></td>
                                        <td><%=estatus%></td>
                                        <td><%=administrador.getFechaRegistro()%></td>
                                        <td>
                                            <div style="display: flex">
                                                <a href="Administrador?accion=edit&id=<%=administrador.getId()%>" 
                                                   title="Mofificar" class="waves-effect waves-light btn green">
                                                    <i class="material-icons">edit</i>
                                                </a>
                                                <a href="Administrador?accion=details&id=<%=administrador.getId()%>" 
                                                   title="Ver" class="waves-effect waves-light btn blue">
                                                    <i class="material-icons">description</i>
                                                </a>
                                                <a href="Administrador?accion=delete&id=<%=administrador.getId()%>" 
                                                   title="Eliminar" class="waves-effect waves-light btn red">
                                                    <i class="material-icons">delete</i>
                                                </a>
                                            </div>
                                        </td>
                                    </tr> 
                                <%}%>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col 112 s12">
                    <jsp:include page="/Views/Shared/paginacion.jsp">
                        <jsp:param name="numPage" value="<%=numPage%>"/>
                    </jsp:include> 
                </div>
            </div>
        </main>
        <jsp:include page="/Views/Shared/footerBody.jsp" />
    </body>
</html>

