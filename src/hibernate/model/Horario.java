package hibernate.model;

// default package

import java.sql.Time;
import java.sql.Timestamp;

/**
 * ControlEmpleado entity. @author MyEclipse Persistence Tools
 */

public class Horario implements java.io.Serializable {

    // Fields

    private Integer id;
    private String nombre;
    private Double normales;
    private Double recargo;
    private Double sobretiempo;
    private Timestamp creacion;
    private Boolean medioDia;
    private Time entrada;
    private Time salida;

    // Constructors

    /** default constructor */
    public Horario() {
    }

    public Horario(Integer id, String nombre, Double normales, Double recargo, Double sobretiempo) {
        this.id = id;
        this.nombre = nombre;
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

    public Time getEntrada() {
        return entrada;
    }

    public void setEntrada(Time entrada) {
        this.entrada = entrada;
    }

    public Time getSalida() {
        return salida;
    }

    public void setSalida(Time salida) {
        this.salida = salida;
    }
    
    

}