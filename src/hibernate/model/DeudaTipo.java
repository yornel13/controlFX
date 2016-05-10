package hibernate.model;

/**
 * DeudaTipo entity. @author MyEclipse Persistence Tools
 */

public class DeudaTipo implements java.io.Serializable {

	// Fields

	private Integer id;
	private String nombre;
	private Integer cuotas;
	private Boolean activo;

	// Constructors

	/** default constructor */
	public DeudaTipo() {
	}

	/** full constructor */
	public DeudaTipo(String nombre, Integer cuotas, Boolean activo) {
		this.nombre = nombre;
		this.cuotas = cuotas;
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

	public Integer getCuotas() {
		return this.cuotas;
	}

	public void setCuotas(Integer cuotas) {
		this.cuotas = cuotas;
	}

	public Boolean getActivo() {
		return this.activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

}