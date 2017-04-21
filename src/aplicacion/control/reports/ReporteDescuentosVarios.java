/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

import aplicacion.control.tableModel.DescuentoTable;
import aplicacion.control.util.Numeros;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Yornel
 */
public class ReporteDescuentosVarios implements JRDataSource {
    
    private final List<DescuentoTable> lista = new ArrayList<>();
    private int indiceActual = -1;
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException { 
        
    Object valor = null;  

    if( null != jrField.getName()) switch (jrField.getName()) {
            case "cedula":
                valor = lista.get(indiceActual).getCedula();
                break; 
            case "nombres":
                valor = lista.get(indiceActual).getNombres();
                break;
            case "valor":
                if (lista.get(indiceActual).getValor()!= null) {
                    valor = Numeros.round(lista.get(indiceActual).getValor()).toString();
                }
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
    
    public void add(DescuentoTable lista) {
        this.lista.add(lista);
    }

    public void addAll(List<DescuentoTable> lista) {
        this.lista.addAll(lista);
    }
}
