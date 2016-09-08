/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.tableModel;

import hibernate.model.ControlEmpleado;
import hibernate.model.Usuario;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class ControlTable {
    
    private Integer id;
    private Usuario usuario;
    private String cliente;
    private String fechaString;
    private Double normales;
    private Double sobreTiempo;
    private Double recargo;
    private String observacion;
    private String dia;
    private ControlEmpleado controlEmpleado;
    private DateTime fecha;
    private Boolean ajeno;
    public Boolean marcar;

    public ControlTable() {
        ajeno = false;
        marcar = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public ControlEmpleado getControlEmpleado() {
        return controlEmpleado;
    }

    public void setControlEmpleado(ControlEmpleado controlEmpleado) {
        this.controlEmpleado = controlEmpleado;
    }

    public String getFechaString() {
        return fechaString;
    }

    public void setFechaString(String fechaString) {
        this.fechaString = fechaString;
    }

    public DateTime getFecha() {
        return fecha;
    }

    public void setFecha(DateTime fecha) {
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

    
    
}
