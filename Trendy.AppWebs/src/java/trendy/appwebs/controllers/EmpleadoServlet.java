/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package trendy.appwebs.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import trendy.accesoadatos.EmpleadoDAL;
import trendy.entidadesdenegocio.Empleado;
import trendy.appwebs.utils.*;
/**
 *
 * @author MINEDUCYT
 */
@WebServlet(name = "EmpleadoServlet", urlPatterns = {"/EmpleadoServlet"})
public class EmpleadoServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
private Empleado obtenerEmpleado(HttpServletRequest request)
    {
        String accion = Utilidad.getParameter(request, "accion", "index");
        Empleado empleado = new Empleado();
        if(accion.equals("create") == false)
        {
            //Obtiene el parametro de Id del request y asigna el valor a la propiedad 
            //Id de la instancia
            empleado.setId(Integer.parseInt(Utilidad.getParameter(request, "id",
                    "0")));
        }
        empleado.setNombre(Utilidad.getParameter(request, "nombre", ""));
        if(accion.equals("index"))
        {
            empleado.setTop_aux(Integer.parseInt(Utilidad.getParameter(request, 
                    "top_aux", "10")));
            empleado.setTop_aux(empleado.getTop_aux() == 0 ? Integer.MAX_VALUE: empleado.getTop_aux());
        }
        return empleado;
    }
    
    protected void doGetRequestIndex(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try
        {
            Empleado empleado = new Empleado();
            empleado.setTop_aux(10);
            ArrayList<Empleado> empleados = EmpleadoDAL.buscar(empleado);
            request.setAttribute("empleado", empleado);
            request.setAttribute("top_aux", empleado.getTop_aux());
            request.getRequestDispatcher("Views/Empleado/index.jsp")
                    .forward(request, response);
        }
        catch(Exception ex)
        {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    
    protected void doPostRequestIndex(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try
        {
            Empleado empleado = obtenerEmpleado(request);
            ArrayList<Empleado> empleados = EmpleadoDAL.buscar(empleado);
            request.setAttribute("empleado", empleado);
            request.setAttribute("top_aux", empleado.getTop_aux());
            request.getRequestDispatcher("Views/Empleado/index.jsp")
                    .forward(request, response);
        }
        catch(Exception ex)
        {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    
    protected void doGetRequestCreate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            request.getRequestDispatcher("Views/Empleado/create.jsp")
                    .forward(request, response);
    }
    
    protected void doPostRequestCreate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try
        {
            Empleado empleado = obtenerEmpleado(request);
            int result = EmpleadoDAL.crear(empleado);
            if(result != 0)
            {
                request.setAttribute("accion", "index");
                doGetRequestIndex(request, response);
            }
            else
            {
                Utilidad.enviarError("Error al Guardar el Regisgtro", request, response);
            }

        }
        catch(Exception ex)
        {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    
    protected void requestObtenerPorId(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try
        {
            Empleado empleado = obtenerEmpleado(request);
            Empleado empleado_result = EmpleadoDAL.obtenerPorId(empleado);
            if(empleado_result.getId() > 0)
            {
                request.setAttribute("empleado", empleado_result);
            }
            else
            {
                Utilidad.enviarError("El id: " + empleado.getId() + " no existe en la tabla empleado", 
                        request, response);
            }
        }
        catch(Exception ex)
        {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    
    protected void doGetRequestEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
            requestObtenerPorId(request, response);
            request.getRequestDispatcher("Views/empleado/edit.jsp")
                    .forward(request, response);
    }
    
    protected void doPostRequestEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try
        {
            Empleado empleado = obtenerEmpleado(request);
            int result = EmpleadoDAL.modificar(empleado);
            if(result != 0)
            {
                request.setAttribute("accion", "index");
                doGetRequestIndex(request, response);
            }
            else
            {
                Utilidad.enviarError("Error al Guardar el Regisgtro", request, response);
            }

        }
        catch(Exception ex)
        {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    
    protected void doGetRequestDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            requestObtenerPorId(request, response);
            request.getRequestDispatcher("Views/Empleado/details.jsp")
                    .forward(request, response);
    }
    
    protected void doGetRequestDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            requestObtenerPorId(request, response);
            request.getRequestDispatcher("Views/empleado/delete.jsp")
                    .forward(request, response);
    }
    
    protected void doPostRequestDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try
        {
            Empleado empleado = obtenerEmpleado(request);
            int result = EmpleadoDAL.eliminar(empleado);
            if(result != 0)
            {
                request.setAttribute("accion", "index");
                doGetRequestIndex(request, response);
            }
            else
            {
                Utilidad.enviarError("Error al Guardar el Regisgtro", request, response);
            }

        }
        catch(Exception ex)
        {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         SessionUser.authorize(request, response, () -> {
            String accion = Utilidad.getParameter(request, 
                    "accion", "index");
            switch(accion)
            {
                case "index":
                    request.setAttribute("accion", accion);
                    doGetRequestIndex(request, response);
                    break;
                case "create":
                    request.setAttribute("accion", accion);
                    doGetRequestCreate(request, response);
                    break;
                case "edit":
                    request.setAttribute("accion", accion);
                    doGetRequestEdit(request, response);
                    break;
                case "delete":
                    request.setAttribute("accion", accion);
                    doGetRequestDelete(request, response);
                    break;
                case "details":
                    request.setAttribute("accion", accion);
                    doGetRequestDetails(request, response);
                    break;
                default:
                    request.setAttribute("accion", accion);
                    doGetRequestIndex(request, response);
                    break;
            }
        });
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //SessionUser.authorize(request, response, () -> {
            String accion = Utilidad.getParameter(request, 
                    "accion", "index");
            switch(accion)
            {
                case "index":
                    request.setAttribute("accion", accion);
                    doPostRequestIndex(request, response);
                    break;
                case "create":
                    request.setAttribute("accion", accion);
                    doPostRequestCreate(request, response);
                    break;
                case "edit":
                    request.setAttribute("accion", accion);
                    doPostRequestEdit(request, response);
                    break;
                case "delete":
                    request.setAttribute("accion", accion);
                    doPostRequestDelete(request, response);
                    break;
                default:
                    request.setAttribute("accion", accion);
                    doGetRequestIndex(request, response);
                    break;
            }
        //});
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
