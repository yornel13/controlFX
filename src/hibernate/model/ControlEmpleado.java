package hibernate.model;

// default package

import java.sql.Date;
import java.sql.Time;

/**
 * ControlEmpleado entity. @author MyEclipse Persistence Tools
 */

public class ControlEmpleado implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuario usuario;
	private Cliente cliente;
	private Date fecha;
	private Double normales;
        private Double recargo;
	private Double sobretiempo;
	private String caso;
        private Boolean medioDia;
        private Time entrada;
        private Time salida;
        
        // uso especial
        public String dias;
        public String getDias() {return dias;}
        public void setDias(String dias) {this.dias = dias;}
        
        

	// Constructors

	/** default constructor */
	public ControlEmpleado() {
	}

	/** minimal constructor */
	public ControlEmpleado(Usuario usuario, Date fecha) {
		this.usuario = usuario;
		this.fecha = fecha;
	}

	/** full constructor */
	public ControlEmpleado(Usuario usuario, Cliente cliente, Date fecha) {
		this.usuario = usuario;
		this.cliente = cliente;
		this.fecha = fecha;
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

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
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

        public String getCaso() {
            return caso;
        }

        public void setCaso(String caso) {
            this.caso = caso;
        }

        public Boolean getMedioDia() {
            return medioDia;
        }

        public void setMedioDia(Boolean medioDia) {
            this.medioDia = medioDia;
        }

        public Time getEntrada() {
            return entrada;
        }

        public void setEntrada(Time entrada) {
            this.entrada = entrada;
        }

        public Time getSalida() {
            return salida;
        }

        public void setSalida(Time salida) {
            this.salida = salida;
        }

        @Override
        public ControlEmpleado clone() {

            ControlEmpleado newControl = new ControlEmpleado();
            newControl.setId(id);
            newControl.setUsuario(usuario);
            newControl.setCliente(cliente);
            newControl.setFecha(fecha);
            newControl.setNormales(normales);
            newControl.setRecargo(recargo);
            newControl.setSobretiempo(sobretiempo);
            newControl.setCaso(caso);
            newControl.setMedioDia(medioDia);
            newControl.setEntrada(entrada);
            newControl.setSalida(salida);
            newControl.setDias(dias);
            
            return newControl;

        }
}