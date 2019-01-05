/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.tableModel;

import aplicacion.control.util.Fecha;
import hibernate.model.Actuariales;
import hibernate.model.Bonos;
import hibernate.model.ControlDiario;
import hibernate.model.ControlExtras;
import hibernate.model.CuotaDeuda;
import hibernate.model.DiasVacaciones;
import hibernate.model.PagoDecimo;
import hibernate.model.PagoMes;
import hibernate.model.RolCliente;
import hibernate.model.PagoMesItem;
import hibernate.model.PagoQuincena;
import hibernate.model.PagoVacaciones;
import hibernate.model.PlanillaIess;
import hibernate.model.RolIndividual;
import hibernate.model.Usuario;
import java.sql.Date;
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
    private Double dias;
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
    public ArrayList<CuotaDeuda> deudas;
    public ArrayList<PagoMesItem> pagoMesItems;
    public ArrayList<PagoMes> pagosMensuales;
    public ArrayList<RolCliente> pagos;
    public ArrayList<RolIndividual> rolesInds;
    public ArrayList<Double> decimosMes;
    public RolIndividual rolIndividual;
    public Boolean problem;
    public PagoQuincena pagoQuincena;
    public PagoMes pagoMes;
    private Boolean activo;
    private Boolean agregar;
    private String monto;
    private String montoAlternativo;
    private String cuotas;
    private Double decimo3;
    private Double decimo4;
    private Double reserva;
    private String detallesD3;
    private String detallesD4;
    private String cliente;
    private String horario;
    private Boolean haveRolCliente;
    private Boolean haveRolIndividual;
    private Boolean planilla;
    private Usuario usuario;
    private RolCliente rolCliente;
    private String bono;
    private String transporte;
    private Actuariales actuariales;
    private Integer diasInt;
    private ArrayList<ControlDiario> turnos;
    private ArrayList<ControlExtras> extras;
    private ControlDiario lunes;
    private ControlDiario martes;
    private ControlDiario miercoles;
    private ControlDiario jueves;
    private ControlDiario viernes;
    private ControlDiario sabado;
    private ControlDiario domingo;
    private String diasVacaciones;
    private DiasVacaciones objectVacaciones;
    private Boolean editado;
    private Bonos bonos;
    private String vacaciones;
    private PagoVacaciones pagoVacaciones;
    private PagoDecimo pagoDecimo;
    private String periodo;
    private Boolean oculto;
    private java.sql.Date sqlDateInicio;
    private java.sql.Date sqlDateFin;
    private Fecha fechaInicio;
    private Fecha FechaFin;
    private boolean administrativo;
    private PlanillaIess planillaIess;

    public EmpleadoTable() {
        problem = false;
        agregar = false;
        haveRolCliente = false;
        haveRolIndividual = false;
        cliente = "";
        monto = "0.0";
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

    public ArrayList<RolIndividual> getRolesInds() {
        return rolesInds;
    }

    public void setRolesInds(ArrayList<RolIndividual> rolesVac) {
        this.rolesInds = rolesVac;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCargo() {
        return cargo;
    }

    public String getMontoAlternativo() {
        return montoAlternativo;
    }

    public void setMontoAlternativo(String montoAlternativo) {
        this.montoAlternativo = montoAlternativo;
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

    public Double getDias() {
        return dias;
    }

    public void setDias(Double dias) {
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

    public ArrayList<CuotaDeuda> getDeudas() {
        return deudas;
    }

    public void setDeudas(ArrayList<CuotaDeuda> deudas) {
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

    public Boolean getAgregar() {
        return agregar;
    }

    public void setAgregar(Boolean agregar) {
        this.agregar = agregar;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getCuotas() {
        return cuotas;
    }

    public void setCuotas(String cuotas) {
        this.cuotas = cuotas;
    }

    public Double getDecimo3() {
        return decimo3;
    }

    public void setDecimo3(Double decimo3) {
        this.decimo3 = decimo3;
    }

    public Double getDecimo4() {
        return decimo4;
    }

    public void setDecimo4(Double decimo4) {
        this.decimo4 = decimo4;
    }

    public String getDetallesD3() {
        return detallesD3;
    }

    public void setDetallesD3(String detallesD3) {
        this.detallesD3 = detallesD3;
    }

    public String getDetallesD4() {
        return detallesD4;
    }

    public void setDetallesD4(String detallesD4) {
        this.detallesD4 = detallesD4;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Boolean getHaveRolCliente() {
        return haveRolCliente;
    }

    public void setHaveRolCliente(Boolean haveRolCliente) {
        this.haveRolCliente = haveRolCliente;
    }

    public Boolean getHaveRolIndividual() {
        return haveRolIndividual;
    }

    public void setHaveRolIndividual(Boolean haveRolIndividual) {
        this.haveRolIndividual = haveRolIndividual;
    }

    public Boolean getPlanilla() {
        return planilla;
    }

    public void setPlanilla(Boolean planilla) {
        this.planilla = planilla;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public PlanillaIess getPlanillaIess() {
        return planillaIess;
    }

    public void setPlanillaIess(PlanillaIess planillaIess) {
        this.planillaIess = planillaIess;
    }

    public RolCliente getRolCliente() {
        return rolCliente;
    }

    public void setRolCliente(RolCliente rolCliente) {
        this.rolCliente = rolCliente;
    }

    public String getBono() {
        return bono;
    }

    public void setBono(String bono) {
        this.bono = bono;
    }

    public String getTransporte() {
        return transporte;
    }

    public void setTransporte(String transporte) {
        this.transporte = transporte;
    }

    public Actuariales getActuariales() {
        return actuariales;
    }

    public void setActuariales(Actuariales actuariales) {
        this.actuariales = actuariales;
    }

    public Integer getDiasInt() {
        return diasInt;
    }

    public void setDiasInt(Integer diasInt) {
        this.diasInt = diasInt;
    }

    public ArrayList<ControlDiario> getTurnos() {
        return turnos;
    }

    public void setTurnos(ArrayList<ControlDiario> turnos) {
        this.turnos = turnos;
    }

    public ControlDiario getLunes() {
        return lunes;
    }

    public void setLunes(ControlDiario lunes) {
        this.lunes = lunes;
    }

    public ControlDiario getMartes() {
        return martes;
    }

    public void setMartes(ControlDiario martes) {
        this.martes = martes;
    }

    public ControlDiario getMiercoles() {
        return miercoles;
    }

    public void setMiercoles(ControlDiario miercoles) {
        this.miercoles = miercoles;
    }

    public ControlDiario getJueves() {
        return jueves;
    }

    public void setJueves(ControlDiario jueves) {
        this.jueves = jueves;
    }

    public ControlDiario getViernes() {
        return viernes;
    }

    public void setViernes(ControlDiario viernes) {
        this.viernes = viernes;
    }

    public ControlDiario getSabado() {
        return sabado;
    }

    public void setSabado(ControlDiario sabado) {
        this.sabado = sabado;
    }

    public ControlDiario getDomingo() {
        return domingo;
    }

    public void setDomingo(ControlDiario domingo) {
        this.domingo = domingo;
    }

    public String getDiasVacaciones() {
        return diasVacaciones;
    }

    public void setDiasVacaciones(String diasVacaciones) {
        this.diasVacaciones = diasVacaciones;
    }

    public Boolean getEditado() {
        return editado;
    }

    public void setEditado(Boolean editado) {
        this.editado = editado;
    }

    public DiasVacaciones getObjectVacaciones() {
        return objectVacaciones;
    }

    public void setObjectVacaciones(DiasVacaciones objectVacaciones) {
        this.objectVacaciones = objectVacaciones;
    }

    public Bonos getBonos() {
        return bonos;
    }

    public void setBonos(Bonos bonos) {
        this.bonos = bonos;
    }

    public ArrayList<ControlExtras> getExtras() {
        return extras;
    }

    public void setExtras(ArrayList<ControlExtras> extras) {
        this.extras = extras;
    }

    public ArrayList<PagoMes> getPagosMensuales() {
        return pagosMensuales;
    }

    public void setPagosMensuales(ArrayList<PagoMes> pagosMensuales) {
        this.pagosMensuales = pagosMensuales;
    }

    public String getVacaciones() {
        return vacaciones;
    }

    public void setVacaciones(String vacaciones) {
        this.vacaciones = vacaciones;
    }

    public PagoVacaciones getPagoVacaciones() {
        return pagoVacaciones;
    }

    public void setPagoVacaciones(PagoVacaciones pagoVacaciones) {
        this.pagoVacaciones = pagoVacaciones;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public PagoMes getPagoMes() {
        return pagoMes;
    }

    public void setPagoMes(PagoMes pagoMes) {
        this.pagoMes = pagoMes;
    }

    public Double getReserva() {
        return reserva;
    }

    public void setReserva(Double reserva) {
        this.reserva = reserva;
    }

    public Boolean getOculto() {
        return oculto;
    }

    public void setOculto(Boolean oculto) {
        this.oculto = oculto;
    }

    public Date getSqlDateInicio() {
        return sqlDateInicio;
    }

    public void setSqlDateInicio(Date sqlDateInicio) {
        this.sqlDateInicio = sqlDateInicio;
    }

    public Date getSqlDateFin() {
        return sqlDateFin;
    }

    public void setSqlDateFin(Date sqlDateFin) {
        this.sqlDateFin = sqlDateFin;
    }

    public Fecha getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Fecha fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Fecha getFechaFin() {
        return FechaFin;
    }

    public void setFechaFin(Fecha FechaFin) {
        this.FechaFin = FechaFin;
    }

    public PagoDecimo getPagoDecimo() {
        return pagoDecimo;
    }

    public void setPagoDecimo(PagoDecimo pagoDecimo) {
        this.pagoDecimo = pagoDecimo;
    }

    public ArrayList<Double> getDecimosMes() {
        return decimosMes;
    }

    public void setDecimosMes(ArrayList<Double> decimosMes) {
        this.decimosMes = decimosMes;
    }

    public boolean isAdministrativo() {
        return administrativo;
    }

    public void setAdministrativo(boolean administrativo) {
        this.administrativo = administrativo;
    }
    
    
}
