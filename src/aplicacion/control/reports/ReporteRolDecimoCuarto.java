/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

import aplicacion.control.util.Fecha;
import aplicacion.control.util.Numeros;
import hibernate.model.RolIndividual;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Yornel
 */
public class ReporteRolDecimoCuarto implements JRDataSource {
    
    private final List<RolIndividual> lista = new ArrayList<>();
    private int indiceActual = -1;
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException { 
        
    Object valor = null;  

    if(null != jrField.getName()) 
        switch (jrField.getName()) {
            case "mes":
                Fecha fec = new Fecha(lista.get(indiceActual).getInicio());
                String fechaToTable = fec.getMonthNameCort()+" "+fec.getAno();
                valor = fechaToTable;
                break; 
            case "valor":
                Double monto = 0d;
                monto = lista.get(indiceActual).getDecimoCuarto();
                valor = Numeros.round(monto).toString();
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
    
    public void add(RolIndividual rol) {
        this.lista.add(rol);
    }

    public void addAll(List<RolIndividual> rol) {
        this.lista.addAll(rol);
    }
}
