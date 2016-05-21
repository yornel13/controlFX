/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

import aplicacion.control.ReportModel.RolPagoIndividual;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Yornel
 */
public class ReporteRolDePagoIndividual implements JRDataSource {
    
    private final List<RolPagoIndividual> lista = new ArrayList<>();
    private int indiceActual = -1;
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException { 
        
    Object valor = null;  

    if( null != jrField.getName()) switch (jrField.getName()) {
            case "descripcion":
                valor = lista.get(indiceActual).getDescripscion();
                break; 
            case "ingresos":
                valor = lista.get(indiceActual).getIngreso();
                break;
            case "deducciones":
                valor = lista.get(indiceActual).getDeduccion();
                break;
            case "dias":
                valor = lista.get(indiceActual).getDias();
                break;
            case "horas":
                valor = lista.get(indiceActual).getHoras();
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
    
    public void add(RolPagoIndividual rol) {
        this.lista.add(rol);
    }

    public void addAll(List<RolPagoIndividual> rol) {
        this.lista.addAll(rol);
    }
}
