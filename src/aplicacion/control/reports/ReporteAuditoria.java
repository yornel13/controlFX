/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

import aplicacion.control.util.Fechas;
import hibernate.model.RegistroAcciones;
import hibernate.model.Usuario;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Yornel
 */
public class ReporteAuditoria implements JRDataSource {
    
    private final List<RegistroAcciones> lista = new ArrayList<>();
    private int indiceActual = -1;
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException { 
        
    Object valor = null;  

    if( null != jrField.getName()) switch (jrField.getName()) {
            case "nombre":
                valor = lista.get(indiceActual).getUsuario().getNombre() + " " + 
                        lista.get(indiceActual).getUsuario().getApellido();
                break; 
            case "cedula":
                valor = lista.get(indiceActual).getUsuario().getCedula();
                break;
            case "fecha":
                valor = Fechas.getFechaConMesYHora(lista.get(indiceActual).getFecha());
                break;
            case "detalles":
                Usuario usuario = lista.get(indiceActual).getUsuario();
                String detalles = lista.get(indiceActual).getDetalles();
                String user = usuario.getNombre() + " " + usuario.getApellido() + " ";
                if (detalles.contains(user)) {
                    detalles = detalles.replaceFirst(user, "");
                }
                valor = detalles;
                break;
            case "accion":
                valor = lista.get(indiceActual).getAccionTipo().getNombre();
                break;
            default:
                break; 
        }
 
    return valor; 
    }

    @Override
    public boolean next() throws JRException {
        return ++indiceActual < lista.size(); 
    }
    
    public void add(RegistroAcciones lista) {
        this.lista.add(lista);
    }

    public void addAll(List<RegistroAcciones> lista) {
        this.lista.addAll(lista);
    }
}
