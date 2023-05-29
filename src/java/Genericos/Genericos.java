package Genericos;

import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Genericos {

    public static boolean esNulo(JTextField txt) {
        if (txt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(txt,
                    "No se permiten valores nulos");
            return false;
        } else {
            return true;
        }
    }

    public static boolean confirmarOperacion() {
        int r = JOptionPane.showConfirmDialog(null, "Confirmar Operación", "Aviso al Usuario ", JOptionPane.YES_NO_OPTION);
        return r == 0;
    }

    public static Integer getUltimoID(String tabla, String columna) {
        try {
            PreparedStatement ps;
            ResultSet rs;
            String query = "select coalesce(max(" + columna + "),0) +  1 as id "
                    + "from " + tabla + "";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Genericos.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        return 0;
    }

    public static KeyEvent soloNumerico(KeyEvent evt) {
        char value = evt.getKeyChar();
        if (value < '0' || value > '9') {
            evt.consume();
        }
        return evt;
    }

    //BLOQUE FECHA 
    private static final SimpleDateFormat formatoddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat formatoyyyyMMdd = new SimpleDateFormat("yyyy/MM/dd");

    public static String getFechaServidor() {
        ConexionDB.getInstancia();
        ResultSet rs;
        PreparedStatement ps;
        try {
            ps = ConexionDB.getDBcon().prepareStatement("select to_char(current_date,'DD/MM/YYYY') as fecha");
            rs = ps.executeQuery();
            rs.next();
            return rs.getString("fecha");
        } catch (Throwable e) {
            return "fecha invalida";
        }
    }

    public static void prepararCampoFecha(JFormattedTextField asdfecha) {
        try {
            asdfecha.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (ParseException ex) {
            Logger.getLogger(Genericos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String retornarFechaddMMyyyy(Date fechaRecuperada) {
        return formatoddMMyyyy.format(fechaRecuperada);
    }

    //retornar Fecha como String
    public static String retornarFechayyyyMMdd(String fecha) {
        Date fechaLocal = null;
        try {
            fechaLocal = formatoyyyyMMdd.parse(fecha);
        } catch (ParseException ex) {
            Logger.getLogger(Genericos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return formatoyyyyMMdd.format(fechaLocal);
    }

    //retornar Fecha como Date
    public static java.sql.Date retornarFecha(String fechaRecibida) {
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
        String strFecha = fechaRecibida;
        Date fecha = null;
        try {
            fecha = formatoDelTexto.parse(strFecha);
        } catch (ParseException ex) {
        }
        return fechaUtilTosql(fecha);
    }

    private static java.sql.Date fechaUtilTosql(java.util.Date utilDate) {
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        return sqlDate;
    }

    //Para evitar que existan valores duplicados....
    //SELECT EXISTS(SELECT id, perfil FROM public.perfiles WHERE perfil='Operacional') 
    public static boolean esEnter(KeyEvent evt) {
        return evt.getKeyCode() == KeyEvent.VK_ENTER;
    }

    //retornar Hora como Time
    //BLOQUE HORA

    }


