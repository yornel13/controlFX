package hibernate.model;

// default package

import java.sql.Timestamp;
import java.util.Date;

/**
 * PagoQuincena entity. @author MyEclipse Persistence Tools
 */

public class PagoQuincena implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuario usuario;
	private Double monto;
	private Timestamp fecha;
	private Timestamp inicioMes;
	private Timestamp finMes;

	// Constructors

	/** default constructor */
	public PagoQuincena() {
	}

	/** minimal constructor */
	public PagoQuincena(Usuario usuario, Double monto, Timestamp fecha) {
		this.usuario = usuario;
		this.monto = monto;
		this.fecha = fecha;
	}

	/** full constructor */
	public PagoQuincena(Usuario usuario, Double monto, Timestamp fecha,
			Timestamp inicioMes, Timestamp finMes) {
		this.usuario = usuario;
		this.monto = monto;
		this.fecha = fecha;
		this.inicioMes = inicioMes;
		this.finMes = finMes;
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

	public Timestamp getInicioMes() {
		return this.inicioMes;
	}

	public void setInicioMes(Timestamp inicioMes) {
		this.inicioMes = inicioMes;
	}

	public Timestamp getFinMes() {
		return this.finMes;
	}

	public void setFinMes(Timestamp finMes) {
		this.finMes = finMes;
	}

}