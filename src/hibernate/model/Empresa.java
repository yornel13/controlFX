package hibernate.model;

// default package

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Empresa entity. @author MyEclipse Persistence Tools
 */

public class Empresa implements java.io.Serializable {

	// Fields

	private Integer id;
	private String nombre;
	private String siglas;
	private String numeracion;
	private String telefono1;
	private String telefono2;
	private String fax;
	private String email;
	private String direccion;
	private String web;
	private String logo;
	private Timestamp creacion;
	private Timestamp ultimaModificacion;
	private String observacion;
	private String tipo;
	private Integer comienzoMes;
	private Boolean activo;
	private Set detallesEmpleados = new HashSet(0);

	// Constructors

	/** default constructor */
	public Empresa() {
	}

	/** minimal constructor */
	public Empresa(String nombre, String siglas, Timestamp creacion,
			Timestamp ultimaModificacion, Integer comienzoMes, Boolean activo) {
		this.nombre = nombre;
		this.siglas = siglas;
		this.creacion = creacion;
		this.ultimaModificacion = ultimaModificacion;
		this.comienzoMes = comienzoMes;
		this.activo = activo;
	}

	/** full constructor */
	public Empresa(String nombre, String siglas, String numeracion,
			String telefono1, String telefono2, String fax, String email,
			String direccion, String web, String logo, Timestamp creacion,
			Timestamp ultimaModificacion, String observacion, String tipo,
			Integer comienzoMes, Boolean activo, Set detallesEmpleados) {
		this.nombre = nombre;
		this.siglas = siglas;
		this.numeracion = numeracion;
		this.telefono1 = telefono1;
		this.telefono2 = telefono2;
		this.fax = fax;
		this.email = email;
		this.direccion = direccion;
		this.web = web;
		this.logo = logo;
		this.creacion = creacion;
		this.ultimaModificacion = ultimaModificacion;
		this.observacion = observacion;
		this.tipo = tipo;
		this.comienzoMes = comienzoMes;
		this.activo = activo;
		this.detallesEmpleados = detallesEmpleados;
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

	public String getSiglas() {
		return this.siglas;
	}

	public void setSiglas(String siglas) {
		this.siglas = siglas;
	}

	public String getNumeracion() {
		return this.numeracion;
	}

	public void setNumeracion(String numeracion) {
		this.numeracion = numeracion;
	}

	public String getTelefono1() {
		return this.telefono1;
	}

	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}

	public String getTelefono2() {
		return this.telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getWeb() {
		return this.web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getLogo() {
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
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

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

        public Integer getComienzoMes() {
            return comienzoMes;
        }

        public void setComienzoMes(Integer comienzoMes) {
            this.comienzoMes = comienzoMes;
        }

	public Boolean getActivo() {
		return this.activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public Set getDetallesEmpleados() {
		return this.detallesEmpleados;
	}

	public void setDetallesEmpleados(Set detallesEmpleados) {
		this.detallesEmpleados = detallesEmpleados;
	}

}