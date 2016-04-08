package hibernate.model;

/**
 * Identidad entity. @author MyEclipse Persistence Tools
 */

public class Identidad implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuarios usuarios;
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
	public Identidad(Usuarios usuarios, String nombreUsuario,
			String contrasena, Boolean activo) {
		this.usuarios = usuarios;
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

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
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