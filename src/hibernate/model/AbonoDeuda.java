package hibernate.model;

// default package

import java.sql.Timestamp;

/**
 * AbonoDeuda entity. @author MyEclipse Persistence Tools
 */

public class AbonoDeuda implements java.io.Serializable {

	// Fields

	private Integer id;
	private Deuda deuda;
	private PagoMes pagoMes;
	private Double monto;
        private Double restante;
	private Timestamp fecha;

	// Constructors

	/** default constructor */
	public AbonoDeuda() {
	}

	/** minimal constructor */
	public AbonoDeuda(Deuda deuda, Double monto, Timestamp fecha) {
		this.deuda = deuda;
		this.monto = monto;
		this.fecha = fecha;
	}

	/** full constructor */
	public AbonoDeuda(Deuda deuda, PagoMes pagoMes, Double monto,
			Timestamp fecha) {
		this.deuda = deuda;
		this.pagoMes = pagoMes;
		this.monto = monto;
		this.fecha = fecha;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Deuda getDeuda() {
		return this.deuda;
	}

	public void setDeuda(Deuda deuda) {
		this.deuda = deuda;
	}

	public PagoMes getPagoMes() {
		return this.pagoMes;
	}

	public void setPagoMes(PagoMes pagoMes) {
		this.pagoMes = pagoMes;
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

        public Double getRestante() {
            return restante;
        }

        public void setRestante(Double restante) {
            this.restante = restante;
        }

        
}