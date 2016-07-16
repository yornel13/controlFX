/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

import static aplicacion.control.util.Numeros.round;
import hibernate.model.PagoMesItem;
import hibernate.model.RolCliente;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Yornel
 */
public class ReporteDecimosGenerado implements JRDataSource {
    
    private final List<RolCliente> lista = new ArrayList<>();
    private int indiceActual = -1;
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException { 
        
    Object valor = null;  

    if( null != jrField.getName()) switch (jrField.getName()) {
            case "cliente":
                valor = lista.get(indiceActual).getClienteNombre();
                break; 
            case "horas":
                valor = String.valueOf(lista.get(indiceActual).getHorasNormales() 
                        + lista.get(indiceActual).getTotalHorasExtras());
                break;
            case "dias":
                valor = lista.get(indiceActual).getDias().toString();
                break;
            case "decimo3":
                valor = lista.get(indiceActual).getDecimoTercero().toString();
                break;
            case "decimo4":
                valor = lista.get(indiceActual).getDecimoCuarto().toString();
                break;
            case "suma":
                valor = round(lista.get(indiceActual).getDecimoTercero() +
                        lista.get(indiceActual).getDecimoCuarto()).toString();
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
    
    public void add(RolCliente object) {
        this.lista.add(object);
    }

    public void addAll(List<RolCliente> objects) {
        this.lista.addAll(objects);
    }
}