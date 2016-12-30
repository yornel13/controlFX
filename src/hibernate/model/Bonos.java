package hibernate.model;

// default package

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Bono entity. @author MyEclipse Persistence Tools
 */

public class Bonos implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuario usuario;
        private Cliente cliente;
	private Double bono;
	private Double transporte;
	private Timestamp fecha;
	private Date inicioMes;
	private Date finMes;

	// Constructors

	/** default constructor */
	public Bonos() {
	}
        
	// Property accessors

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Double getBono() {
        return bono;
    }

    public void setBono(Double bono) {
        this.bono = bono;
    }

    public Double getTransporte() {
        return transporte;
    }

    public void setTransporte(Double transporte) {
        this.transporte = transporte;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public Date getInicioMes() {
        return inicioMes;
    }

    public void setInicioMes(Date inicioMes) {
        this.inicioMes = inicioMes;
    }

    public Date getFinMes() {
        return finMes;
    }

    public void setFinMes(Date finMes) {
        this.finMes = finMes;
    }

    

}