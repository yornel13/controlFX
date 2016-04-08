package hibernate.model;

import java.sql.Timestamp;

/**
 * ControlEmpleado entity. @author MyEclipse Persistence Tools
 */

public class ControlEmpleado implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuarios usuarios;
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
	public ControlEmpleado(Usuarios usuarios, Timestamp fecha,
			Timestamp horaEntrada, Timestamp horaSalida) {
		this.usuarios = usuarios;
		this.fecha = fecha;
		this.horaEntrada = horaEntrada;
		this.horaSalida = horaSalida;
	}

	/** full constructor */
	public ControlEmpleado(Usuarios usuarios, Cliente cliente, Timestamp fecha,
			Integer horasSuplementarias, Integer horasExtras,
			Timestamp horaEntrada, Timestamp horaSalida) {
		this.usuarios = usuarios;
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

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
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