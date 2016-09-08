package hibernate.model;

// default package

import java.sql.Timestamp;

/**
 * ControlEmpleado entity. @author MyEclipse Persistence Tools
 */

public class Horario implements java.io.Serializable {

    // Fields

    private Integer id;
    private String nombre;
    private Integer horaInicio;
    private Integer horaFin;
    private Double normales;
    private Double recargo;
    private Double sobretiempo;
    private Timestamp creacion;
    private Boolean medioDia;

    // Constructors

    /** default constructor */
    public Horario() {
    }

    public Horario(Integer id, String nombre, Integer horaInicio, Integer 
            horaFin, Double normales, Double recargo, Double sobretiempo) {
        this.id = id;
        this.nombre = nombre;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.normales = normales;
        this.recargo = recargo;
        this.sobretiempo = sobretiempo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Integer horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Integer getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Integer horaFin) {
        this.horaFin = horaFin;
    }

    public Double getNormales() {
        return normales;
    }

    public void setNormales(Double normales) {
        this.normales = normales;
    }

    public Double getRecargo() {
        return recargo;
    }

    public void setRecargo(Double recargo) {
        this.recargo = recargo;
    }

    public Double getSobretiempo() {
        return sobretiempo;
    }

    public void setSobretiempo(Double sobretiempo) {
        this.sobretiempo = sobretiempo;
    }

    public Timestamp getCreacion() {
        return creacion;
    }

    public void setCreacion(Timestamp creacion) {
        this.creacion = creacion;
    }

    public Boolean getMedioDia() {
        return medioDia;
    }

    public void setMedioDia(Boolean medioDia) {
        this.medioDia = medioDia;
    }
    
    

}