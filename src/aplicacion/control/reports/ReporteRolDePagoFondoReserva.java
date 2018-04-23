/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Numeros;
import hibernate.model.PagoMesItem;
import hibernate.model.PagoQuincena;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Yornel
 */
public class ReporteRolDePagoFondoReserva implements JRDataSource {
    
    private final List<EmpleadoTable> lista = new ArrayList<>();
    private int indiceActual = -1;
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException { 
        
    Object valor = null;  

    if( null != jrField.getName()) switch (jrField.getName()) {
            case "descripcion":
                valor = "Pago de Fondo de Reserva";
                break; 
            case "ingresos":
                valor = Numeros.round(lista.get(indiceActual).getMonto());
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
    
    public void add(EmpleadoTable rol) {
        this.lista.add(rol);
    }

    public void addAll(List<EmpleadoTable> rol) {
        this.lista.addAll(rol);
    }
}
