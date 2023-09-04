/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trendy.appwebs.utils;

/**
 *
 * @author MINEDUCYT
 */
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import trendy.entidadesdenegocio.Administrador;

public class SessionUser
{
   public static void autenticarUser(HttpServletRequest request, Administrador pAdministrador){
        HttpSession session = (HttpSession) request.getSession();
        session.setMaxInactiveInterval(3600);
        session.setAttribute("auth", true);
        session.setAttribute("user", pAdministrador.getLogin());
    }
    
    public static boolean isAuth(HttpServletRequest request)
    {
        HttpSession session = (HttpSession) request.getSession();
        boolean auth = session.getAttribute("auth") != null ? 
                (boolean) session.getAttribute("auth"): false;
        return auth;
    }
    
    public static String getUser(HttpServletRequest request)
    {
        HttpSession session = (HttpSession) request.getSession();
        String user = "";
        if(SessionUser.isAuth(request))
        {
            user = session.getAttribute("user") != null ? 
                    (String) session.getAttribute("user") : "";
        }
        return user;
    }
    
    public static void authorize(HttpServletRequest request, 
                                 HttpServletResponse response,
                                 IAuthorize pIAuthorize) throws ServletException,
                                                                IOException
    {
        if(SessionUser.isAuth(request))
        {
          pIAuthorize.authorize();
        }
        else
        {
            response.sendRedirect("Administrador?accion=login");
        }
    }
    
    public static void cerrarSession(HttpServletRequest request)
    {
        HttpSession session = (HttpSession) request.getSession();
        session.invalidate();
    }
 
}
