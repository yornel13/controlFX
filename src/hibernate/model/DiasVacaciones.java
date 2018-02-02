package hibernate.model;

// default package

import java.sql.Timestamp;


/**
 * Actuariales entity. @author MyEclipse Persistence Tools
 */

public class DiasVacaciones implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuario usuario;
	private Integer dias;
	private Boolean activo;
        private Boolean aumentado;
        private Timestamp fecha;

	// Constructors

	/** default constructor */
	public DiasVacaciones() {
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

    public Integer getDias() {
        return dias-1;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getAumentado() {
        return aumentado;
    }

    public void setAumentado(Boolean aumentado) {
        this.aumentado = aumentado;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
        
        

}