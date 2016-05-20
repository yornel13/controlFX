/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

import static aplicacion.control.EmpleadoController.getMonthName;
import hibernate.model.Deuda;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.joda.time.DateTime;

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
                valor = lista.get(indiceActual).getMonto().toString();
                break;
            case "restante":
                valor = lista.get(indiceActual).getRestante().toString();
                break;
            case "fecha":
                DateTime time = new DateTime(lista.get(indiceActual).getCreacion().getTime());
                valor = time.getDayOfMonth() + " de " + getMonthName(time.getMonthOfYear()) + " " + time.getYear();
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
        for (Deuda deuda: deudas) {
            System.out.println(deuda.getMonto());
        }
        this.lista.addAll(deudas);
    }
}
