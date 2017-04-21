/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.tableModel;

import aplicacion.control.util.Fecha;
import hibernate.model.ControlDiario;
import hibernate.model.ControlExtras;
import hibernate.model.Usuario;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class ControlTable {
    
    private Long id;
    private Usuario usuario;
    private String cliente;
    private String fechaString;
    private Double normales;
    private Double sobreTiempo;
    private Double recargo;
    private String observacion;
    private String dia;
    private ControlDiario controlDiario;
    private ControlExtras controlExtras;
    private DateTime fechaExtra;
    private Fecha fecha;
    private Boolean ajeno;
    public Boolean marcar;

    public ControlTable() {
        ajeno = false;
        marcar = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public Double getNormales() {
        return normales;
    }

    public void setNormales(Double normales) {
        this.normales = normales;
    }

    public Double getSobreTiempo() {
        return sobreTiempo;
    }

    public void setSobreTiempo(Double sobreTiempo) {
        this.sobreTiempo = sobreTiempo;
    }

    public Double getRecargo() {
        return recargo;
    }

    public void setRecargo(Double recargo) {
        this.recargo = recargo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public ControlDiario getControlDiario() {
        return controlDiario;
    }

    public void setControlEmpleado(ControlDiario controlDiario) {
        this.controlDiario = controlDiario;
    }

    public String getFechaString() {
        return fechaString;
    }

    public void setFechaString(String fechaString) {
        this.fechaString = fechaString;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Fecha fecha) {
        this.fecha = fecha;
    }

    public Boolean esAjeno() {
        return ajeno;
    }

    public void setAjeno(Boolean ajeno) {
        this.ajeno = ajeno;
    }

    public Boolean getMarcar() {
        return marcar;
    }

    public void setMarcar(Boolean marcar) {
        this.marcar = marcar;
    }

    public DateTime getFechaExtra() {
        return fechaExtra;
    }

    public void setFechaExtra(DateTime fechaExtra) {
        this.fechaExtra = fechaExtra;
    }

    public ControlExtras getControlExtras() {
        return controlExtras;
    }

    public void setControlExtras(ControlExtras controlExtras) {
        this.controlExtras = controlExtras;
    }

    
    
}
