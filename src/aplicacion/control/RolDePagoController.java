/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.ControlTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ActuarialesDAO;
import hibernate.dao.ConstanteDAO;
import hibernate.dao.ControlEmpleadoDAO;
import hibernate.dao.RolClienteDAO;
import hibernate.dao.SeguroDAO;
import hibernate.dao.UniformeDAO;
import hibernate.model.Actuariales;
import hibernate.model.Cliente;
import hibernate.model.Constante;
import hibernate.model.ControlEmpleado;
import hibernate.model.RolCliente;
import hibernate.model.Seguro;
import hibernate.model.Uniforme;
import hibernate.model.Usuario;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class RolDePagoController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private Usuario empleado;
    
    public ArrayList<ControlEmpleado> controlEmpleado;
    private ArrayList<ControlTable> controlEmpleadoTable;
   
    @FXML
    private Button administradoresButton;
    
    @FXML
    private Button agregarButton;
    
    @FXML
    private Pane imagenLabel;
    
    @FXML
    private Button ver;
    
    @FXML
    private TableView empleadosTableView;
    
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
    
    @FXML
    private TextField bonoField;
    
    @FXML
    private TextField transporteField;
    
    @FXML
    private TextField vacacionesField;
    
    @FXML
    private CheckBox checkVacaciones;
    
    @FXML Button expandirButton;
    
    private ObservableList<ControlTable> data;
    
    ArrayList<Usuario> usuarios;
    
    Timestamp fin;
    Timestamp inicio;
    
    private RolCliente pago;
    
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
    private void onClickCalcular(ActionEvent event) {
        setControlEmpleadoInfo(empleado, inicio, fin);
    }
    
    @FXML
    private void expandir(ActionEvent event) {
        if (expandirButton.getText().equalsIgnoreCase("Expandir")) {
            empleadosTableView.setPrefHeight(525);
            expandirButton.setText("Contraer");
        } else {
            empleadosTableView.setPrefHeight(220);
            expandirButton.setText("Expandir");
        }
    }
    
    @FXML
    public void onClickPago(ActionEvent event) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pago " + new DateTime(pago.getInicio().getTime())
                .toString("dd-MM-yyyy") + " a " + new DateTime(pago.getFinalizo()
                        .getTime()).toString("dd-MM-yyyy"));
        String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("¿Desea generar el pago del empleado " + pago.getEmpleado() + "?"), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setPrefWidth(50);
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            generarPago();
            dialogStage.close();
            dialogCompletado();
            
        });  
    }
    
    public void generarPago() {
        pago.setDetalles("");
        new RolClienteDAO().save(pago);
        
        // Registro para auditar
        String detalles = "genero un pago al empleado " 
            + empleado.getNombre() + " " + empleado.getApellido();
        aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
    }
    
    public void dialogCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Dialogo");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Pago guardado satisfactoriamente"), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            stagePrincipal.close();
        });
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
    
    @FXML
    public void mostrarHoras(ActionEvent event) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.HORAS, Permisos.Nivel.CREAR)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaHorasExtras.fxml"));
                    AnchorPane ventanaHoras = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle(empleado.getNombre() + " " + empleado.getApellido());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_registro.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaHoras);
                    ventana.setScene(scene);
                    HorasExtrasController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
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
    
    @FXML
    public void mostrarEditarHoras(ControlEmpleado controlEmpleado) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.HORAS, Permisos.Nivel.EDITAR)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEditarHorasExtras.fxml"));
                    AnchorPane ventanaHoras = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle(empleado.getNombre() + " " + empleado.getApellido());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_registro.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaHoras);
                    ventana.setScene(scene);
                    EditarHorasExtrasController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setControlEmpleado(controlEmpleado);
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
    
    @FXML
    private void onClickVacaciones(ActionEvent event) {
        if (checkVacaciones.isSelected()) {
            vacacionesField.setDisable(false);
        } else {
            vacacionesField.setDisable(true);
            vacacionesField.setText("");
        }
    }
    
    private void borrarHoras(ControlEmpleado controlEmpleado, ControlTable controlTable) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.HORAS, Permisos.Nivel.ELIMINAR)) {
               
                new ControlEmpleadoDAO().delete(controlEmpleado);
                HibernateSessionFactory.getSession().flush();
                // Registro para auditar
                String detalles = "elimino un registro diario del empleado " 
                    + empleado.getNombre() + " " + empleado.getApellido() 
                    + " con fecha " + new DateTime(controlEmpleado.getFecha()
                            .getTime()).toString("dd-MM-yyyy");
                aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());
                data.remove(controlTable);
                  
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    public void guardarRegistro(Usuario empleado, Double suplementarias, 
            Double sobreTiempo, Cliente cliente, java.sql.Date fecha, Boolean libre,
             Boolean falta) throws ParseException {
        ControlEmpleadoDAO controlEmpleadoDAO = new ControlEmpleadoDAO();
        ControlEmpleado controlEmpleado = new ControlEmpleado();
        controlEmpleado = new ControlEmpleado();
        controlEmpleado.setFecha(fecha);
        controlEmpleado.setUsuario(empleado);
        controlEmpleado.setSobretiempo(sobreTiempo);
        controlEmpleado.setRecargo(suplementarias);
        controlEmpleado.setCliente(cliente);
        controlEmpleadoDAO.save(controlEmpleado);
        setControlEmpleadoInfo(this.empleado, 
            Timestamp.valueOf(pickerDe.getValue().atStartOfDay()), 
            Timestamp.valueOf(pickerHasta.getValue().atStartOfDay()));
            
            // Registro para auditar
        String detalles = "agrego un registro diario al empleado " 
            + empleado.getNombre() + " " + empleado.getApellido() 
            + " con fecha " + new DateTime(fecha.getTime()).toString("dd-MM-yyyy");
        aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
    }
    
    public void guardarRegistroEditado(ControlEmpleado controlEmpleado, Double suplementarias, 
            Double sobreTiempo, Cliente cliente, java.sql.Date fecha, Boolean libre,
            Boolean falto) throws ParseException {
        
            controlEmpleado.setFecha(fecha);
            controlEmpleado.setCliente(cliente);
            if (libre || falto) {
                controlEmpleado.setSobretiempo(0d);
                controlEmpleado.setRecargo(0d);
            } else {
                controlEmpleado.setSobretiempo(sobreTiempo);
                controlEmpleado.setRecargo(suplementarias);
            }
            HibernateSessionFactory.getSession().flush();

            setControlEmpleadoInfo(empleado, 
                Timestamp.valueOf(pickerDe.getValue().atStartOfDay()), 
                Timestamp.valueOf(pickerHasta.getValue().atStartOfDay()));
    }
    
    public void setEmpleado(Usuario empleado, Timestamp inicio, Timestamp fin) 
            throws ParseException {
        this.empleado = empleado;
        this.inicio = inicio;
        this.fin = fin;
        
        pickerDe.setValue(Fechas.getLocalFromTimestamp(inicio));
        pickerHasta.setValue(Fechas.getLocalFromTimestamp(fin));
        
        setControlEmpleadoInfo(empleado, inicio, fin);
    }
    
    public void setControlEmpleadoInfo(Usuario empleado, Timestamp inicio, Timestamp fin) {
        
        empleadosTableView.getColumns().clear(); 
        
        ControlEmpleadoDAO controlDAO = new ControlEmpleadoDAO();
        
        Double dias = 0d;
        Double normales = 0d;
        Double sobreTiempo = 0d;
        Double suplementarias = 0d;
        
        controlEmpleado = new ArrayList<>();
        controlEmpleadoTable = new ArrayList<>();
        controlEmpleado.addAll(controlDAO.findAllByEmpleadoIdInDeterminateTime(empleado.getId(), inicio, fin));
        
        for (ControlEmpleado control: controlEmpleado) {
            dias = dias + 1;
            normales = normales + 8;
            sobreTiempo = sobreTiempo + control.getSobretiempo();
            suplementarias = suplementarias + control.getRecargo();
            
            ControlTable controlTable = new ControlTable();
            
            controlTable.setId(control.getId());
            if (control.getCliente() != null) {
                controlTable.setCliente(control.getCliente().getNombre());
            }
            DateTime dateTime = new DateTime(control.getFecha().getTime());
            controlTable.setFechaString(dateTime.toString("dd-MM-yyyy"));
            controlTable.setDia(dateTime.toCalendar(Locale.getDefault())
                            .getDisplayName(Calendar
                                    .DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            controlTable.setSobreTiempo(control.getSobretiempo());
            controlTable.setRecargo(control.getRecargo());
            //controlTable.setUsuarios(empleado);
            /*if (control.getLibre()) {
               controlTable.setDescanso("Libre"); 
            } else if (control.getFalta()) {
               controlTable.setDescanso("Falta"); 
            }*/
            controlEmpleadoTable.add(controlTable);
        }
       
        data = FXCollections.observableArrayList(); 
        if (!controlEmpleadoTable.isEmpty()) {
           data.addAll(controlEmpleadoTable);
        } 
        empleadosTableView.setItems(data);
        
        TableColumn dia = new TableColumn("Dia");
        dia.setPrefWidth(80);
        dia.setCellValueFactory(new PropertyValueFactory<>("dia"));
        
        TableColumn fecha = new TableColumn("Fecha");
        fecha.setPrefWidth(80);
        fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
       
        TableColumn cliente = new TableColumn("Cliente");
        cliente.setPrefWidth(200);
        cliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        
        TableColumn horasExtras = new TableColumn("ST");
        horasExtras.setPrefWidth(40);
        horasExtras.setCellValueFactory(new PropertyValueFactory<>("horasExtras"));
        
        TableColumn horasSuplementarias = new TableColumn("RC");
        horasSuplementarias.setPrefWidth(40);
        horasSuplementarias.setCellValueFactory(new PropertyValueFactory<>("horasSuplementarias"));
        
        TableColumn horas = new TableColumn("Horas");
        horas.setPrefWidth(80);
        horas.setCellValueFactory(new PropertyValueFactory<>("horasSuplementarias"));
        horas.getColumns().addAll(horasExtras, horasSuplementarias);
        
        TableColumn descanso = new TableColumn("Observación");
        descanso.setPrefWidth(100);
        descanso.setCellValueFactory(new PropertyValueFactory<>("descanso"));
        
        TableColumn<ControlTable, ControlTable> delete = new TableColumn<>("Borrar");
        delete.setPrefWidth(60);
        delete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        delete.setCellFactory(param -> new TableCell<ControlTable, ControlTable>() {
            private final Button deleteButton = new Button("Borrar");

            @Override
            protected void updateItem(ControlTable controlTable, boolean empty) {
                super.updateItem(controlTable, empty);

                if (controlTable == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    borrarHoras(controlDAO.findById(controlTable.getId()), controlTable);
                });
            }
        });
        
        empleadosTableView.getColumns().addAll(fecha, dia, cliente, horas, descanso, delete);
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<ControlTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    ControlTable rowData = row.getItem();
                    mostrarEditarHoras(controlDAO.findById(rowData.getId()));
                }
            });
            return row ;
        });
        
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
        Double totalRecargoDouble = sueldoHoras * Double.valueOf(suplementarias);
        totalRecargo.setText(String.format( "%.2f", totalRecargoDouble));
        Double totalSobreTiempoDouble = sueldoHoras * Double.valueOf(sobreTiempo);
        totalSobreTiempo.setText(String.format( "%.2f", totalSobreTiempoDouble));
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
        
        pago = new RolCliente();
        pago.setFecha(new Timestamp(new Date().getTime()));
        pago.setInicio(inicio);
        pago.setFinalizo(fin);
        pago.setDias(dias);
        pago.setHorasNormales(normales);
        pago.setHorasSuplementarias(suplementarias);  // RC
        pago.setHorasSobreTiempo(sobreTiempo);         // ST
        pago.setTotalHorasExtras(sobreTiempo + suplementarias);
        pago.setSalario(totalSalarioDouble);
        pago.setMontoHorasSuplementarias(totalRecargoDouble);
        pago.setMontoHorasSobreTiempo(totalSobreTiempoDouble);
        pago.setBono(totalBonoDouble);
        pago.setTransporte(totalTransporteDouble);
        pago.setTotalBonos(totalBonosDouble);
        pago.setVacaciones(totalVacacionesDouble);
        pago.setSubtotal(subTotalDouble);
        pago.setDecimoTercero(decimoTercero);
        pago.setDecimoCuarto(decimoCuarto);
        pago.setJubilacionPatronal(jubilacionPatronal);
        pago.setAportePatronal(aportePatronal);
        pago.setSeguros(segurosDecimal);
        pago.setUniformes(uniformeDecimal);
        pago.setTotalIngreso(ingresoTotal);
        pago.setEmpleado(empleado.getNombre() + " " + empleado.getApellido());
        pago.setCedula(empleado.getCedula());
        pago.setEmpresa(empleado.getDetallesEmpleado().getEmpresa().getNombre());
        pago.setSueldo(empleado.getDetallesEmpleado().getSueldo());
        pago.setUsuario(empleado);
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        empleadosTableView.setEditable(Boolean.TRUE);
        empleadosTableView.getColumns().clear(); 
        
        bonoField.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
        transporteField.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
        vacacionesField.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
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
        constante = (Constante) constanteDao.findUniqueResultByNombre(Const.DECIMO_CUARTO);
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
        Seguro seguro = new SeguroDAO().findByEmpresaId(empresaId);
        if (seguro == null) {
          return 0;  
        } else {
            return seguro.getValor();
        }  
    }
    
    public double getUniforme(Integer empresaId) {
        Uniforme uniforme = new UniformeDAO().findByEmpresaId(empresaId);
        if (uniforme == null) {
          return 0;  
        } else {
            return uniforme.getValor();
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
    
    public static EventHandler<KeyEvent> numDecimalFilter() {

        EventHandler<KeyEvent> aux = (KeyEvent keyEvent) -> {
            if (!"0123456789.".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
                
            }
        };
        return aux;
    }
    
}
