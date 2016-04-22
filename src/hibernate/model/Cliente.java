package hibernate.model;

// default package

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Cliente entity. @author MyEclipse Persistence Tools
 */

public class Cliente implements java.io.Serializable {

	// Fields

	private Integer id;
	private String nombre;
	private String detalles;
	private Integer ruc;
	private String direccion;
	private String telefono;
	private Boolean activo;
	private Timestamp creacion;
	private Timestamp ultimaModificacion;
	private Set controlEmpleados = new HashSet(0);

	// Constructors

	/** default constructor */
	public Cliente() {
	}

	/** minimal constructor */
	public Cliente(String nombre, String detalles, Integer ruc,
			String direccion, String telefono, Boolean activo,
			Timestamp creacion, Timestamp ultimaModificacion) {
		this.nombre = nombre;
		this.detalles = detalles;
		this.ruc = ruc;
		this.direccion = direccion;
		this.telefono = telefono;
		this.activo = activo;
		this.creacion = creacion;
		this.ultimaModificacion = ultimaModificacion;
	}

	/** full constructor */
	public Cliente(String nombre, String detalles, Integer ruc,
			String direccion, String telefono, Boolean activo,
			Timestamp creacion, Timestamp ultimaModificacion,
			Set controlEmpleados) {
		this.nombre = nombre;
		this.detalles = detalles;
		this.ruc = ruc;
		this.direccion = direccion;
		this.telefono = telefono;
		this.activo = activo;
		this.creacion = creacion;
		this.ultimaModificacion = ultimaModificacion;
		this.controlEmpleados = controlEmpleados;
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

	public String getDetalles() {
		return this.detalles;
	}

	public void setDetalles(String detalles) {
		this.detalles = detalles;
	}

	public Integer getRuc() {
		return this.ruc;
	}

	public void setRuc(Integer ruc) {
		this.ruc = ruc;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Boolean getActivo() {
		return this.activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
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

	public Set getControlEmpleados() {
		return this.controlEmpleados;
	}

	public void setControlEmpleados(Set controlEmpleados) {
		this.controlEmpleados = controlEmpleados;
	}

}