/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

import static aplicacion.control.util.Numeros.round;
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
public class DataSourceRolIndividual implements JRDataSource {
    
    private final List<RolIndividual> lista = new ArrayList<>();
    private int indiceActual = -1;
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException { 
        
    Object valor = null;  

    if( null != jrField.getName()) switch (jrField.getName()) {
            case "cedula":
                valor = lista.get(indiceActual).getUsuario().getCedula();
                break; 
            case "nombre":
                valor = lista.get(indiceActual).getUsuario().getNombre();
                break;
            case "apellido":
                valor = lista.get(indiceActual).getUsuario().getApellido();
                break;
            case "cargo":
                valor = lista.get(indiceActual).getUsuario()
                        .getDetallesEmpleado().getCargo().getNombre();
                break;
            case "horas_rc":
                valor = round(lista.get(indiceActual)
                        .getHorasSuplementarias());
                break;
            case "horas_st":
                valor = round(lista.get(indiceActual)
                        .getHorasSobreTiempo());
                break;
            case "monto_rc":
                valor = round(lista.get(indiceActual)
                        .getMontoHorasSuplementarias());
                break;
            case "monto_st":
                valor = round(lista.get(indiceActual)
                        .getMontoHorasSobreTiempo());
                break;
            case "monto_jubilacion":
                valor = round(lista.get(indiceActual)
                        .getJubilacionPatronal());
                break;
            case "monto_aporte":
                valor = round(lista.get(indiceActual)
                        .getAportePatronal());
                break;
            case "monto_reserva":
                valor = round(lista.get(indiceActual)
                        .getDecimoTercero());
                break;
            case "monto_vacaciones":
                valor = round(lista.get(indiceActual)
                        .getVacaciones());
                break;
            case "monto_uniforme":
                valor = round(lista.get(indiceActual)
                        .getUniformes());
                break;
            case "monto_seguro":
                valor = round(lista.get(indiceActual)
                        .getSeguros());
                break;
            case "monto_bono":
                valor = round(lista.get(indiceActual)
                        .getTotalBonos());
                break;
            case "monto_transporte":
                valor = round(lista.get(indiceActual)
                        .getTotalBonos());
                break;
            case "monto_decimo3":
                valor = round(lista.get(indiceActual)
                        .getDecimoTercero());
                break;
            case "monto_decimo4":
                valor = round(lista.get(indiceActual)
                        .getDecimoCuarto());
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
    
    public void add(RolIndividual lista) {
        this.lista.add(lista);
    }

    public void addAll(List<RolIndividual> lista) {
        this.lista.addAll(lista);
    }
}
