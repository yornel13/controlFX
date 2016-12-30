/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

import aplicacion.control.util.Const;
import hibernate.model.ControlEmpleado;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class ReporteHorasTrabajadas implements JRDataSource {
    
    private final List<ControlEmpleado> lista = new ArrayList<>();
    private int indiceActual = -1;
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException { 
        
    Object valor = null;  

    if( null != jrField.getName()) switch (jrField.getName()) {
            case "dia":
                DateTime fecha = new DateTime(lista.get(indiceActual).getFecha().getTime());
                String dia = fecha.toCalendar(Locale.getDefault())
                            .getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                valor = dia + " " + fecha.getDayOfMonth();
                break; 
            case "cliente":
                if (lista.get(indiceActual).getCliente() != null)
                    valor = lista.get(indiceActual).getCliente().getNombre();
                break;
            case "normales":
                valor = lista.get(indiceActual).getNormales().toString();
                break;
            case "sobretiempo":
                valor = lista.get(indiceActual).getSobretiempo().toString();
                break;
            case "recargo":
                valor = lista.get(indiceActual).getRecargo().toString();
                break;
            case "observacion":
                ControlEmpleado control = lista.get(indiceActual);
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
                } else if (control.getMedioDia()) {
                    valor = "1/2 Dia"; 
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
    
    public void add(ControlEmpleado rol) {
        this.lista.add(rol);
    }

    public void addAll(List<ControlEmpleado> rol) {
        this.lista.addAll(rol);
    }
}
