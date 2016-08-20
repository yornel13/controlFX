package hibernate.model;

// default package

import static aplicacion.control.util.Numeros.round;
import java.sql.Timestamp;

/**
 * ControlEmpleado entity. @author MyEclipse Persistence Tools
 */

public class ControlEmpleado implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuario usuario;
	private Cliente cliente;
	private Timestamp fecha;
	private Double normales;
        private Double recargo;
	private Double sobretiempo;
	private Boolean libre;
        private Boolean vacaciones;
        private Boolean falta;

	// Constructors

	/** default constructor */
	public ControlEmpleado() {
	}

	/** minimal constructor */
	public ControlEmpleado(Usuario usuario, Timestamp fecha,
			Boolean libre, Boolean vacaciones) {
		this.usuario = usuario;
		this.fecha = fecha;
		this.libre = libre;
		this.vacaciones = vacaciones;
	}

	/** full constructor */
	public ControlEmpleado(Usuario usuario, Cliente cliente, Timestamp fecha,
			Boolean libre, Boolean vacaciones) {
		this.usuario = usuario;
		this.cliente = cliente;
		this.fecha = fecha;
		this.libre = libre;
		this.vacaciones = vacaciones;
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

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Timestamp getFecha() {
		return this.fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

        public Double getNormales() {
            return normales;
        }

        public void setNormales(Double normales) {
            this.normales = normales;
        }

        public Double getRecargo() {
            return recargo;
        }

        public void setRecargo(Double recargo) {
            this.recargo = recargo;
        }

        public Double getSobretiempo() {
            return sobretiempo;
        }

        public void setSobretiempo(Double sobretiempo) {
            this.sobretiempo = sobretiempo;
        }

        public Boolean getLibre() {
            return libre;
        }

        public void setLibre(Boolean libre) {
            this.libre = libre;
        }

        public Boolean getVacaciones() {
            return vacaciones;
        }

        public void setVacaciones(Boolean vacaciones) {
            this.vacaciones = vacaciones;
        }
        
        public Boolean getFalta() {
            return falta;
        }

        public void setFalta(Boolean falta) {
            this.falta = falta;
        }

}