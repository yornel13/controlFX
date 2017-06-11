package hibernate.model;

// default package

import java.sql.Timestamp;
import java.sql.Date;

/**
 * PagoQuincena entity. @author MyEclipse Persistence Tools
 */

public class PagoVacaciones implements java.io.Serializable {

	// Fields

	private Integer id;
	private Usuario usuario;
        private Integer dias;
	private String inicio;
	private String finalizo;
        private Date goceInicio;
	private Date goceFin;
	private Timestamp fecha;
        private Double sueldo;
        private Double devengado;
	private Double monto;
        private Double aporte;
        private Double valor;

	// Constructors

	/** default constructor */
	public PagoVacaciones() {
	}

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

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFinalizo() {
        return finalizo;
    }

    public void setFinalizo(String finalizo) {
        this.finalizo = finalizo;
    }

    public Date getGoceInicio() {
        return goceInicio;
    }

    public void setGoceInicio(Date goceInicio) {
        this.goceInicio = goceInicio;
    }

    public Date getGoceFin() {
        return goceFin;
    }

    public void setGoceFin(Date goceFin) {
        this.goceFin = goceFin;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Double getAporte() {
        return aporte;
    }

    public void setAporte(Double aporte) {
        this.aporte = aporte;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getSueldo() {
        return sueldo;
    }

    public void setSueldo(Double sueldo) {
        this.sueldo = sueldo;
    }

    public Double getDevengado() {
        return devengado;
    }

    public void setDevengado(Double devengado) {
        this.devengado = devengado;
    }

    
}