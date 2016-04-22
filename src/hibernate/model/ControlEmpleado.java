package hibernate.model;

// default package

import java.sql.Timestamp;

/**
 * ControlEmpleado entity. @author MyEclipse Persistence Tools
 */

public class ControlEmpleado implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuario usuario;
	private Cliente cliente;
	private Timestamp fecha;
	private Integer horasSuplementarias;
	private Integer horasExtras;
	private Timestamp horaEntrada;
	private Timestamp horaSalida;

	// Constructors

	/** default constructor */
	public ControlEmpleado() {
	}

	/** minimal constructor */
	public ControlEmpleado(Usuario usuario, Timestamp fecha,
			Timestamp horaEntrada, Timestamp horaSalida) {
		this.usuario = usuario;
		this.fecha = fecha;
		this.horaEntrada = horaEntrada;
		this.horaSalida = horaSalida;
	}

	/** full constructor */
	public ControlEmpleado(Usuario usuario, Cliente cliente, Timestamp fecha,
			Integer horasSuplementarias, Integer horasExtras,
			Timestamp horaEntrada, Timestamp horaSalida) {
		this.usuario = usuario;
		this.cliente = cliente;
		this.fecha = fecha;
		this.horasSuplementarias = horasSuplementarias;
		this.horasExtras = horasExtras;
		this.horaEntrada = horaEntrada;
		this.horaSalida = horaSalida;
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

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Timestamp getFecha() {
		return this.fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public Integer getHorasSuplementarias() {
		return this.horasSuplementarias;
	}

	public void setHorasSuplementarias(Integer horasSuplementarias) {
		this.horasSuplementarias = horasSuplementarias;
	}

	public Integer getHorasExtras() {
		return this.horasExtras;
	}

	public void setHorasExtras(Integer horasExtras) {
		this.horasExtras = horasExtras;
	}

	public Timestamp getHoraEntrada() {
		return this.horaEntrada;
	}

	public void setHoraEntrada(Timestamp horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	public Timestamp getHoraSalida() {
		return this.horaSalida;
	}

	public void setHoraSalida(Timestamp horaSalida) {
		this.horaSalida = horaSalida;
	}

}