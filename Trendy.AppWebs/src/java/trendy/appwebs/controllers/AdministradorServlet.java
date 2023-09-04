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
import trendy.accesoadatos.AdministradorDAL;
import trendy.appwebs.utils.*;
import trendy.entidadesdenegocio.*;

/**
 *
 * @author MINEDUCYT
 */
@WebServlet(name = "AdministradorServlet", urlPatterns = {"/AdministradorServlet"})
public class AdministradorServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   private Administrador obtenerAdministrador(HttpServletRequest request)
    {
        String accion = Utilidad.getParameter(request, "accion", "index");
        Administrador administrador = new Administrador();
        administrador.setNombre(Utilidad.getParameter(request, "nombre", 
                ""));
        administrador.setApellido(Utilidad.getParameter(request, "apellido",
                ""));
        administrador.setLogin(Utilidad.getParameter(request, "login",
                ""));
        administrador.setEstatus(Byte.parseByte(Utilidad.getParameter(request,
                "estatus",
                "0")));
        if(accion.equals("create") || accion.equals("login") ||
           accion.equals("cambiarpass"))
        {
            //Obtiene el parametro de Id del request y asigna el valor a la propiedad 
            //Id de la instancia
            administrador.setPassword(Utilidad.getParameter(request, 
                    "password",
                    "0"));
            administrador.setConfirmPassword_aux(Utilidad.getParameter(request, 
                    "confirmPassword_aux",
                    "0"));
            if(accion.equals("cambiarpass"))
            {
                administrador.setId(Integer.parseInt(Utilidad.getParameter(request, 
                        "id",
                    "0")));
            }
        }
        else
        if(accion.equals("index"))
        {
            administrador.setTop_aux(Integer.parseInt(Utilidad.getParameter(request, 
                    "top_aux", "10")));
            administrador.setTop_aux(administrador.getTop_aux() == 0 ? Integer.MAX_VALUE: administrador.getTop_aux());
        }
        else
        {
            administrador.setId(Integer.parseInt(Utilidad.getParameter(request, 
                    "id",
                    "0")));
        }
        return administrador;
    }
    
    protected void doGetRequestIndex(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try
        {
            Administrador administrador = new Administrador();
            administrador.setTop_aux(10);
            request.setAttribute("administradores", administrador);
            request.setAttribute("top_aux", administrador.getTop_aux());
            request.getRequestDispatcher("Views/Administrador/index.jsp")
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
            Administrador administradores = new Administrador();
            request.setAttribute("administradores", administradores);
            request.setAttribute("top_aux", administradores.getTop_aux());
            request.getRequestDispatcher("Views/Administrador/index.jsp")
                    .forward(request, response);
        }
        catch(Exception ex)
        {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    
    protected void doGetRequestLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            request.getRequestDispatcher("Views/Usuario/login.jsp")
                    .forward(request, response);
    }
    
    protected void doPostRequestLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try
        {
            Administrador administrador = obtenerAdministrador(request);
            Administrador administrador_auth = AdministradorDAL.login(administrador);//Cambiar esta linea
        }
        catch(Exception ex)
        {
            request.setAttribute("error", ex.getMessage());
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
        String accion = Utilidad.getParameter(request, "accion", 
                "index");
        if(accion.equals("login"))
        {
            request.setAttribute("accion", accion);
            doGetRequestLogin(request,response);
        }
        else
        {
            SessionUser.authorize(request, response, () -> {
                switch(accion)
                {
                    case "index":
                        request.setAttribute("accion", accion);
                        doGetRequestIndex(request, response);
                        break;
                }
            });
        }
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
        String accion = Utilidad.getParameter(request, "accion", 
                "index");
        if(accion.equals("login"))
        {
            request.setAttribute("accion", accion);
            doPostRequestLogin(request,response);
        }
        else
        {
            SessionUser.authorize(request, response, () -> {
                switch(accion)
                {
                    case "index":
                        request.setAttribute("accion", accion);
                        doPostRequestIndex(request, response);
                        break;
                }
            });
        }
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
