package hibernate.model;

// default package

import java.sql.Time;

/**
 * ControlEmpleado entity. @author MyEclipse Persistence Tools
 */

public class ControlDiario implements java.io.Serializable {

	// Fields

	private Long id;
	private Usuario usuario;
	private Cliente cliente;
	private String fecha;
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
	public ControlDiario() {
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
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

	public String getFecha() {
		return this.fecha;
	}

	public void setFecha(String fecha) {
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
        public ControlDiario clone() {

            ControlDiario newControl = new ControlDiario();
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