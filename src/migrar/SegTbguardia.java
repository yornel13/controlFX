package migrar;

// default package

import java.util.Date;

/**
 * SegTbguardia entity. @author MyEclipse Persistence Tools
 */

public class SegTbguardia implements java.io.Serializable {

	// Fields

	private Integer codiGuardia;
	private Short codiEmpresa;
	private String ceduGuardia;
	private String apelGuardia;
	private String nombGuardia;
	private Date fnacGuardia;
	private String fon1Guardia;
	private String fon2Guardia;
	private Integer provGuardia;
	private Integer cantGuardia;
	private Integer parrGuardia;
	private String domiGuardia;
	private String ugpsGuardia;
	private String tcasGuardia;
	private String tconGuardia;
	private String lmilGuardia;
	private String iessGuardia;
	private Integer ecivGuardia;
	private String conyGuardia;
	private String oconGuardia;
	private String ctraGuardia;
	private String mailGuardia;
	private String alibGuardia;
	private String pfamGuardia;
	private Date fingGuardia;
	private String obseGuardia;
	private String statGuardia;
	private String condGuardia;
	private String supeGuardia;
	private String confiGuardia;
	private Date fechCreacion;
	private String horaCreacion;
	private Date fechModifica;
	private String horaModifica;
	private Short codiUsuariosist;
	private Double sueldo;
	private String acercadeMi;
	private String sexoGuardia;
	private byte[] foto;
	private String tipoGuardia;
	private Integer listaNegra;
	private String motivoListaNegra;
	private String motivoBaja;
	private Integer idCargo;
	private String apel2Guardia;

	// Constructors

	/** default constructor */
	public SegTbguardia() {
	}

	/** full constructor */
	public SegTbguardia(Short codiEmpresa, String ceduGuardia,
			String apelGuardia, String nombGuardia, Date fnacGuardia,
			String fon1Guardia, String fon2Guardia, Integer provGuardia,
			Integer cantGuardia, Integer parrGuardia, String domiGuardia,
			String ugpsGuardia, String tcasGuardia, String tconGuardia,
			String lmilGuardia, String iessGuardia, Integer ecivGuardia,
			String conyGuardia, String oconGuardia, String ctraGuardia,
			String mailGuardia, String alibGuardia, String pfamGuardia,
			Date fingGuardia, String obseGuardia, String statGuardia,
			String condGuardia, String supeGuardia, String confiGuardia,
			Date fechCreacion, String horaCreacion, Date fechModifica,
			String horaModifica, Short codiUsuariosist, Double sueldo,
			String acercadeMi, String sexoGuardia, byte[] foto,
			String tipoGuardia, Integer listaNegra, String motivoListaNegra,
			String motivoBaja, Integer idCargo, String apel2Guardia) {
		this.codiEmpresa = codiEmpresa;
		this.ceduGuardia = ceduGuardia;
		this.apelGuardia = apelGuardia;
		this.nombGuardia = nombGuardia;
		this.fnacGuardia = fnacGuardia;
		this.fon1Guardia = fon1Guardia;
		this.fon2Guardia = fon2Guardia;
		this.provGuardia = provGuardia;
		this.cantGuardia = cantGuardia;
		this.parrGuardia = parrGuardia;
		this.domiGuardia = domiGuardia;
		this.ugpsGuardia = ugpsGuardia;
		this.tcasGuardia = tcasGuardia;
		this.tconGuardia = tconGuardia;
		this.lmilGuardia = lmilGuardia;
		this.iessGuardia = iessGuardia;
		this.ecivGuardia = ecivGuardia;
		this.conyGuardia = conyGuardia;
		this.oconGuardia = oconGuardia;
		this.ctraGuardia = ctraGuardia;
		this.mailGuardia = mailGuardia;
		this.alibGuardia = alibGuardia;
		this.pfamGuardia = pfamGuardia;
		this.fingGuardia = fingGuardia;
		this.obseGuardia = obseGuardia;
		this.statGuardia = statGuardia;
		this.condGuardia = condGuardia;
		this.supeGuardia = supeGuardia;
		this.confiGuardia = confiGuardia;
		this.fechCreacion = fechCreacion;
		this.horaCreacion = horaCreacion;
		this.fechModifica = fechModifica;
		this.horaModifica = horaModifica;
		this.codiUsuariosist = codiUsuariosist;
		this.sueldo = sueldo;
		this.acercadeMi = acercadeMi;
		this.sexoGuardia = sexoGuardia;
		this.foto = foto;
		this.tipoGuardia = tipoGuardia;
		this.listaNegra = listaNegra;
		this.motivoListaNegra = motivoListaNegra;
		this.motivoBaja = motivoBaja;
		this.idCargo = idCargo;
		this.apel2Guardia = apel2Guardia;
	}

	// Property accessors

	public Integer getCodiGuardia() {
		return this.codiGuardia;
	}

	public void setCodiGuardia(Integer codiGuardia) {
		this.codiGuardia = codiGuardia;
	}

	public Short getCodiEmpresa() {
		return this.codiEmpresa;
	}

	public void setCodiEmpresa(Short codiEmpresa) {
		this.codiEmpresa = codiEmpresa;
	}

	public String getCeduGuardia() {
		return this.ceduGuardia;
	}

	public void setCeduGuardia(String ceduGuardia) {
		this.ceduGuardia = ceduGuardia;
	}

	public String getApelGuardia() {
		return this.apelGuardia;
	}

	public void setApelGuardia(String apelGuardia) {
		this.apelGuardia = apelGuardia;
	}

	public String getNombGuardia() {
		return this.nombGuardia;
	}

	public void setNombGuardia(String nombGuardia) {
		this.nombGuardia = nombGuardia;
	}

	public Date getFnacGuardia() {
		return this.fnacGuardia;
	}

	public void setFnacGuardia(Date fnacGuardia) {
		this.fnacGuardia = fnacGuardia;
	}

	public String getFon1Guardia() {
		return this.fon1Guardia;
	}

	public void setFon1Guardia(String fon1Guardia) {
		this.fon1Guardia = fon1Guardia;
	}

	public String getFon2Guardia() {
		return this.fon2Guardia;
	}

	public void setFon2Guardia(String fon2Guardia) {
		this.fon2Guardia = fon2Guardia;
	}

	public Integer getProvGuardia() {
		return this.provGuardia;
	}

	public void setProvGuardia(Integer provGuardia) {
		this.provGuardia = provGuardia;
	}

	public Integer getCantGuardia() {
		return this.cantGuardia;
	}

	public void setCantGuardia(Integer cantGuardia) {
		this.cantGuardia = cantGuardia;
	}

	public Integer getParrGuardia() {
		return this.parrGuardia;
	}

	public void setParrGuardia(Integer parrGuardia) {
		this.parrGuardia = parrGuardia;
	}

	public String getDomiGuardia() {
		return this.domiGuardia;
	}

	public void setDomiGuardia(String domiGuardia) {
		this.domiGuardia = domiGuardia;
	}

	public String getUgpsGuardia() {
		return this.ugpsGuardia;
	}

	public void setUgpsGuardia(String ugpsGuardia) {
		this.ugpsGuardia = ugpsGuardia;
	}

	public String getTcasGuardia() {
		return this.tcasGuardia;
	}

	public void setTcasGuardia(String tcasGuardia) {
		this.tcasGuardia = tcasGuardia;
	}

	public String getTconGuardia() {
		return this.tconGuardia;
	}

	public void setTconGuardia(String tconGuardia) {
		this.tconGuardia = tconGuardia;
	}

	public String getLmilGuardia() {
		return this.lmilGuardia;
	}

	public void setLmilGuardia(String lmilGuardia) {
		this.lmilGuardia = lmilGuardia;
	}

	public String getIessGuardia() {
		return this.iessGuardia;
	}

	public void setIessGuardia(String iessGuardia) {
		this.iessGuardia = iessGuardia;
	}

	public Integer getEcivGuardia() {
		return this.ecivGuardia;
	}

	public void setEcivGuardia(Integer ecivGuardia) {
		this.ecivGuardia = ecivGuardia;
	}

	public String getConyGuardia() {
		return this.conyGuardia;
	}

	public void setConyGuardia(String conyGuardia) {
		this.conyGuardia = conyGuardia;
	}

	public String getOconGuardia() {
		return this.oconGuardia;
	}

	public void setOconGuardia(String oconGuardia) {
		this.oconGuardia = oconGuardia;
	}

	public String getCtraGuardia() {
		return this.ctraGuardia;
	}

	public void setCtraGuardia(String ctraGuardia) {
		this.ctraGuardia = ctraGuardia;
	}

	public String getMailGuardia() {
		return this.mailGuardia;
	}

	public void setMailGuardia(String mailGuardia) {
		this.mailGuardia = mailGuardia;
	}

	public String getAlibGuardia() {
		return this.alibGuardia;
	}

	public void setAlibGuardia(String alibGuardia) {
		this.alibGuardia = alibGuardia;
	}

	public String getPfamGuardia() {
		return this.pfamGuardia;
	}

	public void setPfamGuardia(String pfamGuardia) {
		this.pfamGuardia = pfamGuardia;
	}

	public Date getFingGuardia() {
		return this.fingGuardia;
	}

	public void setFingGuardia(Date fingGuardia) {
		this.fingGuardia = fingGuardia;
	}

	public String getObseGuardia() {
		return this.obseGuardia;
	}

	public void setObseGuardia(String obseGuardia) {
		this.obseGuardia = obseGuardia;
	}

	public String getStatGuardia() {
		return this.statGuardia;
	}

	public void setStatGuardia(String statGuardia) {
		this.statGuardia = statGuardia;
	}

	public String getCondGuardia() {
		return this.condGuardia;
	}

	public void setCondGuardia(String condGuardia) {
		this.condGuardia = condGuardia;
	}

	public String getSupeGuardia() {
		return this.supeGuardia;
	}

	public void setSupeGuardia(String supeGuardia) {
		this.supeGuardia = supeGuardia;
	}

	public String getConfiGuardia() {
		return this.confiGuardia;
	}

	public void setConfiGuardia(String confiGuardia) {
		this.confiGuardia = confiGuardia;
	}

	public Date getFechCreacion() {
		return this.fechCreacion;
	}

	public void setFechCreacion(Date fechCreacion) {
		this.fechCreacion = fechCreacion;
	}

	public String getHoraCreacion() {
		return this.horaCreacion;
	}

	public void setHoraCreacion(String horaCreacion) {
		this.horaCreacion = horaCreacion;
	}

	public Date getFechModifica() {
		return this.fechModifica;
	}

	public void setFechModifica(Date fechModifica) {
		this.fechModifica = fechModifica;
	}

	public String getHoraModifica() {
		return this.horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public Short getCodiUsuariosist() {
		return this.codiUsuariosist;
	}

	public void setCodiUsuariosist(Short codiUsuariosist) {
		this.codiUsuariosist = codiUsuariosist;
	}

	public Double getSueldo() {
		return this.sueldo;
	}

	public void setSueldo(Double sueldo) {
		this.sueldo = sueldo;
	}

	public String getAcercadeMi() {
		return this.acercadeMi;
	}

	public void setAcercadeMi(String acercadeMi) {
		this.acercadeMi = acercadeMi;
	}

	public String getSexoGuardia() {
		return this.sexoGuardia;
	}

	public void setSexoGuardia(String sexoGuardia) {
		this.sexoGuardia = sexoGuardia;
	}

	public byte[] getFoto() {
		return this.foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public String getTipoGuardia() {
		return this.tipoGuardia;
	}

	public void setTipoGuardia(String tipoGuardia) {
		this.tipoGuardia = tipoGuardia;
	}

	public Integer getListaNegra() {
		return this.listaNegra;
	}

	public void setListaNegra(Integer listaNegra) {
		this.listaNegra = listaNegra;
	}

	public String getMotivoListaNegra() {
		return this.motivoListaNegra;
	}

	public void setMotivoListaNegra(String motivoListaNegra) {
		this.motivoListaNegra = motivoListaNegra;
	}

	public String getMotivoBaja() {
		return this.motivoBaja;
	}

	public void setMotivoBaja(String motivoBaja) {
		this.motivoBaja = motivoBaja;
	}

	public Integer getIdCargo() {
		return this.idCargo;
	}

	public void setIdCargo(Integer idCargo) {
		this.idCargo = idCargo;
	}

	public String getApel2Guardia() {
		return this.apel2Guardia;
	}

	public void setApel2Guardia(String apel2Guardia) {
		this.apel2Guardia = apel2Guardia;
	}

}