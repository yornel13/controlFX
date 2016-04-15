package hibernate.model;

import java.util.HashSet;
import java.util.Set;

/**
 * DetallesEmpleado entity. @author MyEclipse Persistence Tools
 */

public class DetallesEmpleado implements java.io.Serializable {

	// Fields

	private Integer id;
	private Departamento departamento;
	private Empresa empresa;
	private Cargo cargo;
	private Double sueldo;
	private String nroCuenta;
	private String extra;
	private Set usuarioses = new HashSet(0);

	// Constructors

	/** default constructor */
	public DetallesEmpleado() {
	}

	/** minimal constructor */
	public DetallesEmpleado(Empresa empresa, Cargo cargo, Double sueldo) {
		this.empresa = empresa;
		this.cargo = cargo;
		this.sueldo = sueldo;
	}

	/** full constructor */
	public DetallesEmpleado(Departamento departamento, Empresa empresa,
			Cargo cargo, Double sueldo, String nroCuenta, String extra,
			Set usuarioses) {
		this.departamento = departamento;
		this.empresa = empresa;
		this.cargo = cargo;
		this.sueldo = sueldo;
		this.nroCuenta = nroCuenta;
		this.extra = extra;
		this.usuarioses = usuarioses;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Departamento getDepartamento() {
		return this.departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Empresa getEmpresa() {
		return this.empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Cargo getCargo() {
		return this.cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public Double getSueldo() {
		return this.sueldo;
	}

	public void setSueldo(Double sueldo) {
		this.sueldo = sueldo;
	}

	public String getNroCuenta() {
		return this.nroCuenta;
	}

	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	public String getExtra() {
		return this.extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public Set getUsuarioses() {
		return this.usuarioses;
	}

	public void setUsuarioses(Set usuarioses) {
		this.usuarioses = usuarioses;
	}

}