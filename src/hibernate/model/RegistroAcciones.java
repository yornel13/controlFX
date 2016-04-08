package hibernate.model;

import java.sql.Timestamp;

/**
 * RegistroAcciones entity. @author MyEclipse Persistence Tools
 */

public class RegistroAcciones implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuarios usuarios;
	private AccionTipo accionTipo;
	private String detalles;
	private Timestamp fecha;

	// Constructors

	/** default constructor */
	public RegistroAcciones() {
	}

	/** minimal constructor */
	public RegistroAcciones(Usuarios usuarios, AccionTipo accionTipo,
			Timestamp fecha) {
		this.usuarios = usuarios;
		this.accionTipo = accionTipo;
		this.fecha = fecha;
	}

	/** full constructor */
	public RegistroAcciones(Usuarios usuarios, AccionTipo accionTipo,
			String detalles, Timestamp fecha) {
		this.usuarios = usuarios;
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

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
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