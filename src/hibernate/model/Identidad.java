package hibernate.model;

// default package

/**
 * Identidad entity. @author MyEclipse Persistence Tools
 */

public class Identidad implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuario usuario;
	private String nombreUsuario;
	private String contrasena;
	private Boolean activo;

	// Constructors

	/** default constructor */
	public Identidad() {
	}

	/** minimal constructor */
	public Identidad(String nombreUsuario, String contrasena, Boolean activo) {
		this.nombreUsuario = nombreUsuario;
		this.contrasena = contrasena;
		this.activo = activo;
	}

	/** full constructor */
	public Identidad(Usuario usuario, String nombreUsuario, String contrasena,
			Boolean activo) {
		this.usuario = usuario;
		this.nombreUsuario = nombreUsuario;
		this.contrasena = contrasena;
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

	public String getNombreUsuario() {
		return this.nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getContrasena() {
		return this.contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public Boolean getActivo() {
		return this.activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

}