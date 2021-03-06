/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.tableModel;

import aplicacion.control.util.Fecha;

/**
 *
 * @author Yornel
 */
public class PagosTable {
    
    private Integer id;
    private String cliente;
    private Double dias;
    private Double normales;
    private Double suplementarias;
    private Double sobreTiempo;
    private Double sueldo;
    private Double extra;
    private Double bonos;
    private Double vacaciones;
    private Double subtotal;
    private Double decimos;
    private Double tercero;
    private Double cuarto;
    private Double total;
    private String inicio;
    private String finalizo;
    private String devengado;
    private Boolean modificar;
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public Double getDias() {
        return dias;
    }

    public void setDias(Double dias) {
        this.dias = dias;
    }

    public Double getNormales() {
        return normales;
    }

    public void setNormales(Double normales) {
        this.normales = normales;
    }

    public Double getSuplementarias() {
        return suplementarias;
    }

    public void setSuplementarias(Double suplementarias) {
        this.suplementarias = suplementarias;
    }

    public Double getSobreTiempo() {
        return sobreTiempo;
    }

    public void setSobreTiempo(Double sobreTiempo) {
        this.sobreTiempo = sobreTiempo;
    }

    public Double getSueldo() {
        return sueldo;
    }

    public void setSueldo(Double sueldo) {
        this.sueldo = sueldo;
    }

    public Double getExtra() {
        return extra;
    }

    public void setExtra(Double extra) {
        this.extra = extra;
    }

    public Double getBonos() {
        return bonos;
    }

    public void setBonos(Double bonos) {
        this.bonos = bonos;
    }

    public Double getVacaciones() {
        return vacaciones;
    }

    public void setVacaciones(Double vacaciones) {
        this.vacaciones = vacaciones;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getDecimos() {
        return decimos;
    }

    public void setDecimos(Double decimos) {
        this.decimos = decimos;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getTercero() {
        return tercero;
    }

    public void setTercero(Double tercero) {
        this.tercero = tercero;
    }

    public Double getCuarto() {
        return cuarto;
    }

    public void setCuarto(Double cuarto) {
        this.cuarto = cuarto;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFinalizo() {
        return finalizo;
    }

    public void setFinalizo(String finalizo) {
        this.finalizo = finalizo;
    }

    public String getDevengado() {
        return devengado;
    }

    public void setDevengado(String devengado) {
        this.devengado = devengado;
    }

    public Boolean getModificar() {
        return modificar;
    }

    public void setModificar(Boolean modificar) {
        this.modificar = modificar;
    }

    
    
    
    
}
