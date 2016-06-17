/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.ControlTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.Fechas;
import static aplicacion.control.util.Numeros.round;
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
import hibernate.model.Empresa;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
public class RolDePagoClienteController implements Initializable {
    
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
    
    @FXML 
    private Button expandirButton;
    
    @FXML
    private GridPane gridPaneTotal;
    
     @FXML
    private Button buttonAnterior;
    
    @FXML
    private Button buttonSiguiente;
    
    private Cliente cliente;
    
    private ObservableList<ControlTable> data;
    
    ArrayList<Usuario> usuarios;
    
    private RolCliente pago;
    
    private Boolean editable = true;
    
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
    private void onClickCalcular(ActionEvent event) {
        if (editable)
            setControlEmpleadoInfo(empleado, inicio, fin);
        else 
            dialogError();
    }
    
    @FXML
    private void expandir(ActionEvent event) {
        if (expandirButton.getText().equalsIgnoreCase("Expandir")) {
            empleadosTableView.setPrefHeight(495);
            expandirButton.setText("Contraer");
            gridPaneTotal.setVisible(false);
        } else {
            empleadosTableView.setPrefHeight(220);
            expandirButton.setText("Expandir");
            gridPaneTotal.setVisible(true);
        }
    }
    
    @FXML
    public void onClickMore(ActionEvent event) {
        pickerDe.setValue(pickerDe.getValue().plusMonths(1));
        pickerHasta.setValue(pickerHasta.getValue().plusMonths(1));
        inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay()); 
        mostrarRegistro(null);
    }
    
    @FXML
    public void onClickLess(ActionEvent event) {
        pickerDe.setValue(pickerDe.getValue().minusMonths(1));
        pickerHasta.setValue(pickerHasta.getValue().minusMonths(1));
        inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());
        mostrarRegistro(null);
    }
    
    @FXML
    public void onClickPago(ActionEvent event) {
        
        String faltantes = "";
        if (pago.getVacaciones().equals(0d)) {
            faltantes += " Sin vacaciones.";
        } 
        if (pago.getBono().equals(0d)) {
            faltantes += " Sin bono.";
        }
        if (pago.getTransporte().equals(0d)) {
            faltantes += " Sin transporte.";
        }
        
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
        children(new Text("¿Desea generar el rol de pago del empleado " + pago.getEmpleado()),
                new Text("para el cliente " + pago.getClienteNombre()+ "?" + faltantes), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setPrefWidth(50);
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            generarPago();
            dialogStage.close();
            
        });  
    }
    
    public void generarPago() {
        
        if (new RolClienteDAO().findByFechaAndEmpleadoIdAndClienteId(fin, 
                empleado.getId(), cliente.getId()) == null) {
        
            pago.setDetalles("");
            new RolClienteDAO().save(pago);

            // Registro para auditar
            String detalles = "genero un pago al empleado " 
                + empleado.getNombre() + " " + empleado.getApellido() + " para el cliente " 
                    + pago.getClienteNombre();
            aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
            
            dialogCompletado();
        } else {
            dialogError();
        }
    }
    
    public void dialogCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Completado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Rol de cliente guardado satisfactoriamente."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            stagePrincipal.close();
        });
    }
    
    public void dialogError() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("No se guardo");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Ya hay un rol creado con esta fecha y con este cliente."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
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
        if (editable)
            if (aplicacionControl.permisos == null) {
               aplicacionControl.noLogeado();
            } else {
                if (aplicacionControl.permisos.getPermiso(Permisos.HORAS, Permisos.Nivel.CREAR)) {
                    try {
                        FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaHorasExtrasCliente.fxml"));
                        AnchorPane ventanaHoras = (AnchorPane) loader.load();
                        Stage ventana = new Stage();
                        ventana.setTitle(empleado.getNombre() + " " + empleado.getApellido());
                        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_registro.png").toExternalForm();
                        ventana.getIcons().add(new Image(stageIcon));
                        ventana.setResizable(false);
                        ventana.initOwner(stagePrincipal);
                        Scene scene = new Scene(ventanaHoras);
                        ventana.setScene(scene);
                        HorasExtrasClienteController controller = loader.getController();
                        controller.setStagePrincipal(ventana);
                        controller.setProgramaPrincipal(this);
                        controller.setEmpleado(empleado, cliente);
                        ventana.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                        //tratar la excepción
                    }
                } else {
                    aplicacionControl.noPermitido();
                }
            } 
        else {
            dialogError();
        }
    }
    
    @FXML
    public void mostrarEditarHoras(ControlEmpleado controlEmpleado) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.HORAS, Permisos.Nivel.EDITAR)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEditarHorasExtrasCliente.fxml"));
                    AnchorPane ventanaHoras = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle(empleado.getNombre() + " " + empleado.getApellido());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_registro.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaHoras);
                    ventana.setScene(scene);
                    EditarHorasExtrasClienteController controller = loader.getController();
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
        if (editable)
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
        else {
            dialogError();
        }
    }
    
    public void guardarRegistro(Usuario empleado, Double suplementarias, 
            Double sobreTiempo, Cliente cliente, Timestamp fecha, Boolean libre,
            Boolean falta) throws ParseException {
        ControlEmpleadoDAO controlEmpleadoDAO = new ControlEmpleadoDAO();
        ControlEmpleado controlEmpleado = new ControlEmpleado();
        controlEmpleado = new ControlEmpleado();
        controlEmpleado.setFecha(fecha);
        controlEmpleado.setLibre(libre);
        controlEmpleado.setFalta(falta);
        controlEmpleado.setUsuario(empleado);
        controlEmpleado.setHorasExtras(sobreTiempo);
        controlEmpleado.setHorasSuplementarias(suplementarias);
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
            Double sobreTiempo, Cliente cliente, Timestamp fecha, Boolean libre,
            Boolean falta) throws ParseException {
        
        controlEmpleado.setFecha(fecha);
        controlEmpleado.setCliente(cliente);
        controlEmpleado.setLibre(libre);
        controlEmpleado.setFalta(falta);
        if (libre || falta) {
            controlEmpleado.setHorasExtras(0d);
            controlEmpleado.setHorasSuplementarias(0d);
        } else {
            controlEmpleado.setHorasExtras(sobreTiempo);
            controlEmpleado.setHorasSuplementarias(suplementarias);
        }
        HibernateSessionFactory.getSession().flush();
        
        setControlEmpleadoInfo(empleado, 
                Timestamp.valueOf(pickerDe.getValue().atStartOfDay()), 
                Timestamp.valueOf(pickerHasta.getValue().atStartOfDay()));
        
        // Registro para auditar
        String detalles = "edito un registro diario del empleado " 
            + empleado.getNombre() + " " + empleado.getApellido() 
            + " con fecha " + new DateTime(fecha.getTime()).toString("dd-MM-yyyy");
        aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
    }
    
    public void setEmpleado(Usuario empleado, Cliente cliente, Timestamp inicio, Timestamp fin) throws ParseException {
        this.empleado = empleado;
        this.cliente = cliente;
        this.inicio = inicio;
        this.fin = fin;
        
        pickerDe.setValue(Fechas.getDateFromTimestamp(inicio));
        pickerHasta.setValue(Fechas.getDateFromTimestamp(fin));
        
        setControlEmpleadoInfo(empleado, inicio, fin);
    }
    
    public void setControlEmpleadoInfo(Usuario empleado, Timestamp inicio, Timestamp fin) {
        
        empleadosTableView.getColumns().clear(); 
        
        ControlEmpleadoDAO controlDAO = new ControlEmpleadoDAO();
        
        Integer dias = 0;
        Double normales = 0d;
        Double sobreTiempo = 0d;
        Double suplementarias = 0d;
        
        controlEmpleado = new ArrayList<>();
        controlEmpleadoTable = new ArrayList<>();
        controlEmpleado.addAll(controlDAO.findAllByEmpleadoIdClienteIdInDeterminateTime(empleado.getId(), cliente.getId(), inicio, fin));
        
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
            DateTime dateTime = new DateTime(control.getFecha().getTime());
            controlTable.setFecha(dateTime.toString("dd-MM-yyyy"));
            controlTable.setDia(dateTime.toCalendar(Locale.getDefault())
                            .getDisplayName(Calendar
                                    .DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            controlTable.setHorasExtras(control.getHorasExtras());
            controlTable.setHorasSuplementarias(control.getHorasSuplementarias());
            controlTable.setUsuarios(empleado);
            if (control.getLibre()) {
               controlTable.setDescanso("Libre"); 
            } else if (control.getFalta()) {
               controlTable.setDescanso("Falta"); 
            } 
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
        
        TableColumn descanso = new TableColumn("Descanso");
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
                    borrarHoras(controlDAO
                            .findById(controlTable.getId()), controlTable);
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
        
        normales = round(normales);
        sobreTiempo = round(sobreTiempo);
        suplementarias = round(suplementarias);
        
        totalDias.setText(dias.toString());
        totalHorasN.setText(normales.toString());
        totalHorasRC.setText(suplementarias.toString());
        totalHorasST.setText(sobreTiempo.toString());
        totalHorasExtras.setText(round(sobreTiempo + suplementarias).toString());
        
        // Salario
        Double totalSalarioDouble = round(sueldoDia * Double.valueOf(dias));
        totalSalario.setText(totalSalarioDouble.toString());
        Double totalSobreTiempoDouble = round(sueldoHoras * sobreTiempo);
        totalSobreTiempo.setText(totalSobreTiempoDouble.toString());
        Double totalRecargoDouble = round(sueldoHoras * suplementarias);
        totalRecargo.setText(totalRecargoDouble.toString());
        Double totalBonoDouble = round(getBono());
        totalBono.setText(totalBonoDouble.toString());
        Double totalTransporteDouble = round(getTransporte());
        totalTransporte.setText(totalTransporteDouble.toString());
        Double totalBonosDouble = round(totalBonoDouble + totalTransporteDouble);
        totalBonos.setText(totalBonosDouble.toString());
        Double totalVacacionesDouble = round(getVacaciones());
        totalVacaciones.setText(totalVacacionesDouble.toString());
        Double subTotalDouble = round(totalSalarioDouble 
                + totalSobreTiempoDouble + totalRecargoDouble 
                + totalBonosDouble + totalVacacionesDouble);
        subTotal.setText(subTotalDouble.toString());
        ////////////////////////////////////////////////////
        Double decimoTercero = round(subTotalDouble / 12d);
        totalDecimo3.setText(decimoTercero.toString());
        Double decimoCuarto = round(getDecimoCuarto()/30d * Double.valueOf(dias));
        totalDecimo4.setText(decimoCuarto.toString());
        totalReserva.setText(decimoTercero.toString());
        Double jubilacionPatronal = round((getActuariales(empleado.getId())/ 360d) * Double.valueOf(dias));
        totalJubilacion.setText(jubilacionPatronal.toString());
        Double aportePatronal = round(subTotalDouble * 12.15d / 100d);
        totalAporte.setText(aportePatronal.toString());
        Double segurosDecimal = round(getSeguro(empleado.getDetallesEmpleado()
                .getEmpresa().getId()) * Double.valueOf(dias));
        totalSeguros.setText(segurosDecimal.toString());
        Double uniformeDecimal = round(getUniforme(empleado.getDetallesEmpleado()
                .getEmpresa().getId()) * Double.valueOf(dias));
        totalUniformes.setText(uniformeDecimal.toString());
        
        Double ingresoTotal = round(subTotalDouble + decimoTercero 
                + decimoCuarto + decimoTercero + jubilacionPatronal 
                + aportePatronal + segurosDecimal + uniformeDecimal);
        totalIngresos.setText(ingresoTotal.toString());
        
        pago = new RolClienteDAO().findByFechaAndEmpleadoIdAndClienteId(fin, 
                this.empleado.getId(), this.cliente.getId());
        if (pago == null) {
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
            pago.setCliente(this.cliente);
            pago.setClienteNombre(this.cliente.getNombre());
            
            editable = true;
        } else {
            editable = false;
            vacacionesField.setText(pago.getVacaciones().toString());
            totalVacaciones.setText(pago.getVacaciones().toString());
            bonoField.setText(pago.getBono().toString());
            totalBono.setText(pago.getBono().toString());
            transporteField.setText(pago.getTransporte().toString());
            totalTransporte.setText(pago.getTransporte().toString());
            totalBonos.setText(pago.getTotalBonos().toString());
            subTotal.setText(pago.getSubtotal().toString());
            totalDecimo3.setText(pago.getDecimoTercero().toString());
            totalDecimo4.setText(pago.getDecimoCuarto().toString());
            totalReserva.setText(pago.getDecimoTercero().toString());
            totalJubilacion.setText(pago.getJubilacionPatronal().toString());
            totalJubilacion.setText(pago.getJubilacionPatronal().toString());
            totalSeguros.setText(pago.getSeguros().toString());
            totalUniformes.setText(pago.getUniformes().toString());
            totalIngresos.setText(pago.getTotalIngreso().toString());
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        empleadosTableView.setEditable(Boolean.TRUE);
        empleadosTableView.getColumns().clear(); 
        
        bonoField.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
        transporteField.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
        vacacionesField.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
        
        pickerDe.setEditable(false);
        pickerHasta.setEditable(false);
        
        buttonAnterior.setTooltip(
            new Tooltip("Mes Anterior")
        );
        buttonAnterior.setOnMouseEntered((MouseEvent t) -> {
            buttonAnterior.setStyle("-fx-background-color: #29B6F6;");
        });
        buttonAnterior.setOnMouseExited((MouseEvent t) -> {
            buttonAnterior.setStyle("-fx-background-color: #039BE5;");
        });
        buttonSiguiente.setTooltip(
            new Tooltip("Mes Siguiente")
        );
        buttonSiguiente.setOnMouseEntered((MouseEvent t) -> {
            buttonSiguiente.setStyle("-fx-background-color: #29B6F6;");
        });
        buttonSiguiente.setOnMouseExited((MouseEvent t) -> {
            buttonSiguiente.setStyle("-fx-background-color: #039BE5;");
        });
    }  
    
    public static String getMonthName(int month){
        Calendar cal = Calendar.getInstance();
        // Calendar numbers months from 0
        cal.set(Calendar.MONTH, month - 1);
        return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }
    
    public Double getDecimoCuarto() {
        ConstanteDAO constanteDao = new ConstanteDAO();
        Constante constante;
        constante = (Constante) constanteDao.findUniqueResultByNombre(Const.DECIMO_CUARTO);
        if (constante == null) {
            return 30.5d;
        } else {
            return Double.valueOf(constante.getValor());
        }
    }
    
    public Double getActuariales(Integer empleadoId) {
        ActuarialesDAO actuarialesDAO = new ActuarialesDAO();
        Actuariales actuariales;
        actuariales = actuarialesDAO.findByEmpleadoId(empleadoId);
        if (actuariales == null) {
            return 0d;
        } else {
            return actuariales.getPrimario() + actuariales.getSecundario();
        }
    }
    
    public Double getSeguro(Integer empresaId) {
        Seguro seguro = new SeguroDAO().findByEmpresaId(empresaId);
        if (seguro == null) {
          return 0d;  
        } else {
            return seguro.getValor();
        }  
    }
    
    public Double getUniforme(Integer empresaId) {
        Uniforme uniforme = new UniformeDAO().findByEmpresaId(empresaId);
        if (uniforme == null) {
          return 0d;  
        } else {
            return uniforme.getValor();
        }  
    }
    
    public Double getBono() {
        if (bonoField.getText().isEmpty()) {
            return 0d;
        } else {
            return Double.valueOf(bonoField.getText());
        }
    }
    
    public Double getTransporte() {
        if (transporteField.getText().isEmpty()) {
            return 0d;
        } else {
            return Double.valueOf(transporteField.getText());
        }
    }
    
    public Double getVacaciones() {
        if (vacacionesField.getText().isEmpty()) {
            return 0d;
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
