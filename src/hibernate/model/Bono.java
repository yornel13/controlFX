package hibernate.model;

// default package

import java.sql.Timestamp;

/**
 * Bono entity. @author MyEclipse Persistence Tools
 */

public class Bono implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuario usuario;
	private String concepto;
	private String detalles;
	private Double monto;
	private Timestamp fecha;
	private Boolean activo;

	// Constructors

	/** default constructor */
	public Bono() {
	}

	/** full constructor */
	public Bono(Usuario usuario, String concepto, String detalles,
			Double monto, Timestamp fecha, Boolean activo) {
		this.usuario = usuario;
		this.concepto = concepto;
		this.detalles = detalles;
		this.monto = monto;
		this.fecha = fecha;
		this.activo = activo;
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

	public String getConcepto() {
		return this.concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getDetalles() {
		return this.detalles;
	}

	public void setDetalles(String detalles) {
		this.detalles = detalles;
	}

	public Double getMonto() {
		return this.monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public Timestamp getFecha() {
		return this.fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public Boolean getActivo() {
		return this.activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

}