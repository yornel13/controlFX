/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.ReportModel;

/**
 *
 * @author Yornel
 */
public class RolPagoIndividual {
    
    private String descripscion;
    private Double deduccion;
    private Double ingreso;
    private Integer dias;
    private Integer horas;

    public String getDescripscion() {
        return descripscion;
    }

    public void setDescripscion(String descripscion) {
        this.descripscion = descripscion;
    }

    public Double getDeduccion() {
        return deduccion;
    }

    public void setDeduccion(Double deduccion) {
        this.deduccion = deduccion;
    }

    public Double getIngreso() {
        return ingreso;
    }

    public void setIngreso(Double ingreso) {
        this.ingreso = ingreso;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public Integer getHoras() {
        return horas;
    }

    public void setHoras(Integer horas) {
        this.horas = horas;
    }
    
    
    
}
