package hibernate.model;

// default package

import java.sql.Timestamp;

/**
 * PagoMes entity. @author MyEclipse Persistence Tools
 */

public class PagoMes implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuario usuario;
	private Timestamp inicio;
	private Timestamp finalizo;
	private Integer totalDias;
	private Double horasNormales;
	private Double totalHorasNormales;
	private Double totalHorasExtras;
	private Double totalHorasSuplementarias;
	private Double totalHorasAdicionales;
	private Double montoBono;
	private Double montoTransporte;
	private Double totalMontoAdicionales;
	private Double subtotal;
	private Double totalDecimoTercero;
	private Double totalDecimoCuarto;
	private Double montoJubilacionPatronal;
	private Double montoAportePatronal;
	private Double montoSeguros;
	private Double montoUniformes;
	private Double montoTotalIngreso;

	// Constructors

	/** default constructor */
	public PagoMes() {
	}

	/** full constructor */
	public PagoMes(Usuario usuario, Timestamp inicio, Timestamp finalizo,
			Integer totalDias, Double horasNormales, Double totalHorasNormales,
			Double totalHorasExtras, Double totalHorasSuplementarias,
			Double totalHorasAdicionales, Double montoBono,
			Double montoTransporte, Double totalMontoAdicionales,
			Double subtotal, Double totalDecimoTercero,
			Double totalDecimoCuarto, Double montoJubilacionPatronal,
			Double montoAportePatronal, Double montoSeguros,
			Double montoUniformes, Double montoTotalIngreso) {
		this.usuario = usuario;
		this.inicio = inicio;
		this.finalizo = finalizo;
		this.totalDias = totalDias;
		this.horasNormales = horasNormales;
		this.totalHorasNormales = totalHorasNormales;
		this.totalHorasExtras = totalHorasExtras;
		this.totalHorasSuplementarias = totalHorasSuplementarias;
		this.totalHorasAdicionales = totalHorasAdicionales;
		this.montoBono = montoBono;
		this.montoTransporte = montoTransporte;
		this.totalMontoAdicionales = totalMontoAdicionales;
		this.subtotal = subtotal;
		this.totalDecimoTercero = totalDecimoTercero;
		this.totalDecimoCuarto = totalDecimoCuarto;
		this.montoJubilacionPatronal = montoJubilacionPatronal;
		this.montoAportePatronal = montoAportePatronal;
		this.montoSeguros = montoSeguros;
		this.montoUniformes = montoUniformes;
		this.montoTotalIngreso = montoTotalIngreso;
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

	public Timestamp getInicio() {
		return this.inicio;
	}

	public void setInicio(Timestamp inicio) {
		this.inicio = inicio;
	}

	public Timestamp getFinalizo() {
		return this.finalizo;
	}

	public void setFinalizo(Timestamp finalizo) {
		this.finalizo = finalizo;
	}

	public Integer getTotalDias() {
		return this.totalDias;
	}

	public void setTotalDias(Integer totalDias) {
		this.totalDias = totalDias;
	}

	public Double getHorasNormales() {
		return this.horasNormales;
	}

	public void setHorasNormales(Double horasNormales) {
		this.horasNormales = horasNormales;
	}

	public Double getTotalHorasNormales() {
		return this.totalHorasNormales;
	}

	public void setTotalHorasNormales(Double totalHorasNormales) {
		this.totalHorasNormales = totalHorasNormales;
	}

	public Double getTotalHorasExtras() {
		return this.totalHorasExtras;
	}

	public void setTotalHorasExtras(Double totalHorasExtras) {
		this.totalHorasExtras = totalHorasExtras;
	}

	public Double getTotalHorasSuplementarias() {
		return this.totalHorasSuplementarias;
	}

	public void setTotalHorasSuplementarias(Double totalHorasSuplementarias) {
		this.totalHorasSuplementarias = totalHorasSuplementarias;
	}

	public Double getTotalHorasAdicionales() {
		return this.totalHorasAdicionales;
	}

	public void setTotalHorasAdicionales(Double totalHorasAdicionales) {
		this.totalHorasAdicionales = totalHorasAdicionales;
	}

	public Double getMontoBono() {
		return this.montoBono;
	}

	public void setMontoBono(Double montoBono) {
		this.montoBono = montoBono;
	}

	public Double getMontoTransporte() {
		return this.montoTransporte;
	}

	public void setMontoTransporte(Double montoTransporte) {
		this.montoTransporte = montoTransporte;
	}

	public Double getTotalMontoAdicionales() {
		return this.totalMontoAdicionales;
	}

	public void setTotalMontoAdicionales(Double totalMontoAdicionales) {
		this.totalMontoAdicionales = totalMontoAdicionales;
	}

	public Double getSubtotal() {
		return this.subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getTotalDecimoTercero() {
		return this.totalDecimoTercero;
	}

	public void setTotalDecimoTercero(Double totalDecimoTercero) {
		this.totalDecimoTercero = totalDecimoTercero;
	}

	public Double getTotalDecimoCuarto() {
		return this.totalDecimoCuarto;
	}

	public void setTotalDecimoCuarto(Double totalDecimoCuarto) {
		this.totalDecimoCuarto = totalDecimoCuarto;
	}

	public Double getMontoJubilacionPatronal() {
		return this.montoJubilacionPatronal;
	}

	public void setMontoJubilacionPatronal(Double montoJubilacionPatronal) {
		this.montoJubilacionPatronal = montoJubilacionPatronal;
	}

	public Double getMontoAportePatronal() {
		return this.montoAportePatronal;
	}

	public void setMontoAportePatronal(Double montoAportePatronal) {
		this.montoAportePatronal = montoAportePatronal;
	}

	public Double getMontoSeguros() {
		return this.montoSeguros;
	}

	public void setMontoSeguros(Double montoSeguros) {
		this.montoSeguros = montoSeguros;
	}

	public Double getMontoUniformes() {
		return this.montoUniformes;
	}

	public void setMontoUniformes(Double montoUniformes) {
		this.montoUniformes = montoUniformes;
	}

	public Double getMontoTotalIngreso() {
		return this.montoTotalIngreso;
	}

	public void setMontoTotalIngreso(Double montoTotalIngreso) {
		this.montoTotalIngreso = montoTotalIngreso;
	}

}