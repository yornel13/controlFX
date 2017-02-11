package hibernate.model;

// default package

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * PagoMes entity. @author MyEclipse Persistence Tools
 */

public class PlanillaIess implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuario usuario;
	private Double monto;
	private Timestamp fecha;
	private String inicioMes;
	private String finMes;

	// Constructors

	/** default constructor */
	public PlanillaIess() {
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
        
	public Double getMonto() {
		return this.monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public Timestamp getFecha() {
		return this.fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public String getInicioMes() {
		return this.inicioMes;
	}

	public void setInicioMes(String inicioMes) {
		this.inicioMes = inicioMes;
	}

	public String getFinMes() {
		return this.finMes;
	}

	public void setFinMes(String finMes) {
		this.finMes = finMes;
	}

        
}