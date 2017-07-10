/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.tableModel;

import aplicacion.control.util.Fecha;
import hibernate.model.CuotaDeuda;
import hibernate.model.Deuda;
import hibernate.model.PagoMes;

/**
 *
 * @author Yornel
 */
public class CuotaDeudaTable {
    
    private Integer id;
    private Deuda deuda;
    private PagoMes pagoMes;
    private Fecha fecha;
    private Double monto;
    private Double restante;
    private String detalles;
    private String fechaString;
    private String numeroPago;
    private CuotaDeuda cuotaDeuda;
    private Integer cuotas;
    private String tipo;

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

    public String getNumeroPago() {
        return numeroPago;
    }

    public void setNumeroPago(String numeroPago) {
        this.numeroPago = numeroPago;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Fecha fecha) {
        this.fecha = fecha;
    }

    public String getFechaString() {
        return fechaString;
    }

    public void setFechaString(String fechaString) {
        this.fechaString = fechaString;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public CuotaDeuda getCuotaDeuda() {
        return cuotaDeuda;
    }

    public void setCuotaDeuda(CuotaDeuda cuotaDeuda) {
        this.cuotaDeuda = cuotaDeuda;
    }

    public Integer getCuotas() {
        return cuotas;
    }

    public void setCuotas(Integer cuotas) {
        this.cuotas = cuotas;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    
}
