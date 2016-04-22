package hibernate.model;

// default package

import java.sql.Timestamp;

/**
 * RegistroAcciones entity. @author MyEclipse Persistence Tools
 */

public class RegistroAcciones implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuario usuario;
	private AccionTipo accionTipo;
	private String detalles;
	private Timestamp fecha;

	// Constructors

	/** default constructor */
	public RegistroAcciones() {
	}

	/** minimal constructor */
	public RegistroAcciones(Usuario usuario, AccionTipo accionTipo,
			Timestamp fecha) {
		this.usuario = usuario;
		this.accionTipo = accionTipo;
		this.fecha = fecha;
	}

	/** full constructor */
	public RegistroAcciones(Usuario usuario, AccionTipo accionTipo,
			String detalles, Timestamp fecha) {
		this.usuario = usuario;
		this.accionTipo = accionTipo;
		this.detalles = detalles;
		this.fecha = fecha;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public AccionTipo getAccionTipo() {
		return this.accionTipo;
	}

	public void setAccionTipo(AccionTipo accionTipo) {
		this.accionTipo = accionTipo;
	}

	public String getDetalles() {
		return this.detalles;
	}

	public void setDetalles(String detalles) {
		this.detalles = detalles;
	}

	public Timestamp getFecha() {
		return this.fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

}