package hibernate.model;

import java.sql.Timestamp;

/**
 * PagoMes entity. @author MyEclipse Persistence Tools
 */

public class PagoMes implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuarios usuarios;
	private Timestamp inicio;
	private Timestamp finalizo;
	private Integer totalDias;
	private Long horasNormales;
	private Long totalHorasNormales;
	private Long totalHorasExtras;
	private Long totalHorasSuplementarias;
	private Long totalHorasAdicionales;
	private Integer montoBono;
	private Integer montoTransporte;
	private Long totalMontoAdicionales;
	private Long subtotal;
	private Long totalDecimoTercero;
	private Long totalDecimoCuarto;
	private Long montoJubilacionPatronal;
	private Long montoAportePatronal;
	private Long montoSeguros;
	private Long montoUniformes;
	private Long montoTotalIngreso;

	// Constructors

	/** default constructor */
	public PagoMes() {
	}

	/** full constructor */
	public PagoMes(Usuarios usuarios, Timestamp inicio, Timestamp finalizo,
			Integer totalDias, Long horasNormales, Long totalHorasNormales,
			Long totalHorasExtras, Long totalHorasSuplementarias,
			Long totalHorasAdicionales, Integer montoBono,
			Integer montoTransporte, Long totalMontoAdicionales, Long subtotal,
			Long totalDecimoTercero, Long totalDecimoCuarto,
			Long montoJubilacionPatronal, Long montoAportePatronal,
			Long montoSeguros, Long montoUniformes, Long montoTotalIngreso) {
		this.usuarios = usuarios;
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

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
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

	public Long getHorasNormales() {
		return this.horasNormales;
	}

	public void setHorasNormales(Long horasNormales) {
		this.horasNormales = horasNormales;
	}

	public Long getTotalHorasNormales() {
		return this.totalHorasNormales;
	}

	public void setTotalHorasNormales(Long totalHorasNormales) {
		this.totalHorasNormales = totalHorasNormales;
	}

	public Long getTotalHorasExtras() {
		return this.totalHorasExtras;
	}

	public void setTotalHorasExtras(Long totalHorasExtras) {
		this.totalHorasExtras = totalHorasExtras;
	}

	public Long getTotalHorasSuplementarias() {
		return this.totalHorasSuplementarias;
	}

	public void setTotalHorasSuplementarias(Long totalHorasSuplementarias) {
		this.totalHorasSuplementarias = totalHorasSuplementarias;
	}

	public Long getTotalHorasAdicionales() {
		return this.totalHorasAdicionales;
	}

	public void setTotalHorasAdicionales(Long totalHorasAdicionales) {
		this.totalHorasAdicionales = totalHorasAdicionales;
	}

	public Integer getMontoBono() {
		return this.montoBono;
	}

	public void setMontoBono(Integer montoBono) {
		this.montoBono = montoBono;
	}

	public Integer getMontoTransporte() {
		return this.montoTransporte;
	}

	public void setMontoTransporte(Integer montoTransporte) {
		this.montoTransporte = montoTransporte;
	}

	public Long getTotalMontoAdicionales() {
		return this.totalMontoAdicionales;
	}

	public void setTotalMontoAdicionales(Long totalMontoAdicionales) {
		this.totalMontoAdicionales = totalMontoAdicionales;
	}

	public Long getSubtotal() {
		return this.subtotal;
	}

	public void setSubtotal(Long subtotal) {
		this.subtotal = subtotal;
	}

	public Long getTotalDecimoTercero() {
		return this.totalDecimoTercero;
	}

	public void setTotalDecimoTercero(Long totalDecimoTercero) {
		this.totalDecimoTercero = totalDecimoTercero;
	}

	public Long getTotalDecimoCuarto() {
		return this.totalDecimoCuarto;
	}

	public void setTotalDecimoCuarto(Long totalDecimoCuarto) {
		this.totalDecimoCuarto = totalDecimoCuarto;
	}

	public Long getMontoJubilacionPatronal() {
		return this.montoJubilacionPatronal;
	}

	public void setMontoJubilacionPatronal(Long montoJubilacionPatronal) {
		this.montoJubilacionPatronal = montoJubilacionPatronal;
	}

	public Long getMontoAportePatronal() {
		return this.montoAportePatronal;
	}

	public void setMontoAportePatronal(Long montoAportePatronal) {
		this.montoAportePatronal = montoAportePatronal;
	}

	public Long getMontoSeguros() {
		return this.montoSeguros;
	}

	public void setMontoSeguros(Long montoSeguros) {
		this.montoSeguros = montoSeguros;
	}

	public Long getMontoUniformes() {
		return this.montoUniformes;
	}

	public void setMontoUniformes(Long montoUniformes) {
		this.montoUniformes = montoUniformes;
	}

	public Long getMontoTotalIngreso() {
		return this.montoTotalIngreso;
	}

	public void setMontoTotalIngreso(Long montoTotalIngreso) {
		this.montoTotalIngreso = montoTotalIngreso;
	}

}