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
import trendy.accesoadatos.ClienteDAL;
import trendy.entidadesdenegocio.Cliente;
import trendy.appwebs.utils.*;
/**
 *
 * @author MINEDUCYT
 */
@WebServlet(name = "ClienteServlet", urlPatterns = {"/ClienteServlet"})
public class ClienteServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   private Cliente obtenerCliente(HttpServletRequest request)
    {
        String accion = Utilidad.getParameter(request, "accion", "index");
        Cliente cliente = new Cliente();
        if(accion.equals("create") == false)
        {
            //Obtiene el parametro de Id del request y asigna el valor a la propiedad 
            //Id de la instancia
            cliente.setId(Integer.parseInt(Utilidad.getParameter(request, "id",
                    "0")));
        }
        cliente.setNombre(Utilidad.getParameter(request, "nombre", ""));
        if(accion.equals("index"))
        {
            cliente.setTop_aux(Integer.parseInt(Utilidad.getParameter(request, 
                    "top_aux", "10")));
            cliente.setTop_aux(cliente.getTop_aux() == 0 ? Integer.MAX_VALUE: cliente.getTop_aux());
        }
        return cliente;
    }
    
    protected void doGetRequestIndex(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try
        {
            Cliente cliente = new Cliente();
            cliente.setTop_aux(10);
            ArrayList<Cliente> clientes = ClienteDAL.buscar(cliente);
            request.setAttribute("clientes", cliente);
            request.setAttribute("top_aux", cliente.getTop_aux());
            request.getRequestDispatcher("Views/Cliente/index.jsp")
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
            Cliente cliente = obtenerCliente(request);
            ArrayList<Cliente> roles = ClienteDAL.buscar(cliente);
            request.setAttribute("clientes", cliente);
            request.setAttribute("top_aux", cliente.getTop_aux());
            request.getRequestDispatcher("Views/Rol/index.jsp")
                    .forward(request, response);
        }
        catch(Exception ex)
        {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    
    protected void doGetRequestCreate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            request.getRequestDispatcher("Views/Cliente/create.jsp")
                    .forward(request, response);
    }
    
    protected void doPostRequestCreate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try
        {
            Cliente cliente = obtenerCliente(request);
            int result = ClienteDAL.crear(cliente);
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
            Cliente cliente = obtenerCliente(request);
            Cliente cliente_result = ClienteDAL.obtenerPorId(cliente);
            if(cliente_result.getId() > 0)
            {
                request.setAttribute("cliente", cliente_result);
            }
            else
            {
                Utilidad.enviarError("El id: " + cliente.getId() + " no existe en la tabla cliente", 
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
            request.getRequestDispatcher("Views/Cliente/edit.jsp")
                    .forward(request, response);
    }
    
    protected void doPostRequestEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try
        {
            Cliente cliente = obtenerCliente(request);
            int result = ClienteDAL.modificar(cliente);
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
            request.getRequestDispatcher("Views/Cliente/details.jsp")
                    .forward(request, response);
    }
    
    protected void doGetRequestDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            requestObtenerPorId(request, response);
            request.getRequestDispatcher("Views/Cliente/delete.jsp")
                    .forward(request, response);
    }
    
    protected void doPostRequestDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try
        {
            Cliente cliente = obtenerCliente(request);
            int result = ClienteDAL.eliminar(cliente);
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
