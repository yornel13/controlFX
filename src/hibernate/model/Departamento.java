package hibernate.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Departamento entity. @author MyEclipse Persistence Tools
 */

public class Departamento implements java.io.Serializable {

	// Fields

	private Integer id;
	private String nombre;
	private Boolean activo;
	private Set detallesEmpleados = new HashSet(0);

	// Constructors

	/** default constructor */
	public Departamento() {
	}

	/** minimal constructor */
	public Departamento(String nombre, Boolean activo) {
		this.nombre = nombre;
		this.activo = activo;
	}

	/** full constructor */
	public Departamento(String nombre, Boolean activo, Set detallesEmpleados) {
		this.nombre = nombre;
		this.activo = activo;
		this.detallesEmpleados = detallesEmpleados;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Boolean getActivo() {
		return this.activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public Set getDetallesEmpleados() {
		return this.detallesEmpleados;
	}

	public void setDetallesEmpleados(Set detallesEmpleados) {
		this.detallesEmpleados = detallesEmpleados;
	}

}