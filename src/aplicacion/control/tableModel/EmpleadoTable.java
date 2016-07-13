/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.tableModel;

import hibernate.model.Deuda;
import hibernate.model.RolCliente;
import hibernate.model.PagoMesItem;
import hibernate.model.PagoQuincena;
import hibernate.model.RolIndividual;
import java.util.ArrayList;

/**
 *
 * @author Yornel
 */
public class EmpleadoTable {
    
    private Integer id;
    private String nombre;
    private String apellido;
    private String cedula;
    private String telefono;
    private String departamento;
    private String cargo;
    private String empresa;
    private Integer dias;
    private Double horas;
    private Double suplementarias;
    private Double sobreTiempo;
    private Double actuarial1;
    private Double actuarial2;
    private Double quincenal;
    private Double totalMontoDeudas;
    private Integer totalDeudas;
    private Boolean acumulaDecimos;
    private Double totalIess;
    private Double sueldo;
    private Double nuevoSueldo;
    private Double nuevoQuincenal;
    private Boolean pagar; 
    private String pagado;
    private Boolean sinRoles;
    public ArrayList<Deuda> deudas;
    public ArrayList<PagoMesItem> pagoMesItems;
    public ArrayList<RolCliente> pagos;
    public RolIndividual rolIndividual;
    public Boolean problem;
    public PagoQuincena pagoQuincena;
    private Boolean activo;

    public EmpleadoTable() {
        problem = false;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public Double getHoras() {
        return horas;
    }

    public void setHoras(Double horas) {
        this.horas = horas;
    }

    public Double getSuplementarias() {
        return suplementarias;
    }

    public void setSuplementarias(Double suplementarias) {
        this.suplementarias = suplementarias;
    }

    public Double getSobreTiempo() {
        return sobreTiempo;
    }

    public void setSobreTiempo(Double sobreTiempo) {
        this.sobreTiempo = sobreTiempo;
    }

    public Double getActuarial1() {
        return actuarial1;
    }

    public void setActuarial1(Double actuarial1) {
        this.actuarial1 = actuarial1;
    }

    public Double getActuarial2() {
        return actuarial2;
    }

    public void setActuarial2(Double actuarial2) {
        this.actuarial2 = actuarial2;
    }

    public Double getQuincenal() {
        return quincenal;
    }

    public void setQuincenal(Double quincenal) {
        this.quincenal = quincenal;
    }

    public Double getTotalMontoDeudas() {
        return totalMontoDeudas;
    }

    public void setTotalMontoDeudas(Double totalMontoDeudas) {
        this.totalMontoDeudas = totalMontoDeudas;
    }

    public Integer getTotalDeudas() {
        return totalDeudas;
    }

    public void setTotalDeudas(Integer totalDeudas) {
        this.totalDeudas = totalDeudas;
    }

    public Boolean getAcumulaDecimos() {
        return acumulaDecimos;
    }

    public void setAcumulaDecimos(Boolean acumulaDecimos) {
        this.acumulaDecimos = acumulaDecimos;
    }

    public Double getTotalIess() {
        return totalIess;
    }

    public void setTotalIess(Double totalIess) {
        this.totalIess = totalIess;
    }

    public Double getSueldo() {
        return sueldo;
    }

    public void setSueldo(Double sueldo) {
        this.sueldo = sueldo;
    }

    public Double getNuevoSueldo() {
        return nuevoSueldo;
    }

    public void setNuevoSueldo(Double nuevoSueldo) {
        this.nuevoSueldo = nuevoSueldo;
    }

    public Double getNuevoQuincenal() {
        return nuevoQuincenal;
    }

    public void setNuevoQuincenal(Double nuevoQuincenal) {
        this.nuevoQuincenal = nuevoQuincenal;
    }

    public Boolean getPagar() {
        return pagar;
    }

    public void setPagar(Boolean pagar) {
        this.pagar = pagar;
    }

    public String getPagado() {
        return pagado;
    }

    public void setPagado(String pagado) {
        this.pagado = pagado;
    }

    public ArrayList<Deuda> getDeudas() {
        return deudas;
    }

    public void setDeudas(ArrayList<Deuda> deudas) {
        this.deudas = deudas;
    }

    public ArrayList<PagoMesItem> getPagoMesItems() {
        return pagoMesItems;
    }

    public void setPagoMesItems(ArrayList<PagoMesItem> pagoMesItems) {
        this.pagoMesItems = pagoMesItems;
    }

    public ArrayList<RolCliente> getPagos() {
        return pagos;
    }

    public void setPagos(ArrayList<RolCliente> pagos) {
        this.pagos = pagos;
    }

    public RolIndividual getRolIndividual() {
        return rolIndividual;
    }

    public void setRolIndividual(RolIndividual rolIndividual) {
        this.rolIndividual = rolIndividual;
    }

    public Boolean getSinRoles() {
        if (sinRoles == null) 
            return false;
        return sinRoles;
    }

    public void setSinRoles(Boolean sinRoles) {
        this.sinRoles = sinRoles;
    }

    public Boolean getProblem() {
        return problem;
    }

    public void setProblem(Boolean problem) {
        this.problem = problem;
    }

    public PagoQuincena getPagoQuincena() {
        return pagoQuincena;
    }

    public void setPagoQuincena(PagoQuincena pagoQuincena) {
        this.pagoQuincena = pagoQuincena;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
 
}
