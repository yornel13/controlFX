/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.tableModel;

/**
 *
 * @author Yornel
 */
public class PagosTable {
    
    private Integer id;
    private String cliente;
    private Integer dias;
    private Integer normales;
    private Integer suplementarias;
    private Integer sobreTiempo;
    private Double sueldo;
    private Double extra;
    private Double bonos;
    private Double vacaciones;
    private Double subtotal;
    private Double decimos;
    private Double tercero;
    private Double cuarto;
    private Double total;

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

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public Integer getNormales() {
        return normales;
    }

    public void setNormales(Integer normales) {
        this.normales = normales;
    }

    public Integer getSuplementarias() {
        return suplementarias;
    }

    public void setSuplementarias(Integer suplementarias) {
        this.suplementarias = suplementarias;
    }

    public Integer getSobreTiempo() {
        return sobreTiempo;
    }

    public void setSobreTiempo(Integer sobreTiempo) {
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
    
    
    
}
