package hibernate.model;

// default package

/**
 * Actuariales entity. @author MyEclipse Persistence Tools
 */

public class Actuariales implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuario usuario;
	private Double primario;
	private Double secundario;
	private Boolean activo;

	// Constructors

	/** default constructor */
	public Actuariales() {
	}

	/** full constructor */
	public Actuariales(Usuario usuario, Double primario, Double secundario,
			Boolean activo) {
		this.usuario = usuario;
		this.primario = primario;
		this.secundario = secundario;
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

	public Double getPrimario() {
		return this.primario;
	}

	public void setPrimario(Double primario) {
		this.primario = primario;
	}

	public Double getSecundario() {
		return this.secundario;
	}

	public void setSecundario(Double secundario) {
		this.secundario = secundario;
	}

	public Boolean getActivo() {
		return this.activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

}