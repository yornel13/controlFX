package hibernate.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Roles entity. @author MyEclipse Persistence Tools
 */

public class Roles implements java.io.Serializable {

	// Fields

	private Integer id;
	private String nombre;
	private String permiso;
	private Boolean activo;
	private Set usuarioses = new HashSet(0);

	// Constructors

	/** default constructor */
	public Roles() {
	}

	/** minimal constructor */
	public Roles(String nombre, String permiso, Boolean activo) {
		this.nombre = nombre;
		this.permiso = permiso;
		this.activo = activo;
	}

	/** full constructor */
	public Roles(String nombre, String permiso, Boolean activo, Set usuarioses) {
		this.nombre = nombre;
		this.permiso = permiso;
		this.activo = activo;
		this.usuarioses = usuarioses;
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

	public String getPermiso() {
		return this.permiso;
	}

	public void setPermiso(String permiso) {
		this.permiso = permiso;
	}

	public Boolean getActivo() {
		return this.activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public Set getUsuarioses() {
		return this.usuarioses;
	}

	public void setUsuarioses(Set usuarioses) {
		this.usuarioses = usuarioses;
	}

}