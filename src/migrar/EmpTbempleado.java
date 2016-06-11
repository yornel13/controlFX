package migrar;

// default package

import java.util.Date;

/**
 * EmpTbempleado entity. @author MyEclipse Persistence Tools
 */

public class EmpTbempleado implements java.io.Serializable {

	// Fields

	private Integer codiEmpleado;
	private Integer codiEmpresa;
	private Integer codiDepartament;
	private Integer codiCargo;
	private String ceduEmpleado;
	private Integer codiTipoemple;
	private String apatEmpleado;
	private String amatEmpleado;
	private String nombEmpleado;
	private Integer nproEmpleado;
	private Integer ncanEmpleado;
	private Integer nparEmpleado;
	private String nrecEmpleado;
	private Date fnacEmpleado;
	private Integer ecivEmpleado;
	private String sexoEmpleado;
	private String conyEmpleado;
	private String direEmpleado;
	private String fonoEmpleado;
	private String lmilEmpleado;
	private String iessEmpleado;
	private String mailEmpleado;
	private Date fingEmpleado;
	private String obseEmpleado;
	private String statEmpleado;
	private String fotoEmpleado;
	private Date fechCreacion;
	private String horaCreacion;
	private Short codiUsuariosist;
	private String acercadeMi;
	private Double sueldo;
	private String telf2;
	private String ocupCony;
	private String trabCony;
	private String ugps;
	private Integer listaNegra;
	private String motivoListaNegra;
	private String motivoBaja;
	private String clave;

	// Constructors

	/** default constructor */
	public EmpTbempleado() {
	}

	/** minimal constructor */
	public EmpTbempleado(String ceduEmpleado, Integer codiTipoemple,
			String apatEmpleado, String nombEmpleado) {
		this.ceduEmpleado = ceduEmpleado;
		this.codiTipoemple = codiTipoemple;
		this.apatEmpleado = apatEmpleado;
		this.nombEmpleado = nombEmpleado;
	}

	/** full constructor */
	public EmpTbempleado(Integer codiEmpresa, Integer codiDepartament,
			Integer codiCargo, String ceduEmpleado, Integer codiTipoemple,
			String apatEmpleado, String amatEmpleado, String nombEmpleado,
			Integer nproEmpleado, Integer ncanEmpleado, Integer nparEmpleado,
			String nrecEmpleado, Date fnacEmpleado, Integer ecivEmpleado,
			String sexoEmpleado, String conyEmpleado, String direEmpleado,
			String fonoEmpleado, String lmilEmpleado, String iessEmpleado,
			String mailEmpleado, Date fingEmpleado, String obseEmpleado,
			String statEmpleado, String fotoEmpleado, Date fechCreacion,
			String horaCreacion, Short codiUsuariosist, String acercadeMi,
			Double sueldo, String telf2, String ocupCony, String trabCony,
			String ugps, Integer listaNegra, String motivoListaNegra,
			String motivoBaja, String clave) {
		this.codiEmpresa = codiEmpresa;
		this.codiDepartament = codiDepartament;
		this.codiCargo = codiCargo;
		this.ceduEmpleado = ceduEmpleado;
		this.codiTipoemple = codiTipoemple;
		this.apatEmpleado = apatEmpleado;
		this.amatEmpleado = amatEmpleado;
		this.nombEmpleado = nombEmpleado;
		this.nproEmpleado = nproEmpleado;
		this.ncanEmpleado = ncanEmpleado;
		this.nparEmpleado = nparEmpleado;
		this.nrecEmpleado = nrecEmpleado;
		this.fnacEmpleado = fnacEmpleado;
		this.ecivEmpleado = ecivEmpleado;
		this.sexoEmpleado = sexoEmpleado;
		this.conyEmpleado = conyEmpleado;
		this.direEmpleado = direEmpleado;
		this.fonoEmpleado = fonoEmpleado;
		this.lmilEmpleado = lmilEmpleado;
		this.iessEmpleado = iessEmpleado;
		this.mailEmpleado = mailEmpleado;
		this.fingEmpleado = fingEmpleado;
		this.obseEmpleado = obseEmpleado;
		this.statEmpleado = statEmpleado;
		this.fotoEmpleado = fotoEmpleado;
		this.fechCreacion = fechCreacion;
		this.horaCreacion = horaCreacion;
		this.codiUsuariosist = codiUsuariosist;
		this.acercadeMi = acercadeMi;
		this.sueldo = sueldo;
		this.telf2 = telf2;
		this.ocupCony = ocupCony;
		this.trabCony = trabCony;
		this.ugps = ugps;
		this.listaNegra = listaNegra;
		this.motivoListaNegra = motivoListaNegra;
		this.motivoBaja = motivoBaja;
		this.clave = clave;
	}

	// Property accessors

	public Integer getCodiEmpleado() {
		return this.codiEmpleado;
	}

	public void setCodiEmpleado(Integer codiEmpleado) {
		this.codiEmpleado = codiEmpleado;
	}

	public Integer getCodiEmpresa() {
		return this.codiEmpresa;
	}

	public void setCodiEmpresa(Integer codiEmpresa) {
		this.codiEmpresa = codiEmpresa;
	}

	public Integer getCodiDepartament() {
		return this.codiDepartament;
	}

	public void setCodiDepartament(Integer codiDepartament) {
		this.codiDepartament = codiDepartament;
	}

	public Integer getCodiCargo() {
		return this.codiCargo;
	}

	public void setCodiCargo(Integer codiCargo) {
		this.codiCargo = codiCargo;
	}

	public String getCeduEmpleado() {
		return this.ceduEmpleado;
	}

	public void setCeduEmpleado(String ceduEmpleado) {
		this.ceduEmpleado = ceduEmpleado;
	}

	public Integer getCodiTipoemple() {
		return this.codiTipoemple;
	}

	public void setCodiTipoemple(Integer codiTipoemple) {
		this.codiTipoemple = codiTipoemple;
	}

	public String getApatEmpleado() {
		return this.apatEmpleado;
	}

	public void setApatEmpleado(String apatEmpleado) {
		this.apatEmpleado = apatEmpleado;
	}

	public String getAmatEmpleado() {
		return this.amatEmpleado;
	}

	public void setAmatEmpleado(String amatEmpleado) {
		this.amatEmpleado = amatEmpleado;
	}

	public String getNombEmpleado() {
		return this.nombEmpleado;
	}

	public void setNombEmpleado(String nombEmpleado) {
		this.nombEmpleado = nombEmpleado;
	}

	public Integer getNproEmpleado() {
		return this.nproEmpleado;
	}

	public void setNproEmpleado(Integer nproEmpleado) {
		this.nproEmpleado = nproEmpleado;
	}

	public Integer getNcanEmpleado() {
		return this.ncanEmpleado;
	}

	public void setNcanEmpleado(Integer ncanEmpleado) {
		this.ncanEmpleado = ncanEmpleado;
	}

	public Integer getNparEmpleado() {
		return this.nparEmpleado;
	}

	public void setNparEmpleado(Integer nparEmpleado) {
		this.nparEmpleado = nparEmpleado;
	}

	public String getNrecEmpleado() {
		return this.nrecEmpleado;
	}

	public void setNrecEmpleado(String nrecEmpleado) {
		this.nrecEmpleado = nrecEmpleado;
	}

	public Date getFnacEmpleado() {
		return this.fnacEmpleado;
	}

	public void setFnacEmpleado(Date fnacEmpleado) {
		this.fnacEmpleado = fnacEmpleado;
	}

	public Integer getEcivEmpleado() {
		return this.ecivEmpleado;
	}

	public void setEcivEmpleado(Integer ecivEmpleado) {
		this.ecivEmpleado = ecivEmpleado;
	}

	public String getSexoEmpleado() {
		return this.sexoEmpleado;
	}

	public void setSexoEmpleado(String sexoEmpleado) {
		this.sexoEmpleado = sexoEmpleado;
	}

	public String getConyEmpleado() {
		return this.conyEmpleado;
	}

	public void setConyEmpleado(String conyEmpleado) {
		this.conyEmpleado = conyEmpleado;
	}

	public String getDireEmpleado() {
		return this.direEmpleado;
	}

	public void setDireEmpleado(String direEmpleado) {
		this.direEmpleado = direEmpleado;
	}

	public String getFonoEmpleado() {
		return this.fonoEmpleado;
	}

	public void setFonoEmpleado(String fonoEmpleado) {
		this.fonoEmpleado = fonoEmpleado;
	}

	public String getLmilEmpleado() {
		return this.lmilEmpleado;
	}

	public void setLmilEmpleado(String lmilEmpleado) {
		this.lmilEmpleado = lmilEmpleado;
	}

	public String getIessEmpleado() {
		return this.iessEmpleado;
	}

	public void setIessEmpleado(String iessEmpleado) {
		this.iessEmpleado = iessEmpleado;
	}

	public String getMailEmpleado() {
		return this.mailEmpleado;
	}

	public void setMailEmpleado(String mailEmpleado) {
		this.mailEmpleado = mailEmpleado;
	}

	public Date getFingEmpleado() {
		return this.fingEmpleado;
	}

	public void setFingEmpleado(Date fingEmpleado) {
		this.fingEmpleado = fingEmpleado;
	}

	public String getObseEmpleado() {
		return this.obseEmpleado;
	}

	public void setObseEmpleado(String obseEmpleado) {
		this.obseEmpleado = obseEmpleado;
	}

	public String getStatEmpleado() {
		return this.statEmpleado;
	}

	public void setStatEmpleado(String statEmpleado) {
		this.statEmpleado = statEmpleado;
	}

	public String getFotoEmpleado() {
		return this.fotoEmpleado;
	}

	public void setFotoEmpleado(String fotoEmpleado) {
		this.fotoEmpleado = fotoEmpleado;
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

	public Short getCodiUsuariosist() {
		return this.codiUsuariosist;
	}

	public void setCodiUsuariosist(Short codiUsuariosist) {
		this.codiUsuariosist = codiUsuariosist;
	}

	public String getAcercadeMi() {
		return this.acercadeMi;
	}

	public void setAcercadeMi(String acercadeMi) {
		this.acercadeMi = acercadeMi;
	}

	public Double getSueldo() {
		return this.sueldo;
	}

	public void setSueldo(Double sueldo) {
		this.sueldo = sueldo;
	}

	public String getTelf2() {
		return this.telf2;
	}

	public void setTelf2(String telf2) {
		this.telf2 = telf2;
	}

	public String getOcupCony() {
		return this.ocupCony;
	}

	public void setOcupCony(String ocupCony) {
		this.ocupCony = ocupCony;
	}

	public String getTrabCony() {
		return this.trabCony;
	}

	public void setTrabCony(String trabCony) {
		this.trabCony = trabCony;
	}

	public String getUgps() {
		return this.ugps;
	}

	public void setUgps(String ugps) {
		this.ugps = ugps;
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

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

}