/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.tableModel;

import hibernate.model.Deuda;
import hibernate.model.PagoMes;

/**
 *
 * @author Yornel
 */
public class AbonoDeudaTable {
    
    private Integer id;
    private Deuda deuda;
    private PagoMes pagoMes;
    private Double monto;
    private Double restante;
    private String fecha;
    private String numeroPago;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Deuda getDeuda() {
        return deuda;
    }

    public void setDeuda(Deuda deuda) {
        this.deuda = deuda;
    }

    public PagoMes getPagoMes() {
        return pagoMes;
    }

    public void setPagoMes(PagoMes pagoMes) {
        this.pagoMes = pagoMes;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Double getRestante() {
        return restante;
    }

    public void setRestante(Double restante) {
        this.restante = restante;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNumeroPago() {
        return numeroPago;
    }

    public void setNumeroPago(String numeroPago) {
        this.numeroPago = numeroPago;
    }
    
    
    
}
