package hibernate.model;

// default package

import java.sql.Date;
import java.sql.Time;

/**
 * ControlEmpleado entity. @author MyEclipse Persistence Tools
 */

public class ControlExtras implements java.io.Serializable {

	// Fields

	private Long id;
	private Usuario usuario;
	private Cliente cliente;
	private Date fecha;
        private Double recargo;
	private Double sobretiempo;
	private String caso;
        private Time entrada;
        private Time salida;
        
         // uso especial
        public String dias;

    public ControlExtras(ControlDiario controlDiarioWhioutFecha) {
        this.id = controlDiarioWhioutFecha.getId();
        this.usuario = controlDiarioWhioutFecha.getUsuario();
        this.cliente = controlDiarioWhioutFecha.getCliente();
        this.recargo = controlDiarioWhioutFecha.getRecargo();
        this.sobretiempo = controlDiarioWhioutFecha.getSobretiempo();
        this.caso = controlDiarioWhioutFecha.getCaso();
        this.entrada = controlDiarioWhioutFecha.getEntrada();
        this.salida = controlDiarioWhioutFecha.getSalida();
        this.dias = controlDiarioWhioutFecha.getDias();
    }
        public String getDias() {return dias;}
        public void setDias(String dias) {this.dias = dias;}
       
	// Constructors

	/** default constructor */
	public ControlExtras() {
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
        public ControlExtras clone() {

            ControlExtras newControl = new ControlExtras();
            newControl.setId(id);
            newControl.setUsuario(usuario);
            newControl.setCliente(cliente);
            newControl.setFecha(fecha);
            newControl.setRecargo(recargo);
            newControl.setSobretiempo(sobretiempo);
            newControl.setCaso(caso);
            newControl.setEntrada(entrada);
            newControl.setSalida(salida);
            newControl.setDias(dias);
            
            return newControl;

        }
}