/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.tableModel;

import hibernate.model.Usuario;
import java.sql.Timestamp;

/**
 *
 * @author Yornel
 */
public class DeudaTable {
    private Integer id;
    private Usuario usuario;
    private String tipo;
    private String detalles;
    private Double monto;
    private Double restante;
    private Integer cuotas;
    private Timestamp creacion;
    private Timestamp ultimaModificacion;
    private Boolean pagada;
    private Boolean aplazar;
    private String fecha;

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
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

    public Integer getCuotas() {
        return cuotas;
    }

    public void setCuotas(Integer cuotas) {
        this.cuotas = cuotas;
    }

    public Timestamp getCreacion() {
        return creacion;
    }

    public void setCreacion(Timestamp creacion) {
        this.creacion = creacion;
    }

    public Timestamp getUltimaModificacion() {
        return ultimaModificacion;
    }

    public void setUltimaModificacion(Timestamp ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    public Boolean getPagada() {
        return pagada;
    }

    public void setPagada(Boolean pagada) {
        this.pagada = pagada;
    }

    public Boolean getAplazar() {
        return aplazar;
    }

    public void setAplazar(Boolean aplazar) {
        this.aplazar = aplazar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
    
}
