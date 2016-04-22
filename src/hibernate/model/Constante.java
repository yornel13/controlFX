package hibernate.model;

// default package

/**
 * Constante entity. @author MyEclipse Persistence Tools
 */

public class Constante implements java.io.Serializable {

	// Fields

	private Integer id;
	private String nombre;
	private String valor;
	private Boolean activo;

	// Constructors

	/** default constructor */
	public Constante() {
	}

	/** full constructor */
	public Constante(String nombre, String valor, Boolean activo) {
		this.nombre = nombre;
		this.valor = valor;
		this.activo = activo;
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

	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Boolean getActivo() {
		return this.activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

}