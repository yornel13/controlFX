package hibernate.model;

// default package

/**
 * PagoMesItems entity. @author MyEclipse Persistence Tools
 */

public class PagoMesItems implements java.io.Serializable {

	// Fields

	private Integer id;
	private PagoMes pagoMes;
	private String descripcion;
	private Integer dias;
	private Integer horas;
	private Double ingreso;
	private Double deduccion;

	// Constructors

	/** default constructor */
	public PagoMesItems() {
	}

	/** minimal constructor */
	public PagoMesItems(PagoMes pagoMes, String descripcion) {
		this.pagoMes = pagoMes;
		this.descripcion = descripcion;
	}

	/** full constructor */
	public PagoMesItems(PagoMes pagoMes, String descripcion, Integer dias,
			Integer horas, Double ingreso, Double deduccion) {
		this.pagoMes = pagoMes;
		this.descripcion = descripcion;
		this.dias = dias;
		this.horas = horas;
		this.ingreso = ingreso;
		this.deduccion = deduccion;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PagoMes getPagoMes() {
		return this.pagoMes;
	}

	public void setPagoMes(PagoMes pagoMes) {
		this.pagoMes = pagoMes;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getDias() {
		return this.dias;
	}

	public void setDias(Integer dias) {
		this.dias = dias;
	}

	public Integer getHoras() {
		return this.horas;
	}

	public void setHoras(Integer horas) {
		this.horas = horas;
	}

	public Double getIngreso() {
		return this.ingreso;
	}

	public void setIngreso(Double ingreso) {
		this.ingreso = ingreso;
	}

	public Double getDeduccion() {
		return this.deduccion;
	}

	public void setDeduccion(Double deduccion) {
		this.deduccion = deduccion;
	}

}