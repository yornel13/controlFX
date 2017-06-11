/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.reports;

import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.Numeros;
import hibernate.model.PagoMesItem;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Yornel
 */
public class ReporteRolGeneralMensual implements JRDataSource {
    
    private final List<EmpleadoTable> lista = new ArrayList<>();
    private int indiceActual = -1;
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException { 
        
    Object valor = null;  

    if( null != jrField.getName()) switch (jrField.getName()) {
            case "nombre":
                valor = lista.get(indiceActual).getUsuario().getApellido()
                        +" "+lista.get(indiceActual).getUsuario().getNombre();
                break;
            case "dias":
                valor = lista.get(indiceActual).getRolIndividual().getDias().toString();
                break;
            case "salario":
                valor = Numeros.round(lista.get(indiceActual).getRolIndividual().getSalario()).toString();
                break;
            case "sobretiempo":
                valor = Numeros.round(lista.get(indiceActual).getRolIndividual().getMontoHorasSobreTiempo()).toString();
                break;
            case "recargo":
                valor = Numeros.round(lista.get(indiceActual).getRolIndividual().getMontoHorasSuplementarias()).toString();
                break;
            case "transporte":
                Double transporte = lista.get(indiceActual).getRolIndividual().getTransporte();
                if (transporte > 0d) 
                    valor = Numeros.round(transporte).toString();
                else
                    valor = "-";
                break;
            case "subtotal":
                valor = Numeros.round(lista.get(indiceActual).getRolIndividual().getSubtotal()).toString();
                break;
            case "tercero":
                Double tercero = null;
                for (PagoMesItem pagoMesItem: lista.get(indiceActual).getPagoMesItems()){
                    if (pagoMesItem.getClave().equalsIgnoreCase(Const.IP_DECIMO_TERCERO)) {
                        tercero = pagoMesItem.getIngreso();
                    }
                }
                if (tercero == null) 
                   valor = ""; 
                else 
                    valor = Numeros.round(tercero).toString();
                break;
            case "cuarto":
                Double cuarto = null;
                for (PagoMesItem pagoMesItem: lista.get(indiceActual).getPagoMesItems()){
                    if (pagoMesItem.getClave().equalsIgnoreCase(Const.IP_DECIMO_CUARTO)) {
                        cuarto = pagoMesItem.getIngreso();
                    }
                }
                if (cuarto == null) 
                   valor = ""; 
                else 
                    valor = Numeros.round(cuarto).toString();
                break;
            case "ingresos":
                Double ingresos = 0d;
                for (PagoMesItem pagoMesItem: lista.get(indiceActual).getPagoMesItems()){
                    if (pagoMesItem.getIngreso() != null) {
                        ingresos += pagoMesItem.getIngreso();
                    }
                }
                valor = Numeros.round(ingresos).toString();
                break;
            case "anticipo":
                Double anticipo = 0d;
                for (PagoMesItem pagoMesItem: lista.get(indiceActual).getPagoMesItems()){
                    if (pagoMesItem.getClave().equalsIgnoreCase(Const.IP_ADELANTO_QUINCENAL)) {
                        anticipo = pagoMesItem.getDeduccion();
                    }
                }
                valor = Numeros.round(anticipo).toString();
                break;
            case "iess":
                Double iess = 0d;
                for (PagoMesItem pagoMesItem: lista.get(indiceActual).getPagoMesItems()){
                    if (pagoMesItem.getClave().equalsIgnoreCase(Const.IP_IESS)) {
                        iess = pagoMesItem.getDeduccion();
                    }
                }
                valor = Numeros.round(iess).toString();
                break;
            case "descuentos":
                Double descuentos = 0d;
                for (PagoMesItem pagoMesItem: lista.get(indiceActual).getPagoMesItems()){
                    if (pagoMesItem.getClave().equalsIgnoreCase(Const.IP_DEUDA)) {
                        descuentos += pagoMesItem.getDeduccion();
                    }
                }
                valor = Numeros.round(descuentos).toString();
                break;
            case "neto":
                valor = Numeros.round(lista.get(indiceActual).getPagoMes().getMonto()).toString();
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
    
    public void add(EmpleadoTable pago) {
        this.lista.add(pago);
    }

    public void addAll(List<EmpleadoTable> pagos) {
        this.lista.addAll(pagos);
    }
}
