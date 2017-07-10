package hibernate.model;

// default package

import java.sql.Timestamp;

/**
 * AbonoDeuda entity. @author MyEclipse Persistence Tools
 */

public class CuotaDeuda implements java.io.Serializable {

	// Fields

	private Integer id;
	private Deuda deuda;
	private PagoMes pagoMes;
	private Double monto;
        private String fecha;
        private String detalles;
	private Timestamp creado;
        private Timestamp editado;
        private Boolean aplazado;

	// Constructors

	/** default constructor */
	public CuotaDeuda() {
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Deuda getDeuda() {
        return deuda;
    }

    public void setDeuda(Deuda deuda) {
        this.deuda = deuda;
    }

    public PagoMes getPagoMes() {
        return pagoMes;
    }

    public void setPagoMes(PagoMes pagoMes) {
        this.pagoMes = pagoMes;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Timestamp getCreado() {
        return creado;
    }

    public void setCreado(Timestamp creado) {
        this.creado = creado;
    }

    public Timestamp getEditado() {
        return editado;
    }

    public void setEditado(Timestamp editado) {
        this.editado = editado;
    }

    public Boolean getAplazado() {
        return aplazado;
    }

    public void setAplazado(Boolean aplazado) {
        this.aplazado = aplazado;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

	
        
}