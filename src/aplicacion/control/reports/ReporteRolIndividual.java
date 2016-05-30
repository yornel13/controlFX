/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

import hibernate.model.Pago;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Yornel
 */
public class ReporteRolIndividual implements JRDataSource {
    
    private final List<Pago> lista = new ArrayList<>();
    private int indiceActual = -1;
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException { 
        
    Object valor = null;  

    if( null != jrField.getName()) switch (jrField.getName()) {
            case "cedula":
                valor = lista.get(indiceActual).getCedula();
                break; 
            case "cliente":
                valor = lista.get(indiceActual).getClienteNombre();
                break;
            case "cargo":
                valor = lista.get(indiceActual).getUsuario().getDetallesEmpleado().getCargo().getNombre();
                break;
            case "cuenta":
                valor = "Cuenta Ahorra #" + lista.get(indiceActual).getUsuario().getDetallesEmpleado().getNroCuenta();
                break;
             case "dias":
                valor = lista.get(indiceActual).getDias();
                break;
            case "salario":
                valor = lista.get(indiceActual).getSalario();
                break;
            case "nl":
                valor = lista.get(indiceActual).getHorasNormales();
                break;
            case "sobretiempo":
                valor = lista.get(indiceActual).getMontoHorasSobreTiempo();
                break;
            case "st":
                valor = lista.get(indiceActual).getHorasSobreTiempo();
                break;
            case "recargo":
                valor = lista.get(indiceActual).getMontoHorasSuplementarias();
                break;
            case "rc":
                valor = lista.get(indiceActual).getHorasSuplementarias();
                break;
            case "transporte":
                valor = lista.get(indiceActual).getTransporte();
                break;
            case "vacaciones":
                valor = lista.get(indiceActual).getVacaciones();
                break;
            case "subtotal":
                valor = lista.get(indiceActual).getSubtotal();
                break;
            case "tercero":
                valor = lista.get(indiceActual).getDecimoTercero();
                break;
            case "cuarto":
                valor = lista.get(indiceActual).getDecimoCuarto();
                break;
            case "reserva":
                valor = lista.get(indiceActual).getDecimoTercero();
                break;
            case "jubilacion":
                valor = lista.get(indiceActual).getJubilacionPatronal();
                break;
            case "aporte":
                valor = lista.get(indiceActual).getAportePatronal();
                break;
            case "seguro":
                valor = lista.get(indiceActual).getSeguros();
                break;
            case "uniforme":
                valor = lista.get(indiceActual).getUniformes();
                break;
            case "total":
                valor = lista.get(indiceActual).getTotalIngreso();
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
    
    public void add(Pago pago) {
        this.lista.add(pago);
    }

    public void addAll(List<Pago> pagos) {
        this.lista.addAll(pagos);
    }
}
