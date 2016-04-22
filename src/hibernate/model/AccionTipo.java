package hibernate.model;

// default package

import java.util.HashSet;
import java.util.Set;

/**
 * AccionTipo entity. @author MyEclipse Persistence Tools
 */

public class AccionTipo implements java.io.Serializable {

	// Fields

	private Integer id;
	private String nombre;
	private Boolean activo;
	private Set registroAccioneses = new HashSet(0);

	// Constructors

	/** default constructor */
	public AccionTipo() {
	}

	/** minimal constructor */
	public AccionTipo(String nombre, Boolean activo) {
		this.nombre = nombre;
		this.activo = activo;
	}

	/** full constructor */
	public AccionTipo(String nombre, Boolean activo, Set registroAccioneses) {
		this.nombre = nombre;
		this.activo = activo;
		this.registroAccioneses = registroAccioneses;
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

	public Set getRegistroAccioneses() {
		return this.registroAccioneses;
	}

	public void setRegistroAccioneses(Set registroAccioneses) {
		this.registroAccioneses = registroAccioneses;
	}

}