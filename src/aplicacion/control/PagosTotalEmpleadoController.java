/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.PagosTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.Permisos;
import hibernate.dao.ConstanteDAO;
import hibernate.dao.DeudaDAO;
import hibernate.dao.PagoDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Constante;
import hibernate.model.Deuda;
import hibernate.model.Empresa;
import hibernate.model.Pago;
import hibernate.model.Usuario;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class PagosTotalEmpleadoController implements Initializable {

    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private ChoiceBox empleadosChoiceBox;
    
    @FXML
    private TextField cedulaField;
    
    @FXML
    private DatePicker pickerDe;
    
    @FXML 
    private DatePicker pickerHasta;
    
    @FXML
    private TableView controlClienteTableView;
    
    // Usuario datos
    @FXML
    private Label nombreText;
    
    @FXML
    private Label apellidoText;
    
    @FXML
    private Label cedulaText;
    
    @FXML
    private Label sueldoText;
    
    @FXML
    private Label quincenaText;
    
    @FXML
    private Label decimosText;
    
    @FXML
    private Label cargoText;
   
    /// Columnas
    @FXML
    private TableColumn columnaCliente;
    
    @FXML
    private TableColumn columnaDias;
    
    @FXML
    private TableColumn columnaHoras;
    
    @FXML
    private TableColumn columnaNormales;
    
    @FXML
    private TableColumn columnaSumplementarias;
    
    @FXML
    private TableColumn columnaSobreTiempo;
    
    @FXML
    private TableColumn columnaIngresos;
    
    @FXML
    private TableColumn columnaSueldo;
    
    @FXML
    private TableColumn columnaExtra;
    
    @FXML
    private TableColumn columnaBonos;
    
    @FXML
    private TableColumn columnaVacaciones;
    
    @FXML
    private TableColumn columnaSubTotal;
    
    @FXML
    private TableColumn columnaDecimos;
    
    @FXML
    private TableColumn columnaTotal;
    
    // Totales Text
    
    @FXML
    private Label diasText;
    
    @FXML
    private Label normalesText;
    
    @FXML
    private Label suplementariasText;
    
    @FXML
    private Label sobreTiempoText;
    
    @FXML
    private Label sueldoTotalText;
    
    @FXML
    private Label extraText;
    
    @FXML
    private Label bonosText;
    
    @FXML
    private Label vacacionesText;
    
    @FXML
    private Label subTotalText;
    
    @FXML
    private Label decimosTotalText;
    
    @FXML
    private Label totalText;
    
     
    // Totales pagos y deducciones labels
    
    @FXML
    private Label montoIngresoText;
    
    @FXML
    private Label montoIessText;
    
    @FXML
    private Label montoQuincenaText;
    
    @FXML
    private Label montoDeudasText;
    
    @FXML
    private Label montoDeduccionesText;
    
    @FXML
    private Label montoAPercibirText;
    
    @FXML
    private Label iessPorcentaje;
    
    // Totales de el empleado
    
    private Integer diasTextValor;

    private Integer normalesTextValor;

    private Integer suplementariasTextValor;

    private Integer sobreTiempoTextValor;

    private Double sueldoTotalTextValor;

    private Double extraTextValor;

    private Double bonosTextValor;

    private Double vacacionesTextValor;

    private Double subTotalTextValor;

    private Double decimosTotalTextValor;

    private Double totalTextValor;
   
    // Totales de a percibir
    
    private Double ingresoValor;

    private Double ieesValor;

    private Double quincenaValor;

    private Double deudasValor;
    
    private Double deduccionesValor;

    private Double aPercibirValor;
    
    
    public Timestamp inicio;
    public Timestamp fin;
    
    private Empresa empresa;
    public Usuario empleado;
    
    ArrayList<Usuario> usuarios;
    ArrayList<Pago> pagos;
    ArrayList<PagosTable> pagosTable;
    
    private ObservableList<PagosTable> data;
    
    @FXML
    private Label errorText;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void selectCedula(ActionEvent event) {
        empleadosChoiceBox.setVisible(false);
        cedulaField.setVisible(true);
    }
    
    @FXML
    private void selectLista(ActionEvent event) {
        cedulaField.setVisible(false);
        empleadosChoiceBox.setVisible(true);
    }
    
    @FXML
    private void returnEmpresa(ActionEvent event) {
        aplicacionControl.mostrarInEmpresa(empresa);
        stagePrincipal.close();
    } 
    
    @FXML
    public void onClickMore(ActionEvent event) {
        pickerDe.setValue(pickerDe.getValue().plusMonths(1));
        pickerHasta.setValue(pickerHasta.getValue().plusMonths(1));
        inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());  
        if (empleado != null) {
            setTableInfo(inicio, fin, empleado.getId());
        }
    }
    
    @FXML
    public void onClickLess(ActionEvent event)  {
        pickerDe.setValue(pickerDe.getValue().minusMonths(1));
        pickerHasta.setValue(pickerHasta.getValue().minusMonths(1));
        inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());
        if (empleado != null) {
            setTableInfo(inicio, fin, empleado.getId());
        }
    }
    
    @FXML
    public void onClickGestionarDeudas(ActionEvent event) {
        if (empleado != null) {
            if (aplicacionControl.permisos == null) {
                aplicacionControl.noLogeado();
            } else {
                if (aplicacionControl.permisos.getPermiso(Permisos.A_ROL_DE_PAGO, Permisos.Nivel.EDITAR)) {
                    try {
                        FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaGestionDeudas.fxml"));
                        AnchorPane ventanaDeudas = (AnchorPane) loader.load();
                        Stage ventana = new Stage();
                        ventana.setTitle(empleado.getNombre() + " " + empleado.getApellido());
                        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_registro.png").toExternalForm();
                        ventana.getIcons().add(new Image(stageIcon));
                        ventana.setResizable(false);
                        ventana.setMaxWidth(608);
                        ventana.initOwner(stagePrincipal);
                        Scene scene = new Scene(ventanaDeudas);
                        ventana.setScene(scene);
                        GestionDeudasController controller = loader.getController();
                        controller.setStagePrincipal(ventana);
                        controller.setProgramaPrincipal(aplicacionControl);
                        controller.setPagoTotalController(this);
                        controller.setEmpleado(empleado);
                        ventana.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                        //tratar la excepción
                    }
                } else {
                    aplicacionControl.noPermitido();
                }
            }
        }
    }
    
    public void setEmpresa(Empresa empresa) throws ParseException {
        this.empresa = empresa;
        
        DateTime dateTime;
        
        dateTime = new DateTime(getToday().getTime());
        fin = new Timestamp(dateTime.withDayOfMonth(empresa.getDiaCortePago()).getMillis());
        inicio = new Timestamp(dateTime.withDayOfMonth(empresa.getDiaCortePago()).minusMonths(1).plusDays(1).getMillis());

        pickerDe.setValue(Fechas.getDateFromTimestamp(inicio));
        pickerHasta.setValue(Fechas.getDateFromTimestamp(fin));
        
    }
    
    @FXML
    private void mostrarRegistro(ActionEvent event) throws IOException {
        
        FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaFiltroEmpleados.fxml"));
        AnchorPane ventanaFiltro = (AnchorPane) loader.load();
        Stage ventana = new Stage();
        ventana.setTitle("Selecciona el empleado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_select.png").toExternalForm();
        ventana.getIcons().add(new Image(stageIcon));
        ventana.setResizable(false);
        ventana.initOwner(stagePrincipal);
        Scene scene = new Scene(ventanaFiltro);
        ventana.setScene(scene);
        FiltrarEmpleadoController controller = loader.getController();
        controller.setStagePrincipal(ventana);
        controller.setProgramaPrincipal(aplicacionControl);
        controller.setPagosController(this);
        controller.setEmpresa(empresa);
        ventana.showAndWait();
        
        
        /*
        if (pickerDe.getValue() == null) {
            errorText.setText("Fechas incorrectas");
        } else if (pickerHasta.getValue() == null) {
            errorText.setText("Fechas incorrectas");
        } else if (pickerDe.getValue().isAfter(pickerHasta.getValue())){
            errorText.setText("Fechas incorrectas");
        } else {   
            
            Usuario empleado = null;
            
            if (cedulaField.isVisible()) {
                
                empleado = new UsuarioDAO().findByCedulaActivo(cedulaField.getText());

            } else if (!empleadosChoiceBox.getSelectionModel().isEmpty()) {
                
                empleado = usuarios.get(empleadosChoiceBox.getSelectionModel().getSelectedIndex());
                
            }
            
            if (empleado == null) {
                
               errorText.setText("Empleado no encontrado"); 
                
            } else {
                
                this.empleado = empleado;
                
                fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());
                inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
            
                setTableInfo(inicio, fin, this.empleado.getId());
            }
        } */
    } 
    
    public void setEmpleado(Usuario empleado) {
        this.empleado = empleado;
        setTableInfo(inicio, fin, this.empleado.getId());
    }
    
    public void setTableInfo(Timestamp inicio, Timestamp fin, Integer empleadoId) {
        this.inicio = inicio;
        this.fin = fin;
        PagoDAO pagoDAO = new PagoDAO();
        pagos = new ArrayList<>();
        pagosTable = new ArrayList<>();
        pagos.addAll(pagoDAO.findAllByFechaAndEmpleadoIdConCliente(fin, empleadoId));
        if (pagos.isEmpty())
            pagos.addAll(pagoDAO.findAllByFechaAndEmpleadoIdSinCliente(fin, empleadoId));
        
        diasTextValor = 0;
        normalesTextValor = 0;
        suplementariasTextValor = 0;
        sobreTiempoTextValor = 0;
        sueldoTotalTextValor = 0d;
        extraTextValor = 0d;
        bonosTextValor = 0d;
        vacacionesTextValor = 0d;
        subTotalTextValor = 0d;
        decimosTotalTextValor = 0d;
        totalTextValor = 0d;
        
        ingresoValor = 0d;
        ieesValor = 0d;
        quincenaValor = 0d;
        deudasValor = 0d;
        deduccionesValor = 0d;
        aPercibirValor = 0d;
        
        pagos.stream().forEach((pago) -> {
            PagosTable pagoTable = new PagosTable();
            pagoTable.setId(pago.getId());
            if (pago.getClienteNombre() == null)
                pagoTable.setCliente("*Administrativo*");
            else
                pagoTable.setCliente(pago.getClienteNombre());
            pagoTable.setDias(pago.getDias());
            pagoTable.setNormales(pago.getHorasNormales().intValue());
            pagoTable.setSuplementarias(pago.getHorasSuplementarias().intValue());
            pagoTable.setSobreTiempo(pago.getHorasSobreTiempo().intValue());
            pagoTable.setSueldo(pago.getSalario());
            pagoTable.setExtra(pago.getMontoHorasSuplementarias() + pago.getMontoHorasSobreTiempo());
            pagoTable.setBonos(pago.getTotalBonos());
            pagoTable.setVacaciones(pago.getVacaciones());
            pagoTable.setSubtotal(pago.getSubtotal());
            pagoTable.setDecimos(pago.getDecimoCuarto() + pago.getDecimoTercero());
            pagoTable.setTotal(pago.getTotalIngreso());
            pagosTable.add(pagoTable);
            
            diasTextValor += pagoTable.getDias();
            normalesTextValor += pagoTable.getNormales();
            suplementariasTextValor += pagoTable.getSuplementarias();
            sobreTiempoTextValor += pagoTable.getSobreTiempo();
            sueldoTotalTextValor += pagoTable.getSueldo();
            extraTextValor += pagoTable.getExtra();
            bonosTextValor += pagoTable.getBonos();
            vacacionesTextValor += pagoTable.getVacaciones();
            subTotalTextValor += pagoTable.getSubtotal();
            decimosTotalTextValor += pagoTable.getDecimos();
            totalTextValor += pagoTable.getTotal();
            
        });
        
        data = FXCollections.observableArrayList(); 
        data.addAll(pagosTable);
        
        controlClienteTableView.setItems(data);
        errorText.setText("");
        
        nombreText.setText(empleado.getNombre());
        apellidoText.setText(empleado.getApellido());
        cedulaText.setText(empleado.getCedula());
        cargoText.setText(empleado.getDetallesEmpleado().getCargo().getNombre());
        sueldoText.setText(empleado.getDetallesEmpleado().getSueldo().toString());
        quincenaText.setText(empleado.getDetallesEmpleado().getQuincena().toString());
        if (empleado.getDetallesEmpleado().getAcumulaDecimos()) {
            decimosText.setText("Si");
        } else {
            decimosText.setText("No");
        }
        
        diasText.setText(diasTextValor.toString());   
        normalesText.setText(normalesTextValor.toString());
        suplementariasText.setText(suplementariasTextValor.toString());
        sobreTiempoText.setText(sobreTiempoTextValor.toString());
        sueldoTotalText.setText(String.format( "%.2f", sueldoTotalTextValor));
        extraText.setText(String.format( "%.2f", extraTextValor));
        bonosText.setText(String.format( "%.2f", bonosTextValor));
        vacacionesText.setText(String.format( "%.2f", vacacionesTextValor));
        subTotalText.setText(String.format( "%.2f", subTotalTextValor));
        decimosTotalText.setText(String.format( "%.2f", decimosTotalTextValor));
        totalText.setText(String.format( "%.2f", totalTextValor));
        
        // Calculando montos
        if (empleado.getDetallesEmpleado().getAcumulaDecimos()) {
            ingresoValor = sueldoTotalTextValor + extraTextValor + bonosTextValor;
        } else {
            ingresoValor = sueldoTotalTextValor + extraTextValor + bonosTextValor + decimosTotalTextValor;
        }
        ieesValor = (ingresoValor/100d) * getIess();  // TODO, sacar de data base
        quincenaValor = empleado.getDetallesEmpleado().getQuincena();
        deudasValor = getDeudas();
        deduccionesValor = ieesValor + quincenaValor + deudasValor;
        aPercibirValor = ingresoValor - deduccionesValor;
        
        montoIngresoText.setText(String.format( "%.2f", ingresoValor));
        montoIessText.setText(String.format( "%.2f", ieesValor));
        montoQuincenaText.setText(String.format( "%.2f", quincenaValor));
        montoDeudasText.setText(String.format( "%.2f", deudasValor));
        montoDeduccionesText.setText(String.format( "%.2f", deduccionesValor));
        montoAPercibirText.setText(String.format( "%.2f", aPercibirValor));
        
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        controlClienteTableView.setEditable(Boolean.FALSE);
        
        columnaCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        
        columnaDias.setCellValueFactory(new PropertyValueFactory<>("dias"));
        
        columnaNormales.setCellValueFactory(new PropertyValueFactory<>("normales"));
        
        columnaSumplementarias.setCellValueFactory(new PropertyValueFactory<>("suplementarias"));
        
        columnaSobreTiempo.setCellValueFactory(new PropertyValueFactory<>("sobreTiempo"));
        
        columnaHoras.getColumns().clear();
        columnaHoras.getColumns().addAll(columnaNormales, columnaSumplementarias, columnaSobreTiempo);
        
        columnaSueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));
        
        columnaExtra.setCellValueFactory(new PropertyValueFactory<>("extra"));
        
        columnaBonos.setCellValueFactory(new PropertyValueFactory<>("bonos"));
        
        columnaIngresos.getColumns().clear();
        columnaIngresos.getColumns().addAll(columnaSueldo, columnaExtra, columnaBonos);
        
        columnaVacaciones.setCellValueFactory(new PropertyValueFactory<>("vacaciones"));
        
        columnaSubTotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        
        columnaDecimos.setCellValueFactory(new PropertyValueFactory<>("decimos"));
        
        columnaTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        
        pickerDe.setEditable(false);
        pickerHasta.setEditable(false);
        
    }   
    
    public static Timestamp getToday() throws ParseException {
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date today = new Date();

        Date todayWithZeroTime = formatter.parse(formatter.format(today));
        
        return new Timestamp(todayWithZeroTime.getTime());
    }
    
    public static String getMonthName(int month){
        Calendar cal = Calendar.getInstance();
        // Calendar numbers months from 0
        cal.set(Calendar.MONTH, month - 1);
        return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }
    
    public double getIess() {
        ConstanteDAO constanteDao = new ConstanteDAO();
        Constante constante;
        constante = (Constante) constanteDao.findUniqueResultByNombre(Const.IESS);
        if (constante == null) {
            iessPorcentaje.setText("IESS (0.0%)");
            return 0.0;
        } else {
            iessPorcentaje.setText("IESS (" + constante.getValor() + "%)");
            return Double.valueOf(constante.getValor());
        }
    }
    
    public Double getDeudas() {
        Double monto = 0d;
        ArrayList<Deuda> deudas = new ArrayList<>();
        deudas.addAll(new DeudaDAO().findAllByUsuarioId(empleado.getId()));
        for (Deuda deuda: deudas) {
            if (!deuda.getPagada() && !deuda.getAplazar())
                monto += (deuda.getRestante() / deuda.getCuotas());
        }
        return monto;
    }
    
    // Login items
    @FXML
    public Button login;
    
    @FXML 
    public Label usuarioLogin;
    
    @FXML
    public void onClickLoginButton(ActionEvent event) {
        aplicacionControl.login(login, usuarioLogin);
    }
}
