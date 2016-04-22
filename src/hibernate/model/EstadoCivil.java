package hibernate.model;

// default package

import java.util.HashSet;
import java.util.Set;

/**
 * EstadoCivil entity. @author MyEclipse Persistence Tools
 */

public class EstadoCivil implements java.io.Serializable {

	// Fields

	private Integer id;
	private String nombre;
	private Boolean activo;
	private Set usuarios = new HashSet(0);

	// Constructors

	/** default constructor */
	public EstadoCivil() {
	}

	/** minimal constructor */
	public EstadoCivil(String nombre, Boolean activo) {
		this.nombre = nombre;
		this.activo = activo;
	}

	/** full constructor */
	public EstadoCivil(String nombre, Boolean activo, Set usuarios) {
		this.nombre = nombre;
		this.activo = activo;
		this.usuarios = usuarios;
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

	public Set getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Set usuarios) {
		this.usuarios = usuarios;
	}

}