package Modelos;

import Genericos.Reportes;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReportesMod extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer bandera = Integer.parseInt(request.getParameter("bandera").trim());

        String urlJasper = "";
        switch (bandera) {
            case 1:
                urlJasper = "cliente.jasper";
                break;
            case 2:
                urlJasper = "empleado.jasper";
                break;
            case 3:
                urlJasper = "sucursales.jasper";
                break;
            case 4:
                urlJasper = "usuarios.jasper";
                break;
        }
        Reportes.verReporte(getServletContext().getRealPath("/WEB-INF/Reportes/" + urlJasper), new HashMap());
        //Reportes.verReporte(getServletContext().getRealPath("/WEB-INF/Reportes/MantenerPerfiles.jrxml"), new HashMap());
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
        processRequest(request, response);
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
        processRequest(request, response);
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
