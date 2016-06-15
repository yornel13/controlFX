package hibernate.model;

// default package

import java.sql.Timestamp;


/**
 * Uniforme entity. @author MyEclipse Persistence Tools
 */

public class Uniforme implements java.io.Serializable {

	// Fields

	private Integer id;
	private Empresa empresa;
        private Cliente cliente;
	private String nombre;
	private Double valor;
	private Boolean activo;
        private Timestamp fecha;

	// Constructors

	/** default constructor */
	public Uniforme() {
	}

	/** full constructor */
	public Uniforme(Empresa empresa, String nombre, Double valor, Boolean activo) {
		this.empresa = empresa;
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

	public Empresa getEmpresa() {
		return this.empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

        public Cliente getCliente() {
            return cliente;
        }

        public void setCliente(Cliente cliente) {
            this.cliente = cliente;
        }
        
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getValor() {
		return this.valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Boolean getActivo() {
		return this.activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

        public Timestamp getFecha() {
            return fecha;
        }

        public void setFecha(Timestamp fecha) {
            this.fecha = fecha;
        }
       
}