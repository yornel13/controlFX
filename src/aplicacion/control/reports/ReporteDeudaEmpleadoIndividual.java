/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

import aplicacion.control.util.Fechas;
import hibernate.model.CuotaDeuda;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Yornel
 */
public class ReporteDeudaEmpleadoIndividual implements JRDataSource {
    
    private final List<CuotaDeuda> lista = new ArrayList<>();
    private int indiceActual = -1;
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException { 
        
    Object valor = null;  

    if( null != jrField.getName()) switch (jrField.getName()) {
            case "fecha":
                valor = Fechas.getFechaConMes(lista.get(indiceActual).getFecha());
                break; 
            case "codigo":
                if (lista.get(indiceActual).getPagoMes() != null)
                    valor = "N-" + lista.get(indiceActual).getPagoMes().getId().toString();
                else
                    valor = "Pendiente";
                break;
            case "abonado":
                valor = lista.get(indiceActual).getMonto();
                break;
            case "restante":
                valor = lista.get(indiceActual).getDetalles();
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
    
    public void add(CuotaDeuda object) {
        this.lista.add(object);
    }

    public void addAll(List<CuotaDeuda> objects) {
        this.lista.addAll(objects);
    }
}
