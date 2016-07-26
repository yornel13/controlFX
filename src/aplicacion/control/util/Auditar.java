/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.util;

import hibernate.dao.AccionTipoDAO;
import hibernate.dao.RegistroAccionesDAO;
import hibernate.model.AccionTipo;
import hibernate.model.RegistroAcciones;
import hibernate.model.Usuario;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author Yornel
 */
public class Auditar {
    
    private String nombre;
    
    public static final String INGRESO = "ingreso";
    public static final String AGREGO = "agrego";
    public static final String EDITO = "edito";
    public static final String ELIMINO = "elimino";
    
    public static final String EMPLEADO = "empleado";
    public static final String EMPLEADOS = "empleados";
    public static final String CLIENTE = "cliente";
    public static final String CLIENTES  = "clientes";
    public static final String EMPRESA = "empresa";
    public static final String EMPRESAS = "empresas";
    public static final String CONFIGURACION = "configuraciones";
    public static final String LOGEO = "logeo";
    public static final String ADMINITRADORES = "administradores";
    public static final String ROL_DE_PAGO = "rol de pago";
    public static final String PAGOS = "pagos";
    public static final String REPORTES = "reportes";
    public static final String AUDITORIA = "auditoria";
    
    public String getDetalles(String accion, String lugar, Usuario usuario, String extra) {
        nombre = usuario.getNombre() + " " + usuario.getApellido();
        
        String a2;
        if (accion.equalsIgnoreCase(INGRESO)) {
            a2 = " a ";
        } else if (accion.equalsIgnoreCase(AGREGO)) {
            a2 = " un ";
        } else if (accion.equalsIgnoreCase(EDITO)) {
            a2 = " un ";
        } else if (accion.equalsIgnoreCase(ELIMINO)) {
            a2 = " un ";
        } else {
            a2 = " ";
        }
        String detalle = nombre + " " + accion + a2 + lugar;
        
        if (lugar.equalsIgnoreCase(LOGEO)) {
            detalle = nombre + " ingreso al sistema con exito";
        } else if (lugar.equalsIgnoreCase(EMPRESA)) {
            detalle += " " + extra;
        } else if (lugar.equalsIgnoreCase(EMPLEADO)) {
            detalle = nombre + " " + accion + " a el empleado " + extra ;
        } else if (lugar.equalsIgnoreCase(CLIENTE)) {
            detalle = nombre + " " + accion + " a el cliente " + extra ;
        }  
        
        return detalle;
    } 
    
    public void saveRegistro(String accion, String lugar, Usuario usuario, String extra) {
        RegistroAcciones registro = new RegistroAcciones();
        registro.setDetalles(getDetalles(accion, lugar, usuario, extra));
        registro.setFecha(new Timestamp(new Date().getTime()));
        registro.setUsuario(usuario);
        registro.setAccionTipo((AccionTipo) new AccionTipoDAO().findAll().get(0));
        new RegistroAccionesDAO().save(registro);
    }
    
    public void saveIngreso(String detalles, Usuario usuario) {
        RegistroAcciones registro = new RegistroAcciones();
        registro.setDetalles(usuario.getNombre() + " " + usuario.getApellido() + " " + detalles);
        registro.setFecha(new Timestamp(new Date().getTime()));
        registro.setUsuario(usuario);
        registro.setAccionTipo((AccionTipo) new AccionTipoDAO().findAll().get(0));
        new RegistroAccionesDAO().save(registro);
    }
    
    public void saveAgrego(String detalles, Usuario usuario) {
        RegistroAcciones registro = new RegistroAcciones();
        registro.setDetalles(usuario.getNombre() + " " + usuario.getApellido() + " " + detalles);
        registro.setFecha(new Timestamp(new Date().getTime()));
        registro.setUsuario(usuario);
        registro.setAccionTipo((AccionTipo) new AccionTipoDAO().findAll().get(1));
        new RegistroAccionesDAO().save(registro);
    }
    
    public void saveEdito(String detalles, Usuario usuario) {
        RegistroAcciones registro = new RegistroAcciones();
        registro.setDetalles(usuario.getNombre() + " " + usuario.getApellido() + " " + detalles);
        registro.setFecha(new Timestamp(new Date().getTime()));
        registro.setUsuario(usuario);
        registro.setAccionTipo((AccionTipo) new AccionTipoDAO().findAll().get(2));
        new RegistroAccionesDAO().save(registro);
    }
    
    public void saveElimino(String detalles, Usuario usuario) {
        RegistroAcciones registro = new RegistroAcciones();
        registro.setDetalles(usuario.getNombre() + " " + usuario.getApellido() + " " + detalles);
        registro.setFecha(new Timestamp(new Date().getTime()));
        registro.setUsuario(usuario);
        registro.setAccionTipo((AccionTipo) new AccionTipoDAO().findAll().get(3));
        new RegistroAccionesDAO().save(registro);
    }
    
}
