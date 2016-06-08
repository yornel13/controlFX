/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

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
public class ReporteRolDePagoQuincenal implements JRDataSource {
    
    private final List<PagoQuincena> lista = new ArrayList<>();
    private int indiceActual = -1;
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException { 
        
    Object valor = null;  

    if( null != jrField.getName()) switch (jrField.getName()) {
            case "descripcion":
                valor = "Pago de adelanto quincenal";
                break; 
            case "ingresos":
                valor = lista.get(indiceActual).getMonto();
                break;
            case "deducciones":
                break;
            case "dias":
                break;
            case "horas":
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
    
    public void add(PagoQuincena rol) {
        this.lista.add(rol);
    }

    public void addAll(List<PagoQuincena> rol) {
        this.lista.addAll(rol);
    }
}
