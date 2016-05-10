package hibernate.model;

// default package

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Deuda entity. @author MyEclipse Persistence Tools
 */

public class Deuda implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuario usuario;
	private String detalles;
	private Double monto;
	private Double restante;
	private Integer cuotas;
	private Timestamp creacion;
	private Timestamp ultimaModificacion;
	private Boolean pagada;
        private Boolean aplazar;
	private Set abonoDeudas = new HashSet(0);

	// Constructors

	/** default constructor */
	public Deuda() {
	}

	/** minimal constructor */
	public Deuda(Usuario usuario, Double monto, Double restante,
			Integer cuotas, Timestamp creacion, Timestamp ultimaModificacion,
			Boolean pagada) {
		this.usuario = usuario;
		this.monto = monto;
		this.restante = restante;
		this.cuotas = cuotas;
		this.creacion = creacion;
		this.ultimaModificacion = ultimaModificacion;
		this.pagada = pagada;
	}

	/** full constructor */
	public Deuda(Usuario usuario, String detalles, Double monto,
			Double restante, Integer cuotas, Timestamp creacion,
			Timestamp ultimaModificacion, Boolean pagada, Set abonoDeudas) {
		this.usuario = usuario;
		this.detalles = detalles;
		this.monto = monto;
		this.restante = restante;
		this.cuotas = cuotas;
		this.creacion = creacion;
		this.ultimaModificacion = ultimaModificacion;
		this.pagada = pagada;
		this.abonoDeudas = abonoDeudas;
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

	public String getDetalles() {
		return this.detalles;
	}

	public void setDetalles(String detalles) {
		this.detalles = detalles;
	}

	public Double getMonto() {
		return this.monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public Double getRestante() {
		return this.restante;
	}

	public void setRestante(Double restante) {
		this.restante = restante;
	}

	public Integer getCuotas() {
		return this.cuotas;
	}

	public void setCuotas(Integer cuotas) {
		this.cuotas = cuotas;
	}

	public Timestamp getCreacion() {
		return this.creacion;
	}

	public void setCreacion(Timestamp creacion) {
		this.creacion = creacion;
	}

	public Timestamp getUltimaModificacion() {
		return this.ultimaModificacion;
	}

	public void setUltimaModificacion(Timestamp ultimaModificacion) {
		this.ultimaModificacion = ultimaModificacion;
	}

	public Boolean getPagada() {
		return this.pagada;
	}

	public void setPagada(Boolean pagada) {
		this.pagada = pagada;
	}

	public Set getAbonoDeudas() {
		return this.abonoDeudas;
	}

	public void setAbonoDeudas(Set abonoDeudas) {
		this.abonoDeudas = abonoDeudas;
	}

        public Boolean getAplazar() {
            return aplazar;
        }

        public void setAplazar(Boolean aplazar) {
            this.aplazar = aplazar;
        }

        
}