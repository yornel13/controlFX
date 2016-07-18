/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

import aplicacion.control.PagoDecimoTerceroController;
import aplicacion.control.util.Const;
import hibernate.model.PagoDecimo;
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
public class ReporteRolDePagoDecimo implements JRDataSource {
    
    private final List<PagoDecimo> lista = new ArrayList<>();
    private int indiceActual = -1;
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException { 
        
    Object valor = null;  

    if( null != jrField.getName()) switch (jrField.getName()) {
            case "descripcion":
                if (lista.get(indiceActual).getDecimo()
                        .equalsIgnoreCase(Const.DECIMO_TERCERO)) {
                    valor = "Pago de decimo tercero acumulado";
                } else if (lista.get(indiceActual).getDecimo()
                        .equalsIgnoreCase(Const.DECIMO_CUARTO)) {
                    valor = "Pago de decimo cuarto acumulado";
                }
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
    
    public void add(PagoDecimo object) {
        this.lista.add(object);
    }

    public void addAll(List<PagoDecimo> objects) {
        this.lista.addAll(objects);
    }
}
