package Genericos;

import java.io.File;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class Reportes {
    public static void verReporte(String urlJasper, Map parametros) {
        try {
            ConexionDB.getInstancia();
            JasperPrint jp;
            JasperReport reporte;

            File url = new File(urlJasper);
            if (url.exists()) {
                reporte = (JasperReport) JRLoader.loadObject(url);
                jp = JasperFillManager.fillReport(reporte, parametros, ConexionDB.getDBcon());
                JasperViewer.viewReport(jp, false);
            }
            JasperReport report = JasperCompileManager.compileReport(urlJasper);
            JasperPrint print = JasperFillManager.fillReport(report, parametros, ConexionDB.getDBcon());
            JasperViewer.viewReport(print, false);
        } catch (JRException ex) {
            Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}