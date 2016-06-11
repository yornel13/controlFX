package migrar;

// default package

/**
 * WebTblCliente entity. @author MyEclipse Persistence Tools
 */

public class WebTblCliente implements java.io.Serializable {

	// Fields

	private Integer codCliente;
	private String razonSocial;
	private String siglas;

	// Constructors

	/** default constructor */
	public WebTblCliente() {
	}

	/** full constructor */
	public WebTblCliente(String razonSocial, String siglas) {
		this.razonSocial = razonSocial;
		this.siglas = siglas;
	}

	// Property accessors

	public Integer getCodCliente() {
		return this.codCliente;
	}

	public void setCodCliente(Integer codCliente) {
		this.codCliente = codCliente;
	}

	public String getRazonSocial() {
		return this.razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getSiglas() {
		return this.siglas;
	}

	public void setSiglas(String siglas) {
		this.siglas = siglas;
	}

}