package hibernate.model;

// default package

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * PagoMes entity. @author MyEclipse Persistence Tools
 */

public class PagoMes implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuario usuario;
	private Double monto;
	private Timestamp fecha;
	private Timestamp inicioMes;
	private Timestamp finMes;
	private Set abonoDeudas = new HashSet(0);

	// Constructors

	/** default constructor */
	public PagoMes() {
	}

	/** minimal constructor */
	public PagoMes(Usuario usuario, Double monto, Timestamp fecha,
			Timestamp inicioMes, Timestamp finMes) {
		this.usuario = usuario;
		this.monto = monto;
		this.fecha = fecha;
		this.inicioMes = inicioMes;
		this.finMes = finMes;
	}

	/** full constructor */
	public PagoMes(Usuario usuario, Double monto, Timestamp fecha,
			Timestamp inicioMes, Timestamp finMes, Set abonoDeudas) {
		this.usuario = usuario;
		this.monto = monto;
		this.fecha = fecha;
		this.inicioMes = inicioMes;
		this.finMes = finMes;
		this.abonoDeudas = abonoDeudas;
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

	public Set getAbonoDeudas() {
		return this.abonoDeudas;
	}

	public void setAbonoDeudas(Set abonoDeudas) {
		this.abonoDeudas = abonoDeudas;
	}

}