package hibernate.model;

// default package

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * PagoMes entity. @author MyEclipse Persistence Tools
 */

public class PagoDecimo implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuario usuario;
	private Double monto;
	private Timestamp fecha;
        private String decimo;
        private Set pagoDecimoTerceros = new HashSet(0);
        private Set pagoDecimoCuartos = new HashSet(0);

	// Constructors

	/** default constructor */
	public PagoDecimo() {
	}

	/** minimal constructor */
	public PagoDecimo(Usuario usuario, Double monto, Timestamp fecha) {
		this.usuario = usuario;
		this.monto = monto;
		this.fecha = fecha;
	}

	/** full constructor */
	public PagoDecimo(Usuario usuario, Double monto, Timestamp fecha, String decimo) {
		this.usuario = usuario;
		this.monto = monto;
		this.fecha = fecha;
                this.decimo = decimo;
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

        public String getDecimo() {
            return decimo;
        }

        public void setDecimo(String decimo) {
            this.decimo = decimo;
        }

        public Set getPagoDecimoTerceros() {
            return pagoDecimoTerceros;
        }

        public void setPagoDecimoTerceros(Set pagoDecimoTerceros) {
            this.pagoDecimoTerceros = pagoDecimoTerceros;
        }

        public Set getPagoDecimoCuartos() {
            return pagoDecimoCuartos;
        }

        public void setPagoDecimoCuartos(Set pagoDecimoCuartos) {
            this.pagoDecimoCuartos = pagoDecimoCuartos;
        }

	
            

        
}