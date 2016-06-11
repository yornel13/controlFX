package migrar;

// default package

/**
 * EmpTbcargoguardia entity. @author MyEclipse Persistence Tools
 */

public class EmpTbcargoguardia implements java.io.Serializable {

	// Fields

	private Integer codiCargoguardia;
	private String nombCargoguardia;
	private String statCargoguardia;

	// Constructors

	/** default constructor */
	public EmpTbcargoguardia() {
	}

	/** full constructor */
	public EmpTbcargoguardia(String nombCargoguardia, String statCargoguardia) {
		this.nombCargoguardia = nombCargoguardia;
		this.statCargoguardia = statCargoguardia;
	}

	// Property accessors

	public Integer getCodiCargoguardia() {
		return this.codiCargoguardia;
	}

	public void setCodiCargoguardia(Integer codiCargoguardia) {
		this.codiCargoguardia = codiCargoguardia;
	}

	public String getNombCargoguardia() {
		return this.nombCargoguardia;
	}

	public void setNombCargoguardia(String nombCargoguardia) {
		this.nombCargoguardia = nombCargoguardia;
	}

	public String getStatCargoguardia() {
		return this.statCargoguardia;
	}

	public void setStatCargoguardia(String statCargoguardia) {
		this.statCargoguardia = statCargoguardia;
	}

}