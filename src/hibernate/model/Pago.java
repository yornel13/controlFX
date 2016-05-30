package hibernate.model;

// default package

import java.sql.Timestamp;

/**
 * Pago entity. @author MyEclipse Persistence Tools
 */

public class Pago implements java.io.Serializable {

	// Fields

	private Integer id;
	private String detalles;
	private Timestamp inicio;
	private Timestamp finalizo;
	private Timestamp fecha;
	private Double sueldo;
	private Integer dias;
	private Integer horasNormales;
	private Integer horasSuplementarias;
	private Integer horasSobreTiempo;
	private Integer totalHorasExtras;
	private Double salario;
	private Double montoHorasSuplementarias;
	private Double montoHorasSobreTiempo;
        private Double montoHorasExtras;
	private Double bono;
	private Double transporte;
	private Double totalBonos;
        private Double vacaciones;
	private Double subtotal;
	private Double decimoTercero;
	private Double decimoCuarto;
	private Double jubilacionPatronal;
	private Double aportePatronal;
	private Double seguros;
	private Double uniformes;
	private Double totalIngreso;
	private String empleado;
	private String cedula;
	private String empresa;
	private String clienteNombre;
        private Cliente cliente;
        private Usuario usuario;

	// Constructors

	/** default constructor */
	public Pago() {
	}

	/** minimal constructor */
	public Pago(Timestamp inicio, Timestamp finalizo, Timestamp fecha,
			Double sueldo, Integer dias, Integer horasNormales,
			Integer horasSuplementarias, Integer horasSobreTiempo,
			Integer totalHorasExtras, Double salario,
			Double montoHorasSuplementarias, Double montoHorasSobreTiempo,
			Double bono, Double transporte, Double totalBonos, Double vacaciones, Double subtotal,
			Double decimoTercero, Double decimoCuarto,
			Double jubilacionPatronal, Double aportePatronal, Double seguros,
			Double uniformes, Double totalIngreso, String empleado,
			String cedula, String empresa) {
		this.inicio = inicio;
		this.finalizo = finalizo;
		this.fecha = fecha;
		this.sueldo = sueldo;
		this.dias = dias;
		this.horasNormales = horasNormales;
		this.horasSuplementarias = horasSuplementarias;
		this.horasSobreTiempo = horasSobreTiempo;
		this.totalHorasExtras = totalHorasExtras;
		this.salario = salario;
		this.montoHorasSuplementarias = montoHorasSuplementarias;
		this.montoHorasSobreTiempo = montoHorasSobreTiempo;
		this.bono = bono;
		this.transporte = transporte;
		this.totalBonos = totalBonos;
                this.vacaciones = vacaciones;
		this.subtotal = subtotal;
		this.decimoTercero = decimoTercero;
		this.decimoCuarto = decimoCuarto;
		this.jubilacionPatronal = jubilacionPatronal;
		this.aportePatronal = aportePatronal;
		this.seguros = seguros;
		this.uniformes = uniformes;
		this.totalIngreso = totalIngreso;
		this.empleado = empleado;
		this.cedula = cedula;
		this.empresa = empresa;
	}

	/** full constructor */
	public Pago(String detalles, Timestamp inicio, Timestamp finalizo,
			Timestamp fecha, Double sueldo, Integer dias, Integer horasNormales,
			Integer horasSuplementarias, Integer horasSobreTiempo,
			Integer totalHorasExtras, Double salario,
			Double montoHorasSuplementarias, Double montoHorasSobreTiempo,
			Double bono, Double transporte, Double totalBonos, Double vacaciones, Double subtotal,
			Double decimoTercero, Double decimoCuarto,
			Double jubilacionPatronal, Double aportePatronal, Double seguros,
			Double uniformes, Double totalIngreso, String empleado,
			String cedula, String empresa, String clienteNombre) {
		this.detalles = detalles;
		this.inicio = inicio;
		this.finalizo = finalizo;
		this.fecha = fecha;
		this.sueldo = sueldo;
		this.dias = dias;
		this.horasNormales = horasNormales;
		this.horasSuplementarias = horasSuplementarias;
		this.horasSobreTiempo = horasSobreTiempo;
		this.totalHorasExtras = totalHorasExtras;
		this.salario = salario;
		this.montoHorasSuplementarias = montoHorasSuplementarias;
		this.montoHorasSobreTiempo = montoHorasSobreTiempo;
		this.bono = bono;
		this.transporte = transporte;
		this.totalBonos = totalBonos;
		this.subtotal = subtotal;
                this.vacaciones = vacaciones;
		this.decimoTercero = decimoTercero;
		this.decimoCuarto = decimoCuarto;
		this.jubilacionPatronal = jubilacionPatronal;
		this.aportePatronal = aportePatronal;
		this.seguros = seguros;
		this.uniformes = uniformes;
		this.totalIngreso = totalIngreso;
		this.empleado = empleado;
		this.cedula = cedula;
		this.empresa = empresa;
		this.clienteNombre = clienteNombre;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

        public String getDetalles() {
            return detalles;
        }

        public void setDetalles(String detalles) {
            this.detalles = detalles;
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

	public Timestamp getFecha() {
		return this.fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public Double getSueldo() {
		return this.sueldo;
	}

	public void setSueldo(Double sueldo) {
		this.sueldo = sueldo;
	}

	public Integer getDias() {
		return this.dias;
	}

	public void setDias(Integer dias) {
		this.dias = dias;
	}

	public Integer getHorasNormales() {
		return this.horasNormales;
	}

	public void setHorasNormales(Integer horasNormales) {
		this.horasNormales = horasNormales;
	}

	public Integer getHorasSuplementarias() {
		return this.horasSuplementarias;
	}

	public void setHorasSuplementarias(Integer horasSuplementarias) {
		this.horasSuplementarias = horasSuplementarias;
	}

	public Integer getHorasSobreTiempo() {
		return this.horasSobreTiempo;
	}

	public void setHorasSobreTiempo(Integer horasSobreTiempo) {
		this.horasSobreTiempo = horasSobreTiempo;
	}

	public Integer getTotalHorasExtras() {
		return this.totalHorasExtras;
	}

	public void setTotalHorasExtras(Integer totalHorasExtras) {
		this.totalHorasExtras = totalHorasExtras;
	}

	public Double getSalario() {
		return this.salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}

	public Double getMontoHorasSuplementarias() {
		return this.montoHorasSuplementarias;
	}

	public void setMontoHorasSuplementarias(Double montoHorasSuplementarias) {
		this.montoHorasSuplementarias = montoHorasSuplementarias;
	}

	public Double getMontoHorasSobreTiempo() {
		return this.montoHorasSobreTiempo;
	}

	public void setMontoHorasSobreTiempo(Double montoHorasSobreTiempo) {
		this.montoHorasSobreTiempo = montoHorasSobreTiempo;
	}

	public Double getBono() {
		return this.bono;
	}

	public void setBono(Double bono) {
		this.bono = bono;
	}

	public Double getTransporte() {
		return this.transporte;
	}

	public void setTransporte(Double transporte) {
		this.transporte = transporte;
	}

	public Double getTotalBonos() {
		return this.totalBonos;
	}

	public void setTotalBonos(Double totalBonos) {
		this.totalBonos = totalBonos;
	}
        
        public Double getVacaciones() {
            return vacaciones;
        }

        public void setVacaciones(Double vacaciones) {
            this.vacaciones = vacaciones;
        }

	public Double getSubtotal() {
		return this.subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getDecimoTercero() {
		return this.decimoTercero;
	}

	public void setDecimoTercero(Double decimoTercero) {
		this.decimoTercero = decimoTercero;
	}

	public Double getDecimoCuarto() {
		return this.decimoCuarto;
	}

	public void setDecimoCuarto(Double decimoCuarto) {
		this.decimoCuarto = decimoCuarto;
	}

	public Double getJubilacionPatronal() {
		return this.jubilacionPatronal;
	}

	public void setJubilacionPatronal(Double jubilacionPatronal) {
		this.jubilacionPatronal = jubilacionPatronal;
	}

	public Double getAportePatronal() {
		return this.aportePatronal;
	}

	public void setAportePatronal(Double aportePatronal) {
		this.aportePatronal = aportePatronal;
	}

	public Double getSeguros() {
		return this.seguros;
	}

	public void setSeguros(Double seguros) {
		this.seguros = seguros;
	}

	public Double getUniformes() {
		return this.uniformes;
	}

	public void setUniformes(Double uniformes) {
		this.uniformes = uniformes;
	}

	public Double getTotalIngreso() {
		return this.totalIngreso;
	}

	public void setTotalIngreso(Double totalIngreso) {
		this.totalIngreso = totalIngreso;
	}

	public String getEmpleado() {
		return this.empleado;
	}

	public void setEmpleado(String empleado) {
		this.empleado = empleado;
	}

	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getEmpresa() {
		return this.empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getClienteNombre() {
		return this.clienteNombre;
	}

	public void setClienteNombre(String clienteNombre) {
		this.clienteNombre = clienteNombre;
	} 

        public Cliente getCliente() {
            return cliente;
        }

        public void setCliente(Cliente cliente) {
            this.cliente = cliente;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

        public Double getMontoHorasExtras() {
            return montoHorasExtras;
        }

        public void setMontoHorasExtras(Double montoHorasExtras) {
            this.montoHorasExtras = montoHorasExtras;
        }

        
}