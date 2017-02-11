/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

import aplicacion.control.util.Const;
import aplicacion.control.util.Fecha;
import hibernate.model.ControlDiario;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Yornel
 */
public class ReporteHorasTrabajadas implements JRDataSource {
    
    private final List<ControlDiario> lista = new ArrayList<>();
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
                Fecha fecha = new Fecha(lista.get(indiceActual).getFecha());
                valor = fecha.getDiaInt() + " " + fecha.getMonthName();
                break; 
            case "cliente":
                if (lista.get(indiceActual).getCliente() != null)
                    valor = lista.get(indiceActual).getCliente().getNombre();
                break;
            case "normales":
                if (new Fecha(lista.get(indiceActual).getFecha()).afterEquals(inicio)) {
                    valor = lista.get(indiceActual).getNormales().toString();
                } else {
                    valor = "";
                }
                break;
            case "sobretiempo":
                if (new Fecha(lista.get(indiceActual).getFecha()).before(fin.minusDays(6))) {
                    valor = lista.get(indiceActual).getSobretiempo().toString();
                } else {
                    valor = "";
                }
                break;
            case "recargo":
                if (new Fecha(lista.get(indiceActual).getFecha()).before(fin.minusDays(6))) {
                    valor = lista.get(indiceActual).getRecargo().toString();
                } else {
                    valor = "";
                }
                break;
            case "observacion":
                ControlDiario control = lista.get(indiceActual);
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
    
    public void add(ControlDiario rol) {
        this.lista.add(rol);
    }

    public void addAll(List<ControlDiario> rol) {
        this.lista.addAll(rol);
    }
}
