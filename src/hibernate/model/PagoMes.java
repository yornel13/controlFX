package hibernate.model;

// default package

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * PagoMes entity. @author MyEclipse Persistence Tools
 */

public class PagoMes implements java.io.Serializable {

    // Fields

    private Integer id;
    private Usuario usuario;
    private RolIndividual rolIndividual;
    private Double monto;
    private Timestamp fecha;
    private String inicioMes;
    private String finMes;
    private Set abonoDeudas = new HashSet(0);
    private Set pagoMesItems = new HashSet(0);
    private List<PagoMesItem> pagosItems;

    // Constructors

    /** default constructor */
    public PagoMes() {
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

    public RolIndividual getRolIndividual() {
        return rolIndividual;
    }

    public void setRolIndividual(RolIndividual rolIndividual) {
        this.rolIndividual = rolIndividual;
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

    public Set getAbonoDeudas() {
            return this.abonoDeudas;
    }

    public void setAbonoDeudas(Set abonoDeudas) {
            this.abonoDeudas = abonoDeudas;
    }

    public Set getPagoMesItems() {
        return pagoMesItems;
    }

    public void setPagoMesItems(Set pagoMesItems) {
        this.pagoMesItems = pagoMesItems;
    }

    public List<PagoMesItem> getPagosItems() {
        return pagosItems;
    }

    public void setPagosItems(List<PagoMesItem> pagosItems) {
        this.pagosItems = pagosItems;
    }

        
}