package hibernate.model;

// default package

/**
 * Roles entity. @author MyEclipse Persistence Tools
 */

public class Roles implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuario usuario;
	private String nombre;
	private String permiso;
	private Boolean activo;

	// Constructors

	/** default constructor */
	public Roles() {
	}

	/** full constructor */
	public Roles(Usuario usuario, String nombre, String permiso, Boolean activo) {
		this.usuario = usuario;
		this.nombre = nombre;
		this.permiso = permiso;
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

}