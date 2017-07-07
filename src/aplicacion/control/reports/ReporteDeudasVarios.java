/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

import aplicacion.control.tableModel.EmpleadoTable;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Yornel
 */
public class ReporteDeudasVarios implements JRDataSource {
    
    private final List<EmpleadoTable> lista = new ArrayList<>();
    private int indiceActual = -1;
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException { 
        
    Object valor = null;  

    if( null != jrField.getName()) switch (jrField.getName()) {
            case "cedula":
                valor = lista.get(indiceActual).getCedula();
                break; 
            case "nombre":
                valor = lista.get(indiceActual).getNombre();
                break;
            case "apellido":
                valor = lista.get(indiceActual).getApellido();
                break;
            case "cargo":
                valor = lista.get(indiceActual).getCargo();
                break;
             case "deuda":
                valor = lista.get(indiceActual).getTotalMontoDeudas();
                break;
            case "cantidad":
                valor = lista.get(indiceActual).getTotalDeudas();
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
