/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

import aplicacion.control.util.Fechas;
import hibernate.model.Deuda;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Yornel
 */
public class ReporteDeudas implements JRDataSource {
    
    private final List<Deuda> lista = new ArrayList<>();
    private int indiceActual = -1;
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException { 
        
    Object valor = null;  

    if( null != jrField.getName()) switch (jrField.getName()) {
            case "tipo":
                valor = lista.get(indiceActual).getTipo();
                break; 
            case "detalles":
                valor = lista.get(indiceActual).getDetalles();
                break;
            case "monto":
                valor = lista.get(indiceActual).getMonto();
                break;
            case "restante":
                valor = lista.get(indiceActual).getRestante();
                break;
             case "cuotas":
                valor = lista.get(indiceActual).getCuotas();
                break;
            case "fecha":
                valor = Fechas.getFechaConMes(lista.get(indiceActual).getCreacion());
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
    
    public void add(Deuda deuda) {
        this.lista.add(deuda);
    }

    public void addAll(List<Deuda> deudas) {
        this.lista.addAll(deudas);
    }
}
