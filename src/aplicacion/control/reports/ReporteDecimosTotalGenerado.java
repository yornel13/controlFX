/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

import static aplicacion.control.DecimosAcumuladoEmpleadosController.PAGADO;
import static aplicacion.control.DecimosAcumuladoEmpleadosController.RETENIDO;
import aplicacion.control.util.Fechas;
import static aplicacion.control.util.Numeros.round;
import hibernate.model.RolIndividual;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import static aplicacion.control.util.Numeros.round;

/**
 *
 * @author Yornel
 */
public class ReporteDecimosTotalGenerado implements JRDataSource {
    
    private final List<RolIndividual> lista = new ArrayList<>();
    private int indiceActual = -1;
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException { 
        
    Object valor = null;  

    if( null != jrField.getName()) switch (jrField.getName()) {
            case "fecha":
                valor = Fechas.getFechaConMes(lista.get(indiceActual).getInicio())
                        + " al " + Fechas.getFechaConMes(lista.get(indiceActual).getFinalizo());
                break; 
            case "decimo3":
                valor = round(lista.get(indiceActual).getDecimoTercero()).toString();
                break;
            case "decimo4":
                valor = round(lista.get(indiceActual).getDecimoCuarto()).toString();
                break;
            case "estado3":
                if (lista.get(indiceActual).getDecimosPagado() || 
                        lista.get(indiceActual).getPagoDecimoTercero() != null) {
                    valor = PAGADO;
                } else {
                    valor = RETENIDO;
                }
                break;
            case "estado4":
                if (lista.get(indiceActual).getDecimosPagado() || 
                        lista.get(indiceActual).getPagoDecimoCuarto() != null) {
                    valor = PAGADO;
                } else {
                    valor = RETENIDO;
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
    
    public void add(RolIndividual object) {
        this.lista.add(object);
    }

    public void addAll(List<RolIndividual> objects) {
        this.lista.addAll(objects);
    }
}