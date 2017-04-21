/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

import aplicacion.control.util.Const;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import hibernate.model.ControlDiario;
import hibernate.model.ControlExtras;
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
public class ReporteHorasTrabajadas implements JRDataSource {
    
    private final List<ControlExtras> lista = new ArrayList<>();
    private int indiceActual = -1;
    
    private Fecha inicio;
    private Fecha fin;
    
    public ReporteHorasTrabajadas(Fecha inicio, Fecha fin) {
        this.inicio = inicio;
        this.fin = fin;
    }
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException { 
        
    Object valor = null;  

    if( null != jrField.getName()) switch (jrField.getName()) {
            case "dia":
                DateTime dateTime = new DateTime(lista.get(indiceActual).getFecha().getTime());
                valor = Fechas.getFechaConMesSinAno(dateTime);
                break; 
            case "cliente":
                if (lista.get(indiceActual).getCliente() != null)
                    valor = lista.get(indiceActual).getCliente().getNombre();
                break;
            case "sobretiempo":
                valor = lista.get(indiceActual).getSobretiempo().toString();
                break;
            case "recargo":
                    valor = lista.get(indiceActual).getRecargo().toString();
                break;
            case "observacion":
                ControlExtras control = lista.get(indiceActual);
                if (control.getCaso().equals(Const.LIBRE)) {
                   valor = "Libre"; 
                } else if (control.getCaso().equals(Const.FALTA)) {
                   valor = "Falta"; 
                } else if (control.getCaso().equals(Const.VACACIONES)) {
                   valor = "Vacaciones"; 
                } else if (control.getCaso().equals(Const.PERMISO)) {
                   valor = "Permiso"; 
                } else if (control.getCaso().equals(Const.DM)) {
                    valor = "D. Medico"; 
                } else if (control.getCaso().equals(Const.CM)) {
                   valor = "C. Medica"; 
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
    
    public void add(ControlExtras rol) {
        this.lista.add(rol);
    }

    public void addAll(List<ControlExtras> rol) {
        this.lista.addAll(rol);
    }
}
