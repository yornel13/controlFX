/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import static aplicacion.control.DeudasController.numDecimalFilter;
import static aplicacion.control.DeudasController.numFilter;
import aplicacion.control.reports.ReporteDeudasVarios;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.MaterialDesignButton;
import aplicacion.control.util.Numeros;
import static aplicacion.control.util.Numeros.round;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.CuotaDeudaDAO;
import hibernate.dao.DeudaDAO;
import hibernate.dao.DeudaTipoDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.CuotaDeuda;
import hibernate.model.Deuda;
import hibernate.model.DeudaTipo;
import hibernate.model.Empresa;
import hibernate.model.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import static aplicacion.control.util.Numeros.round;
import static aplicacion.control.util.Numeros.round;
import static aplicacion.control.util.Numeros.round;

/**
 *
 * @author Yornel
 */
public class DeudasEmpleadosController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private TableView empleadosTableView;
    
    @FXML
    private TextField filterField;
    
    @FXML 
    private TableColumn cedulaColumna;
    
    @FXML 
    private TableColumn nombreColumna;
    
    @FXML 
    private TableColumn apellidoColumna;
    
    @FXML 
    private TableColumn departamentoColumna;
    
    @FXML 
    private TableColumn cargoColumna;
    
    @FXML 
    private TableColumn deudasColumna;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonDeudas;
    
    @FXML
    private Button buttonGuardar;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private GridPane marcarTodos;
    
    @FXML
    private CheckBox checkBoxDeudaTodos;
    
    @FXML
    private Label tipoText;
    
    @FXML
    private Label totalText;
    
    TableColumn cuotasColumna;
    
    TableColumn<EmpleadoTable, EmpleadoTable> agregarColumna;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    private Empresa empresa;
    
    Stage dialogLoading;
    
    Boolean enDeudaMultiple = false;
    
    private Deuda deuda;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void returnEmpresa(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarInEmpresa(empresa);
    } 
    
    public void dialogWait() {
        dialogLoading = new Stage();
        dialogLoading.initModality(Modality.APPLICATION_MODAL);
        dialogLoading.setResizable(false);
        dialogLoading.setTitle("Cargando...");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_loading.png").toExternalForm();
        dialogLoading.getIcons().add(new Image(stageIcon));
        dialogLoading.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Cargando espere...")).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogLoading.show();
    }
    
    public void generarDeudas() {
        dialogWait();
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) data) {
            if (empleadoTable.getAgregar() 
                    && Double.valueOf(empleadoTable.getMonto()) > 0d) {
                
                Deuda newDeuda;
                newDeuda = cloneDeuda(deuda);
                newDeuda.setMonto(Double.valueOf(empleadoTable.getMonto()));
                newDeuda.setRestante(Double.valueOf(empleadoTable.getMonto()));
                newDeuda.setCuotas(Integer.valueOf(empleadoTable.getCuotas()));
                newDeuda.setUsuario(findUsuarioById(empleadoTable.getId()));
                new DeudaDAO().save(newDeuda);
                new UsuarioDAO().getSession().refresh(newDeuda.getUsuario());
                
                int numeroC = newDeuda.getCuotas();
                
                Double montoCuotas = newDeuda.getRestante() / numeroC;
                montoCuotas = round(montoCuotas);

                Fecha fechaToSave = new Fecha(fecha.getFecha());

                for (int number = 0; numeroC > number; number++) {

                    CuotaDeuda cuota = new CuotaDeuda();
                    cuota.setFecha(fechaToSave.plusMonths(number).getFecha());
                    cuota.setMonto(montoCuotas);
                    cuota.setPagoMes(null);
                    cuota.setCreado(Fechas.getToday());
                    cuota.setEditado(Fechas.getToday());
                    cuota.setAplazado(Boolean.FALSE);
                    cuota.setDeuda(newDeuda);

                    new CuotaDeudaDAO().save(cuota);

                }
                
                String detalle = "agrego una deuda al empleado " 
                        + empleadoTable.getApellido()+ " " 
                        + empleadoTable.getNombre()
                        + " del tipo " + newDeuda.getTipo()
                        + " por $" + newDeuda.getMonto();
                aplicacionControl.au.saveAgrego(detalle, 
                        aplicacionControl.permisos.getUsuario());
            } 
        }
       
        filterField.clear();
        checkBoxDeudaTodos.setSelected(false);
        
        marcarTodos.setVisible(false);
        buttonImprimir.setVisible(true);
        buttonDeudas.setVisible(true);
        buttonGuardar.setVisible(false);
        
        empleadosTableView.getColumns().remove(agregarColumna);
        empleadosTableView.getColumns().remove(cuotasColumna);
        empleadosTableView.getColumns().remove(deudasColumna);
        empleadosTableView.getColumns().remove(cargoColumna);
        
        deudasColumna = new TableColumn("Total Deudas");
        deudasColumna.setCellValueFactory(
                new PropertyValueFactory<>("totalMontoDeudas"));
        deudasColumna.setMinWidth(100);
        deudasColumna.setStyle("-fx-alignment: center;");
        
        empleadosTableView.getColumns().addAll(departamentoColumna, 
                cargoColumna, deudasColumna);
        
        empleadosTableView.setEditable(false);
        
        enDeudaMultiple = false;
        
        setEmpresa(empresa);
        
        dialogLoading.close();
        
        tipoText.setText("");
        totalText.setText("");
        
        dialogoDeudasMultiplesCompletado();
    }
    
    @FXML
    public void guardarDeudas(ActionEvent event) {
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Deudas Multiples");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        MaterialDesignButton buttonOk = new MaterialDesignButton("Si");
        MaterialDesignButton buttonNo = new MaterialDesignButton("no");
        HBox hBox = HBoxBuilder.create()
                .spacing(10.0) //In case you are using HBoxBuilder
                .padding(new Insets(5, 5, 5, 5))
                .alignment(Pos.CENTER)
                .children(buttonOk, buttonNo)
                .build();
        hBox.maxWidth(120);
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("¿Seguro que desea generar la deuda(s) "
                + "a este(os) empleado(s)?"), hBox).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        buttonOk.setMinWidth(50);
        buttonNo.setMinWidth(50);
        buttonOk.setOnAction((ActionEvent e) -> {
            generarDeudas();
            dialogStage.close();
        });
        buttonNo.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        dialogStage.show();
        
        
    }
    
    public void dialogoDeudasMultiplesCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Deudas Multiples");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Deudas creadas con exito."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setPrefWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        dialogStage.show();
    }
    
    public Usuario findUsuarioById(Integer id) {
        for (Usuario usuario: usuarios) {
            if (usuario.getId().equals(id)) {
                return usuario;
            }
        }
        return  null;
    }
    
    @FXML
    public void marcarATodos(ActionEvent event) {
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) empleadosTableView.getItems()) {
            if (checkBoxDeudaTodos.isSelected()) {
                empleadoTable.setAgregar(true);
            } else {
                empleadoTable.setAgregar(false);
            }
            data.set(data.indexOf(empleadoTable), empleadoTable);
        }
    }
    
    @FXML
    public void onClickGestionarDeudas(ActionEvent event) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Deudas Multiples");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_editar.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        MaterialDesignButton buttonOk = new MaterialDesignButton("Si");
        MaterialDesignButton buttonNo = new MaterialDesignButton("no");
        HBox hBox = HBoxBuilder.create()
                .spacing(10.0) //In case you are using HBoxBuilder
                .padding(new Insets(5, 5, 5, 5))
                .alignment(Pos.CENTER)
                .children(buttonOk, buttonNo)
                .build();
        hBox.maxWidth(120);
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("¿Desea usar el modo deuda a multiples empleados?"), hBox).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        buttonOk.setMinWidth(50);
        buttonNo.setMinWidth(50);
        buttonOk.setOnAction((ActionEvent e) -> {
            nuevaDeuda();
            dialogStage.close();
        });
        buttonNo.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    public void nuevaDeuda() {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.CREAR)) {
               
                ArrayList<DeudaTipo> deudaTipos = new ArrayList<>();
                deudaTipos.addAll(new DeudaTipoDAO().findAll());
                
                String[] itemsTipos = new String[deudaTipos.size() + 1];
                deudaTipos.stream().forEach((deudaTipo) -> {
                    itemsTipos[deudaTipos.indexOf(deudaTipo)] = deudaTipo.getNombre();
                });
                itemsTipos[deudaTipos.size()] = "+ CREAR";
                
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("Nueva Deuda");
                String stageIcon = AplicacionControl.class.getResource("imagenes/icon_crear.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));
                Button buttonConfirmar = new Button("Crear");
                ChoiceBox choiceBoxTipos = new ChoiceBox();
                TextField fieldDetalles = new TextField();
                TextField fieldCuotas = new TextField();
                
                ChoiceBox selectorMes = new ChoiceBox();
                ChoiceBox selectorAno = new ChoiceBox();

                selectorMes.setItems(Fechas.arraySpinnerMes());
                selectorAno.setItems(Fechas.arraySpinnerAno());

                selectorMes.getSelectionModel().select(Fechas.getFechaActual().getMes());
                selectorAno.getSelectionModel().select(Fechas.getFechaActual().getAno());

                HBox hBox = HBoxBuilder.create()
                        .spacing(10.0) //In case you are using HBoxBuilder
                        .padding(new Insets(0, 5, 5, 5))
                        .alignment(Pos.CENTER)
                        .children(selectorMes, selectorAno)
                        .build();
                hBox.maxWidth(120);
                
                Text textTipo = new Text("Tipo");
                Text textDetalles = new Text("Detalles");
                Text textCuotas = new Text("Cuotas");
                Text textFecha = new Text("Fecha de pago (a partir de)");
                
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(textTipo, choiceBoxTipos, textDetalles, fieldDetalles, 
                        textCuotas, fieldCuotas, textFecha, hBox, buttonConfirmar).
                alignment(Pos.CENTER).padding(new Insets(20)).build()));
                choiceBoxTipos.setItems(FXCollections.observableArrayList(itemsTipos));
                fieldCuotas.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
                choiceBoxTipos.getSelectionModel().selectedIndexProperty()
                        .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> 
                            observable, Number oldValue, Number newValue) {
                        if (newValue.intValue() == (deudaTipos.size())) {
                            agregarDeuda();
                            dialogStage.close();
                        } else
                            fieldCuotas.setText(deudaTipos.get(newValue.intValue())
                                    .getCuotas().toString());
                    }
                });
                buttonConfirmar.setOnAction((ActionEvent e) -> {
                    
                    if (choiceBoxTipos.getSelectionModel().isEmpty()) {
                        
                    } else if (fieldCuotas.getText().isEmpty()) {
                        
                    } else {
                        
                        String tipo = choiceBoxTipos.getSelectionModel()
                                .getSelectedItem().toString();
                        String detalles = fieldDetalles.getText();
                        String cuotas = fieldCuotas.getText();
                        
                        Deuda newDeuda = new Deuda();
                        newDeuda.setTipo(tipo);
                        newDeuda.setTipoDeuda(deudaTipos.get(choiceBoxTipos
                                .getSelectionModel().getSelectedIndex()));
                        newDeuda.setDetalles(detalles);
                        newDeuda.setMonto(0d);
                        newDeuda.setCuotas(Integer.parseInt(cuotas));
                        newDeuda.setCuotasTotal(Integer.parseInt(cuotas));
                        newDeuda.setPagada(Boolean.FALSE);
                        newDeuda.setAplazar(Boolean.FALSE);
                        newDeuda.setRestante(0d);
                        newDeuda.setCreacion(new Timestamp(new Date().getTime()));
                        newDeuda.setUltimaModificacion(new Timestamp(new Date().getTime()));
                        
                        Fecha fecha = new Fecha(selectorAno, selectorMes, "30");
                        
                        deudaMultple(newDeuda, fecha);
                        
                        dialogStage.close();
                    }
                });  
                dialogStage.show();
                
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    private void agregarDeuda() {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.CREAR)) {
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("Nuevo tipo de deuda");
                String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));
                Button cambiarValor = new Button("Agregar");
                TextField field = new TextField();
                TextField fieldCuotas = new TextField();
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(new Text("Escriba el nombre del tipo de deuda"), field, 
                        new Text("Cuotas por defecto"), fieldCuotas, cambiarValor).
                alignment(Pos.CENTER).padding(new Insets(20)).build()));
                field.setPrefWidth(150);
                fieldCuotas.setPrefWidth(50);
                fieldCuotas.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
                cambiarValor.setPrefWidth(100);
                dialogStage.show();
                cambiarValor.setOnAction((ActionEvent e) -> {
                    if (field.getText().isEmpty()) {

                    } else if (fieldCuotas.getText().isEmpty()) {

                    } else {
                        DeudaTipo deudaTipo = new DeudaTipo();
                        deudaTipo.setActivo(Boolean.TRUE);
                        deudaTipo.setNombre(field.getText());
                        deudaTipo.setCuotas(Integer.parseInt(fieldCuotas.getText()));
                        new DeudaTipoDAO().save(deudaTipo);
                        dialogStage.close();
                        nuevaDeuda();

                        // Registro para auditar
                        String detalles = "agrego el tipo de deuda " 
                                + deudaTipo.getNombre();
                        aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
                    }
                }); 
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    public void editarDeuda(EmpleadoTable empleadoTable) {
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Edición de Deuda");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_editar.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonConfirmar = new Button("guardar");
        TextField fieldMonto = new TextField();
        TextField fieldCuotas = new TextField();
        Text textMonto = new Text("Monto");
        Text textCuotas = new Text("Cuotas");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(textMonto, 
                fieldMonto, textCuotas, fieldCuotas, buttonConfirmar).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        fieldMonto.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
        fieldCuotas.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
        fieldCuotas.setText(empleadoTable.getCuotas());
        fieldMonto.setText(empleadoTable.getMonto());
        buttonConfirmar.setOnAction((ActionEvent e) -> {

            if (fieldMonto.getText().isEmpty()) {

            } else if (fieldCuotas.getText().isEmpty()) {

            } else {

                String monto = fieldMonto.getText();
                String cuotas = fieldCuotas.getText();

                empleadoTable.setMonto(monto);
                empleadoTable.setCuotas(cuotas);
                
                data.set(data.indexOf(empleadoTable), empleadoTable);

                dialogStage.close();
            }
        });  
        dialogStage.show();


    }
    
    private Fecha fecha;
    public void deudaMultple(Deuda deuda, Fecha fecha) {
        this.fecha = fecha;
        
        tipoText.setText("Deuda por " + deuda.getTipo());
        
        this.deuda = deuda;
        
        filterField.clear();
        
        marcarTodos.setVisible(true);
        buttonImprimir.setVisible(false);
        buttonDeudas.setVisible(false);
        buttonGuardar.setVisible(true);
        
        data.stream().forEach((empleadoTable) -> {
            empleadoTable.setCuotas(deuda.getCuotas().toString());
            empleadoTable.setMonto(deuda.getMonto().toString());
        });
        
        empleadosTableView.getColumns().remove(departamentoColumna);
        
        empleadosTableView.setEditable(true);
       
        
        deudasColumna.setText("Monto");
        deudasColumna.setCellValueFactory(new PropertyValueFactory<>("monto"));
        deudasColumna.setCellFactory(TextFieldTableCell.forTableColumn());
        deudasColumna.setOnEditCommit(
            new EventHandler<CellEditEvent<EmpleadoTable, String>>() {
                @Override
                public void handle(CellEditEvent<EmpleadoTable, String> t) {
                    Double newValue;
                    try {
                        newValue = Double.valueOf(t.getNewValue());
                    } catch (NumberFormatException e) {
                        newValue = Double.valueOf(t.getOldValue());
                        dialogoErrorMonto();
                    }
                    EmpleadoTable empleadoTable = ((EmpleadoTable) t.getTableView().getItems()
                            .get(t.getTablePosition().getRow()));     
                    
                    empleadoTable.setMonto(newValue.toString());
                    if (newValue > 0d) {
                        empleadoTable.setAgregar(true);
                    } else {
                        empleadoTable.setAgregar(false);
                    }
                    data.set(data.indexOf(empleadoTable), empleadoTable);
                    sumarTotal();
                }
            }
        );
        
        cuotasColumna = new TableColumn("Cuotas");
        cuotasColumna.setMaxWidth(60);
        cuotasColumna.setMinWidth(60);
        cuotasColumna.setStyle("-fx-alignment: center;");
        cuotasColumna.setCellValueFactory(new PropertyValueFactory<>("cuotas"));
        cuotasColumna.setCellFactory(TextFieldTableCell.forTableColumn());
        cuotasColumna.setOnEditCommit(
            new EventHandler<CellEditEvent<EmpleadoTable, String>>() {
                @Override
                public void handle(CellEditEvent<EmpleadoTable, String> t) {
                    Integer newValue;
                    try {
                        newValue = Integer.valueOf(t.getNewValue());
                        if (newValue <= 0 || newValue > 12) {
                            newValue = Integer.valueOf(t.getOldValue());
                            dialogoErrorCuotas();
                        }
                    } catch (NumberFormatException e) {
                        newValue = Integer.valueOf(t.getOldValue());
                        dialogoErrorCuotas();
                    }
                    EmpleadoTable empleadoTable = ((EmpleadoTable) t.getTableView().getItems()
                            .get(t.getTablePosition().getRow()));     
                    
                    empleadoTable.setCuotas(newValue.toString());
                    data.set(data.indexOf(empleadoTable), empleadoTable);
                }
            }
        );
        
        agregarColumna = new TableColumn("Deuda");
        agregarColumna.setMaxWidth(50);
        agregarColumna.setStyle("-fx-alignment: center;");
        agregarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        agregarColumna.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            private final CheckBox checkBoxAgregar = new CheckBox();

            @Override
            protected void updateItem(EmpleadoTable empleadoTable, boolean empty) {
                super.updateItem(empleadoTable, empty);

                if (empleadoTable == null) {
                    setGraphic(null);
                    return;
                }
                
                setGraphic(checkBoxAgregar);
                if (checkBoxAgregar != null) {
                    checkBoxAgregar.setSelected(empleadoTable.getAgregar());
                }
                checkBoxAgregar.setOnAction(event -> {
                    if (Double.valueOf(empleadoTable.getMonto()) > 0d) {
                        empleadoTable.setAgregar(checkBoxAgregar.isSelected());
                        sumarTotal();
                    } else {
                        empleadoTable.setAgregar(false);
                        checkBoxAgregar.setSelected(false);
                    }
                });
            } 
        });
        empleadosTableView.getColumns().addAll(cuotasColumna, agregarColumna);
        
        totalText.setText("Total:  $0");
        
        enDeudaMultiple = true;
        
        dialogoInformacion();
    }
    
    void sumarTotal() {
        
        Double monto = 0d;
        
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) data) {
            if (empleadoTable.getAgregar() 
                    && Double.valueOf(empleadoTable.getMonto()) > 0d) {
                monto += Double.valueOf(empleadoTable.getMonto());
            } 
        }
        
        totalText.setText("Total:  $"+Numeros.round(monto).toString());
    }
    
    public void dialogoErrorMonto() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Error de monto");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Por favor ingrese un monto valido."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setPrefWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        dialogStage.show();
    }
    
    public void dialogoErrorCuotas() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Error de cuotas");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Por favor ingrese un numero valido (1-12)."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setPrefWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        dialogStage.show();
    }
    
    public void dialogoInformacion() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Sobre deudas multiples");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_editar.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Puede marcar o desmarcar los empleados a los cuales desea agregarle la deuda,"),
                new Text("adicionalmente puede editar individualmente el monto y las cuotas"),
                 new Text("haciendo click en la celda correspondiente, al finalizar utilize el boton guardar"), 
                 new Text("para agregar las deudas."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setPrefWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        dialogStage.show();
    }
    
    public void mostrarDeudas(Usuario empleado) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.EDITAR)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaDeudas.fxml"));
                    AnchorPane ventanaDeudas = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle(empleado.getNombre() + " " + empleado.getApellido());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_registro.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaDeudas);
                    ventana.setScene(scene);
                    DeudasController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(aplicacionControl);
                    controller.setProgramaDeudas(this);
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
    
    public void empleadoEditado(Usuario user) {
        for (EmpleadoTable empleadoTable: data) {
            if(Objects.equals(empleadoTable.getId(), user.getId())) {
                EmpleadoTable empleado = new EmpleadoTable();
                empleado.setId(user.getId());
                empleado.setNombre(user.getNombre());
                empleado.setApellido(user.getApellido());
                empleado.setCedula(user.getCedula());
                empleado.setEmpresa(user.getDetallesEmpleado()
                        .getEmpresa().getNombre());
                empleado.setTelefono(user.getTelefono());
                empleado.setDepartamento(user.getDetallesEmpleado()
                        .getDepartamento().getNombre());
                empleado.setCargo(user.getDetallesEmpleado()
                        .getCargo().getNombre());
               ArrayList<Deuda> deudas = new ArrayList<>();
               deudas.addAll(new DeudaDAO().findAllByEmpleadoId(user.getId()));
               Double montoDeuda = 0d;
               Integer cantidad = 0;
               for (Deuda deuda: deudas) {
                    montoDeuda += deuda.getRestante();
                    if (!deuda.getPagada()) {
                        cantidad++;
                    }
               }
               empleado.setTotalMontoDeudas(montoDeuda);
               empleado.setTotalDeudas(cantidad);
                empleado.setTotalMontoDeudas(Numeros.round(montoDeuda));
               data.set(data.indexOf(empleadoTable), empleado);
               return;
            }
        }
    }
    
    public void imprimir(File file) {
        
        dialogWait();
        
        ReporteDeudasVarios datasource = new ReporteDeudasVarios();
        datasource.addAll((List<EmpleadoTable>) empleadosTableView.getItems());
        Double total = 0d;
        for (EmpleadoTable empleadoTable: (List<EmpleadoTable>) 
                empleadosTableView.getItems()) {
            total += empleadoTable.getTotalMontoDeudas();
        }
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_DEUDAS_EMPLEADOS);
        
            Map<String, Object> parametros = new HashMap();
            parametros.put("empresa", empresa.getNombre());
            parametros.put("siglas", empresa.getSiglas());
            parametros.put("correo", "Correo: " + empresa.getEmail());
            parametros.put("detalles", 
                         "Ruc: " + empresa.getNumeracion() 
                    + " - Direccion: " + empresa.getDireccion() 
                    + " - Tel: " + empresa.getTelefono1());
            parametros.put("total", Numeros.round(total).toString());
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "deudas_" + System.currentTimeMillis();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            
            // Registro para auditar
            String detalles = "genero el recibo general de deudas de todos los empleado";
            aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
            
            dialogoCompletado();
            
            
        } catch (JRException | IOException ex) {
            Logger.getLogger(PagosTotalEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dialogLoading.close();
        }
    }
    
    public void dialogoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Imprimir deudas");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Completado."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    public File seleccionarDirectorio() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Selecciona un directorio para guardar el recibo");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));    
        return fileChooser.showDialog(stagePrincipal);
    }
    
    @FXML
    public void dialogoImprimir(ActionEvent event) {
        if (!empleadosTableView.getItems().isEmpty()) {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Imprimir Deudas");
            String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonSiDocumento = new Button("Seleccionar ruta");
            Button buttonNoDocumento = new Button("Salir");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
            children(new Text("Seleccione la ruta de guardado"), 
                    buttonSiDocumento, buttonNoDocumento).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            buttonSiDocumento.setOnAction((ActionEvent e) -> {
                File file = seleccionarDirectorio();
                if (file != null) {
                    dialogStage.close();
                    imprimir(file);
                }
            });
            buttonNoDocumento.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
            dialogStage.showAndWait();
        }
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        
        HibernateSessionFactory.getSession().clear();
        HibernateSessionFactory.getSession().flush();
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        
        usuarios.addAll(usuarioDAO.findAllByEmpresaIdConDeuda(empresa.getId()));
        System.out.println(usuarios.size()+" tamaño de users");
        if (usuarios.isEmpty()) {
            usuarios.addAll(usuarioDAO.findAllByEmpresaIdActivo(empresa.getId()));
        }
        
        data = FXCollections.observableArrayList(); 
        usuarios.stream().map((user) -> {
            EmpleadoTable empleado = new EmpleadoTable();
            empleado.setId(user.getId());
            empleado.setNombre(user.getNombre());
            empleado.setApellido(user.getApellido());
            empleado.setCedula(user.getCedula());
            empleado.setEmpresa(user.getDetallesEmpleado()
                    .getEmpresa().getNombre());
            empleado.setTelefono(user.getTelefono());
            empleado.setDepartamento(user.getDetallesEmpleado()
                    .getDepartamento().getNombre());
            empleado.setCargo(user.getDetallesEmpleado()
                    .getCargo().getNombre());
            
            ArrayList<Deuda> deudas = new ArrayList<>();
            deudas.addAll(user.getDeudas());
            Double montoDeuda = 0d;
            Integer cantidad = 0;
            for (Deuda deuda: deudas) {
                montoDeuda += deuda.getMonto();
            }
            empleado.setTotalMontoDeudas(Numeros.round(montoDeuda));
            return empleado;
        }).forEach((empleado) -> {
            data.add(empleado);
        });
        empleadosTableView.setItems(data);

        FilteredList<EmpleadoTable> filteredData = new FilteredList<>(data, p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(empleado -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (empleado.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (empleado.getApellido().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (empleado.getCedula().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (empleado.getDepartamento().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (empleado.getCargo().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        
        SortedList<EmpleadoTable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(empleadosTableView.comparatorProperty());
        empleadosTableView.setItems(sortedData);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        empleadosTableView.setEditable(Boolean.FALSE);
        
        cedulaColumna.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
       
        apellidoColumna.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        
        departamentoColumna.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        
        cargoColumna.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        deudasColumna.setCellValueFactory(new PropertyValueFactory<>("totalMontoDeudas"));
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    if (!enDeudaMultiple) {
                        mostrarDeudas(new UsuarioDAO().findById(rowData.getId()));
                    }
                }
            });
            return row ;
        });
        buttonAtras.setOnMouseEntered((MouseEvent t) -> {
            buttonAtras.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/atras.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonAtras.setOnMouseExited((MouseEvent t) -> {
            buttonAtras.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/atras.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        buttonDeudas.setTooltip(
            new Tooltip("Deudas por tipo de cuenta")
        );
        buttonDeudas.setOnMouseEntered((MouseEvent t) -> {
            buttonDeudas.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/deudas.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonDeudas.setOnMouseExited((MouseEvent t) -> {
            buttonDeudas.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/deudas.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        buttonImprimir.setTooltip(
            new Tooltip("Imprimir")
        );
        buttonImprimir.setOnMouseEntered((MouseEvent t) -> {
            buttonImprimir.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/imprimir.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonImprimir.setOnMouseExited((MouseEvent t) -> {
            buttonImprimir.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/imprimir.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        buttonGuardar.setTooltip(
            new Tooltip("Guardar deudas")
        );
        buttonGuardar.setOnMouseEntered((MouseEvent t) -> {
            buttonGuardar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/guardar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonGuardar.setOnMouseExited((MouseEvent t) -> {
            buttonGuardar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/guardar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
    } 
    
    private static Deuda cloneDeuda(Deuda obj){
        try{
            Deuda clone = obj.getClass().newInstance();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                field.set(clone, field.get(obj));
            }
            return clone;
        }catch(Exception e){
            return null;
        }
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
