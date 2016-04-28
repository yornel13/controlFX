/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.ControlTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.Fechas;
import hibernate.dao.ActuarialesDAO;
import hibernate.dao.ConstanteDAO;
import hibernate.dao.ControlEmpleadoDAO;
import hibernate.dao.SeguroDAO;
import hibernate.dao.UniformeDAO;
import hibernate.model.Actuariales;
import hibernate.model.Constante;
import hibernate.model.ControlEmpleado;
import hibernate.model.Seguro;
import hibernate.model.Uniforme;
import hibernate.model.Usuario;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class PagoMesController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private Usuario empleado;
    
    public ArrayList<ControlEmpleado> controlEmpleado;
   
    @FXML
    private Button administradoresButton;
    
    @FXML
    private Button agregarButton;
    
    @FXML
    private Pane imagenLabel;
    
    @FXML
    private Button ver;
    
    @FXML
    private Label totalDias;
           
    @FXML
    private Label totalHorasN;
    
    @FXML
    private Label totalHorasST;
    
    @FXML
    private Label totalHorasRC;
    
    @FXML
    private Label totalHorasExtras;
    
    @FXML
    private Label totalSalario;
    
    @FXML
    private Label totalSobreTiempo;
    
    @FXML
    private Label totalRecargo;
    
    @FXML
    private Label totalBono;
    
    @FXML
    private Label totalTransporte;
    
    @FXML
    private Label totalBonos;
    
    @FXML
    private Label totalVacaciones;
    
    @FXML
    private Label subTotal;
    
    @FXML
    private Label totalDecimo3;
    
    @FXML
    private Label totalDecimo4;
    
    @FXML
    private Label totalReserva;
    
    @FXML
    private Label totalJubilacion;
    
    @FXML
    private Label totalAporte;
    
    @FXML
    private Label totalSeguros;
    
    @FXML
    private Label totalUniformes;
    
    @FXML
    private Label totalIngresos;  
    
    @FXML
    private Label textEmpleado;
    
    @FXML
    private DatePicker pickerDe;
    
    @FXML 
    private DatePicker pickerHasta;
    
    @FXML Button expandirButton;
    
    @FXML
    private TextField bonoField;
    
    @FXML
    private TextField transporteField;
    
    @FXML
    private TextField seguroField;
    
    @FXML
    private TextField uniformeField;
    
    @FXML
    private TextField vacacionesField;
    
    @FXML
    private CheckBox checkVacaciones;
    
    private ObservableList<ControlTable> data;
    
    ArrayList<Usuario> usuarios;
    
    Timestamp fin;
    
    Timestamp inicio;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void goHome(ActionEvent event) {
        aplicacionControl.mostrarVentanaPrincipal();
        stagePrincipal.close();
    }
    
    @FXML
    private void onClickVacaciones(ActionEvent event) {
        if (checkVacaciones.isSelected()) {
            vacacionesField.setDisable(false);
        } else {
            vacacionesField.setDisable(true);
            vacacionesField.setText("");
        }
    }
    
    @FXML
    private void onClickCalcular(ActionEvent event) {
        setControlEmpleadoInfo(empleado, inicio, fin);
    }
    
    @FXML
    private void mostrarRegistro(ActionEvent event) {
        if (pickerDe.getValue() == null) {
            // error
        } else if (pickerHasta.getValue() == null) {
            // error 
        } else if (pickerDe.getValue().isAfter(pickerHasta.getValue())){
            // error
        } else {       
            fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());
            inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
            setControlEmpleadoInfo(empleado, inicio, fin);
        }
    } 
    
    public void setEmpleado(Usuario empleado, Timestamp inicio, Timestamp fin) throws ParseException {
        this.empleado = empleado;
        this.inicio = inicio;
        this.fin = fin;
        
        pickerDe.setValue(Fechas.getDateFromTimestamp(inicio));
        pickerHasta.setValue(Fechas.getDateFromTimestamp(fin));
        
        setControlEmpleadoInfo(empleado, inicio, fin);
    }
    
    public void setControlEmpleadoInfo(Usuario empleado, Timestamp inicio, Timestamp fin) {
        
        ControlEmpleadoDAO controlDAO = new ControlEmpleadoDAO();
        
        Integer dias = 0;
        Integer normales = 0;
        Integer sobreTiempo = 0;
        Integer suplementarias = 0;
        
        controlEmpleado = new ArrayList<>();
        controlEmpleado.addAll(controlDAO.findAllByEmpleadoIdInDeterminateTime(empleado.getId(), inicio, fin));
        
        for (ControlEmpleado control: controlEmpleado) {
            dias = dias + 1;
            normales = normales + 8;
            sobreTiempo = sobreTiempo + control.getHorasExtras();
            suplementarias = suplementarias + control.getHorasSuplementarias();
            
            ControlTable controlTable = new ControlTable();
            
            controlTable.setId(control.getId());
            if (control.getCliente() != null) {
                controlTable.setCliente(control.getCliente().getNombre());
            }
            controlTable.setFecha(new DateTime(control.getFecha().getTime()).toString("dd-MM-yyyy"));
            controlTable.setHorasExtras(control.getHorasExtras());
            controlTable.setHorasSuplementarias(control.getHorasSuplementarias());
            controlTable.setUsuarios(empleado);
            if (control.getLibre() != null && control.getLibre()) {
               controlTable.setDescanso("Libre"); 
            } 
            
        }
        
        Double sueldoDia = empleado.getDetallesEmpleado().getSueldo() / 30d;
        Double sueldoHoras = empleado.getDetallesEmpleado().getSueldo() / 240d;
        
        totalDias.setText(dias.toString());
        totalHorasN.setText(normales.toString());
        totalHorasRC.setText(suplementarias.toString());
        totalHorasST.setText(sobreTiempo.toString());
        totalHorasExtras.setText(String.valueOf(sobreTiempo + suplementarias));
        
        // Salario
        Double totalSalarioDouble = sueldoDia * Double.valueOf(dias);
        totalSalario.setText(String.format( "%.2f", totalSalarioDouble));
        Double totalSobreTiempoDouble = sueldoHoras * Double.valueOf(sobreTiempo);
        totalSobreTiempo.setText(String.format( "%.2f", totalSobreTiempoDouble));
        Double totalRecargoDouble = sueldoHoras * Double.valueOf(suplementarias);
        totalRecargo.setText(String.format( "%.2f", totalRecargoDouble));
        Double totalBonoDouble = getBono();
        totalBono.setText(String.format( "%.2f", totalBonoDouble));
        Double totalTransporteDouble = getTransporte();
        totalTransporte.setText(String.format( "%.2f", totalTransporteDouble));
        Double totalBonosDouble = totalBonoDouble + totalTransporteDouble;
        totalBonos.setText(String.format( "%.2f", totalBonosDouble));
        Double totalVacacionesDouble = getVacaciones();
        totalVacaciones.setText(String.format( "%.2f", totalVacacionesDouble));
        Double subTotalDouble = totalSalarioDouble + totalSobreTiempoDouble + totalRecargoDouble + totalBonosDouble + totalVacacionesDouble;
        subTotal.setText(String.format( "%.2f", subTotalDouble));
        ////////////////////////////////////////////////////
        Double decimoTercero = subTotalDouble / 12d;
        totalDecimo3.setText(String.format( "%.2f", decimoTercero));
        Double decimoCuarto = (getDecimoCuarto()/30d) * Double.valueOf(dias);
        totalDecimo4.setText(String.format( "%.2f", decimoCuarto));
        totalReserva.setText(String.format( "%.2f", decimoTercero));
        Double jubilacionPatronal = getActuariales(empleado.getId())/ 360d * Double.valueOf(dias);
        totalJubilacion.setText(String.format( "%.2f", jubilacionPatronal));
        Double aportePatronal = subTotalDouble * 12.5d / 100d;
        totalAporte.setText(String.format( "%.2f", aportePatronal));
        Double segurosDecimal = getSeguro(empleado.getDetallesEmpleado().getEmpresa().getId()) * Double.valueOf(dias);
        totalSeguros.setText(String.format( "%.2f", segurosDecimal));
        Double uniformeDecimal = getUniforme(empleado.getDetallesEmpleado().getEmpresa().getId()) * Double.valueOf(dias);
        totalUniformes.setText(String.format( "%.2f", uniformeDecimal));
        
        Double ingresoTotal = subTotalDouble + decimoTercero + decimoCuarto + decimoTercero 
                + jubilacionPatronal + aportePatronal + segurosDecimal + uniformeDecimal;
        totalIngresos.setText(String.format( "%.2f", ingresoTotal));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
    }  
    
    public static String getMonthName(int month){
        Calendar cal = Calendar.getInstance();
        // Calendar numbers months from 0
        cal.set(Calendar.MONTH, month - 1);
        return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }
    
    public double getDecimoCuarto() {
        ConstanteDAO constanteDao = new ConstanteDAO();
        Constante constante;
        constante = (Constante) constanteDao.findUniqueResultByNombre(Const.DECIMO_TERCERO);
        if (constante == null) {
            return 30.5;
        } else {
            return Double.valueOf(constante.getValor());
        }
    }
    
    public double getActuariales(Integer empleadoId) {
        ActuarialesDAO actuarialesDAO = new ActuarialesDAO();
        Actuariales actuariales;
        actuariales = actuarialesDAO.findByEmpleadoId(empleadoId);
        if (actuariales == null) {
            return 0;
        } else {
            return actuariales.getPrimario() + actuariales.getSecundario();
        }
    }
    
    public double getSeguro(Integer empresaId) {
        /*
        SeguroDAO seguroDAO = new SeguroDAO();
        Seguro seguro;
        seguro = seguroDAO.findByEmpresaId(empresaId);
        if (seguro == null) {
          return 0;  
        } else {
            return seguro.getValor();
        }  */
        if (seguroField.getText().isEmpty()) {
            return 0;
        } else {
            return Double.valueOf(seguroField.getText());
        }
    }
    
    public double getUniforme(Integer empresaId) {
        /*
        UniformeDAO uniformeDAO = new UniformeDAO();
        Uniforme uniforme;
        uniforme = uniformeDAO.findByEmpresaId(empresaId);
        if (uniforme == null) {
          return 0;  
        } else {
            return uniforme.getValor();
        }  */
        if (uniformeField.getText().isEmpty()) {
            return 0;
        } else {
            return Double.valueOf(uniformeField.getText());
        }
    }
    
    public double getBono() {
        if (bonoField.getText().isEmpty()) {
            return 0;
        } else {
            return Double.valueOf(bonoField.getText());
        }
    }
    
    public double getTransporte() {
        if (transporteField.getText().isEmpty()) {
            return 0;
        } else {
            return Double.valueOf(transporteField.getText());
        }
    }
    
    public double getVacaciones() {
        if (vacacionesField.getText().isEmpty()) {
            return 0;
        } else {
            return Double.valueOf(vacacionesField.getText());
        }
    }
    
}
