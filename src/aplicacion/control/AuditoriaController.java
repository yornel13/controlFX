/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import static aplicacion.control.DeudaController.numFilter;
import aplicacion.control.reports.ReporteAuditoria;
import aplicacion.control.util.Const;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.MaterialDesignButton;
import hibernate.dao.AccionTipoDAO;
import hibernate.dao.IdentidadDAO;
import hibernate.dao.RegistroAccionesDAO;
import hibernate.model.AccionTipo;
import hibernate.model.Identidad;
import hibernate.model.RegistroAcciones;
import hibernate.model.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author Yornel
 */
public class AuditoriaController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private ArrayList<AccionTipo> acciones;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private TableView accionesTableView;
    
    @FXML 
    private TableColumn horaColumna;
    
    @FXML 
    private TableColumn fechaColumna;
    
    @FXML 
    private TableColumn detallesColumna;
    
    @FXML
    private ChoiceBox accionesSelector;
    
    @FXML 
    private TextField filterField;
    
    @FXML
    private DatePicker pickerDe;
    
    @FXML 
    private DatePicker pickerHasta;
    
    @FXML 
    private Button buttonEmpleado;
    
    @FXML 
    private Button buttonSearch;
    
    @FXML 
    private Button buttonDelete;
    
    private ObservableList<RegistroAcciones> data;
    private Stage dialogLoading;
    
    ArrayList<Usuario> administradores;
    
    Usuario usuarioSelected;
    
    public Timestamp inicio;
    public Timestamp fin;
    
    @FXML
    private void returnConfiguracion(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarConfiguracion();
    }
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
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
    
    public void imprimir(File file, int count) {
        
        dialogWait();
        List<RegistroAcciones> limitando = new ArrayList<>();
        for (RegistroAcciones accion: (List<RegistroAcciones>) accionesTableView.getItems()) {
            if (((List<RegistroAcciones>) accionesTableView.getItems()).indexOf(accion) < count) {
                limitando.add(accion);
            }
        }
        ReporteAuditoria datasource = new ReporteAuditoria();
        datasource.addAll(limitando);
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_AUDITORIA);
        
            Map<String, Object> parametros = new HashMap();
            parametros.put("detalles", "Reporte impreso por: " + 
                    aplicacionControl.permisos.getUsuario().getNombre() + " " + 
                    aplicacionControl.permisos.getUsuario().getApellido());
            String detalles2 = "";
            if (aplicacionControl.permisos.getUsuario().getDetallesEmpleado() != null) {
                detalles2 = "Cargo: " + aplicacionControl.permisos.getUsuario()
                        .getDetallesEmpleado().getCargo().getNombre();
            }
            parametros.put("detalles2", detalles2);
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "auditoria_" + System.currentTimeMillis();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            
            // Registro para auditar
            String detalles = "genero un reporte de acciones hechas por los usarios.";
            aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
            
            dialogoCompletado();
            
            
        } catch (JRException | IOException ex) {
            ex.printStackTrace();
        } finally {
            dialogLoading.close();
        }
    }
    
    public void selecionarCantidad() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Imprirmi");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_editar.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("IMPRIMIR");
        TextField field = new TextField();
        HBox hBox = HBoxBuilder.create()
                    .spacing(10.0) //In case you are using HBoxBuilder
                    .padding(new Insets(0, 5, 5, 5))
                    .alignment(Pos.CENTER)
                    .children(new Text("Los primeros:"), field)
                    .build();
        hBox.maxWidth(120);
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Indique el numero de registros a imprimir."), hBox, buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        field.setMaxWidth(50);
        field.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
        buttonOk.setOnAction((ActionEvent e) -> {

            if (field.getText() != null && Integer.parseInt(field.getText()) > 0) {

                int count = Integer.valueOf(field.getText());
                dialogStage.close();
                seleccionRuta(count);
            }
        });
        dialogStage.showAndWait();
    }
    
    public void dialogoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Imprimir");
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
        fileChooser.setTitle("Selecciona un directorio para guardar la auditoria");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));    
        return fileChooser.showDialog(stagePrincipal);
    }
    
    @FXML
    public void onDeleteFilter(ActionEvent event) {
        usuarioSelected = null;
        buttonEmpleado.setText("Seleccionar usuario....");
        accionesSelector.getSelectionModel().select(0);
        pickerDe.setValue(null);
        pickerHasta.setValue(null);
        inicio = null;
        fin = null;
        verAcciones();
    }
    
    @FXML
    public void dialogoImprimir(ActionEvent event) {
        selecionarCantidad();
    }
    
    
   public void seleccionRuta(int count) {
       Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Imprimir");
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
                imprimir(file, count);
            }
        });
        buttonNoDocumento.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
   }
    
    private void verAcciones() {
        filterField.clear();
   
        if (pickerDe.getValue() != null)
            inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        if (pickerHasta.getValue() != null)
            fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());
        
        if ((pickerDe.getValue() == null && pickerHasta.getValue() != null) 
                || (pickerHasta.getValue() == null && pickerDe.getValue() != null)
                || ((pickerHasta.getValue() != null && pickerDe.getValue() != null) && inicio.getTime() > fin.getTime())) {
            errorFecha();
        } else {
            
            if (pickerHasta.getValue() != null)
                fin = Timestamp.valueOf(pickerHasta.getValue().plusDays(1).atStartOfDay());
            
            if (!accionesSelector.getSelectionModel().isEmpty()
                    && accionesSelector.getSelectionModel().getSelectedIndex() != 0) {
                if (usuarioSelected != null) 
                    setAccionesTipo(usuarioSelected);
                else
                    setAccionesTipo();
            } else {
                if (usuarioSelected != null) 
                    setAcciones(usuarioSelected);
                else
                setAcciones();
            }
        }
        
    }
    
    public void errorFecha() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        MaterialDesignButton buttonOk = new MaterialDesignButton("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
        children(new Text("Una fecha es nula o el rango de fechas es incorrecto."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        dialogStage.show();
        buttonOk.setPrefWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
    }
    
    @FXML
    public void onSelectUser(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(AplicacionControl.class
                .getResource("ventanas/VentanaSeleccionarAdministrador.fxml"));
        AnchorPane ventanaRolDePago = (AnchorPane) loader.load();
        Stage ventana = new Stage();
        ventana.setTitle("");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/security_dialog.png").toExternalForm();
        ventana.getIcons().add(new Image(stageIcon));
        ventana.setResizable(false);
        ventana.initOwner(stagePrincipal);
        Scene scene = new Scene(ventanaRolDePago);
        ventana.setScene(scene);
        SeleccionarAdministradorController controller = loader.getController();
        controller.setStagePrincipal(ventana);
        controller.setParentController(this);
        controller.setUsuarios(administradores);
        ventana.show();
    }
    
    @FXML
    public void onSearchActions(ActionEvent event) {
        verAcciones();
    }
    
    void setAdministrador(Usuario usuario) {
        usuarioSelected = usuario;
        buttonEmpleado.setText(usuario.getApellido()+" "+usuario.getNombre());
    }
    
    public void setAcciones() {
        RegistroAccionesDAO registroAccionesDAO = new RegistroAccionesDAO();
        data = FXCollections.observableArrayList(); 
        if (inicio != null)
            data.addAll(registroAccionesDAO.findAllDesc(inicio, fin));
        else
            data.addAll(registroAccionesDAO.findAllDesc());
        accionesTableView.setItems(data); 
        setFiltro();
    }
    
    public void setAcciones(Usuario administrador) {
        RegistroAccionesDAO registroAccionesDAO = new RegistroAccionesDAO();
        data = FXCollections.observableArrayList(); 
        if (inicio != null)
            data.addAll(registroAccionesDAO.findAllDescByUsuarioId(inicio, fin, administrador.getId()));
        else
            data.addAll(registroAccionesDAO.findAllDescByUsuarioId(administrador.getId()));
        accionesTableView.setItems(data); 
        setFiltro();
    }
    
    public void setAccionesTipo() {
        Integer accionId = acciones.get(accionesSelector.getSelectionModel().getSelectedIndex() - 1).getId();
        
        RegistroAccionesDAO registroAccionesDAO = new RegistroAccionesDAO();
        data = FXCollections.observableArrayList(); 
        if (inicio != null)
           data.addAll(registroAccionesDAO.findAllByAccionIdDesc(inicio, fin, accionId)); 
        else
            data.addAll(registroAccionesDAO.findAllByAccionIdDesc(accionId));
        accionesTableView.setItems(data); 
        setFiltro();
    }
    
    public void setAccionesTipo(Usuario administrador) {
        Integer accionId = acciones.get(accionesSelector.getSelectionModel().getSelectedIndex() - 1).getId();
        
        RegistroAccionesDAO registroAccionesDAO = new RegistroAccionesDAO();
        data = FXCollections.observableArrayList(); 
        if (inicio != null)
            data.addAll(registroAccionesDAO.findAllDescByUsuarioIdAndAccionId(inicio, fin, administrador.getId(), accionId));
        else
            data.addAll(registroAccionesDAO.findAllDescByUsuarioIdAndAccionId(administrador.getId(), accionId));
        accionesTableView.setItems(data); 
        setFiltro();
    }
    
    public void setFiltro() {
       FilteredList<RegistroAcciones> filteredData = new FilteredList<>(data, p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(acciones -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (acciones.getUsuario().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (acciones.getUsuario().getApellido().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (acciones.getDetalles().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        
        SortedList<RegistroAcciones> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(accionesTableView.comparatorProperty());
        accionesTableView.setItems(sortedData); 
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        accionesTableView.setEditable(Boolean.FALSE);
        
        horaColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<RegistroAcciones,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<RegistroAcciones, String> data) {
                
                String hora;

                Date date = new Date(data.getValue().getFecha().getTime());
                DateFormat df = new SimpleDateFormat("hh:mm a");
                hora = df.format(date);
                
                return new ReadOnlyStringWrapper(hora);
            }
        });
        
        fechaColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<RegistroAcciones,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<RegistroAcciones, String> data) {
                
                String fecha;
                
                fecha = Fechas.getFechaCorta(data.getValue().getFecha());
                
                return new ReadOnlyStringWrapper(fecha);
            }
        });
       
        detallesColumna.setCellValueFactory(new PropertyValueFactory<>("detalles"));
        
        setAcciones();
        
        AccionTipoDAO accionTipoDAO = new AccionTipoDAO();
        acciones = new ArrayList<>();
        acciones.addAll(accionTipoDAO.findAll());
        
        String[] itemsAccion = new String[acciones.size() + 1];
        
        itemsAccion[0] = "TODO";
        acciones.stream().forEach((accion) -> {
            itemsAccion[acciones.indexOf(accion)+1] = accion.getNombre();
        });
        
        accionesSelector.setItems(FXCollections.observableArrayList(itemsAccion));
        
        IdentidadDAO identidadDAO = new IdentidadDAO();
        List<Identidad> identidades = (List<Identidad>) identidadDAO.findAll();
        
        administradores = new ArrayList<>();
        if (identidades != null) {
            for (Identidad identidad: identidades) {
                administradores.add(identidad.getUsuario());
            }
        }
        
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
        
        buttonSearch.setTooltip(
            new Tooltip("Buscar")
        );
        buttonSearch.setOnMouseEntered((MouseEvent t) -> {
            buttonSearch.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/search.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonSearch.setOnMouseExited((MouseEvent t) -> {
            buttonSearch.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/search.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        
        buttonDelete.setTooltip(
            new Tooltip("Borrar datos de filtro")
        );
        buttonDelete.setOnMouseEntered((MouseEvent t) -> {
            buttonDelete.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/delete.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonDelete.setOnMouseExited((MouseEvent t) -> {
            buttonDelete.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/delete.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        
        accionesSelector.getSelectionModel().select(0);
        
        pickerDe.setEditable(false);
        pickerHasta.setEditable(false);
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