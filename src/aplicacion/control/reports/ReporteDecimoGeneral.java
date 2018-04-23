/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

import aplicacion.control.tableModel.EmpleadoTable;
import static aplicacion.control.util.Numeros.round;
import hibernate.model.RolIndividual;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import static aplicacion.control.util.Numeros.round;

/**
 *
 * @author Yornel
 */
public class ReporteDecimoGeneral implements JRDataSource {
    
    private final List<EmpleadoTable> lista = new ArrayList<>();
    private int indiceActual = -1;
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException { 
        
    Object valor = null;  

    if( null != jrField.getName()) switch (jrField.getName()) {
            case "cedula":
                valor = lista.get(indiceActual).getUsuario().getCedula();
                break; 
            case "nombre":
                valor = lista.get(indiceActual).getUsuario().getNombre();
                break;
            case "apellido":
                valor = lista.get(indiceActual).getUsuario().getApellido();
                break;
            case "cargo":
                valor = lista.get(indiceActual).getUsuario()
                        .getDetallesEmpleado().getCargo().getNombre();
                break;
            case "monto_decimo3":
                valor = round(lista.get(indiceActual)
                        .getDecimo3());
                break;
            case "monto_decimo4":
                valor = round(lista.get(indiceActual)
                        .getDecimo4());
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
    
    public void add(EmpleadoTable lista) {
        this.lista.add(lista);
    }

    public void addAll(List<EmpleadoTable> lista) {
        this.lista.addAll(lista);
    }
}
