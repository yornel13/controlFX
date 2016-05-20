/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

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
public class ReportTest1 implements JRDataSource {
    
    private List<Usuario> lista = new ArrayList<>();
    private int indiceActual = -1;
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException { 
    Object valor = null;  

    if( null != jrField.getName()) switch (jrField.getName()) {
            case "nombre":
                valor = lista.get(indiceActual).getNombre();
                break;
            case "apellido":
                valor = lista.get(indiceActual).getApellido();
                break;
            case "cedula":
                valor = lista.get(indiceActual).getCedula();
                break;
            case "sueldo":
                valor = lista.get(indiceActual).getDetallesEmpleado().getSueldo();
                break;
            case "cargo":
                valor = lista.get(indiceActual).getDetallesEmpleado().getCargo().getNombre();
                break;
            case "departamento":
                valor = lista.get(indiceActual).getDetallesEmpleado().getDepartamento().getNombre();
                break;
            case "decimos":
                valor = lista.get(indiceActual).getDetallesEmpleado().getAcumulaDecimos();
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
    
    public void add(Usuario usuario) {
        this.lista.add(usuario);
    }

    public void addAll(List<Usuario> usuarios) {
        this.lista.addAll(usuarios);
    }
}
