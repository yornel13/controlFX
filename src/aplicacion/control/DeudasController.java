/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteDeudas;
import aplicacion.control.util.Const;
import aplicacion.control.util.CorreoUtil;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.DeudaDAO;
import hibernate.dao.DeudaTipoDAO;
import hibernate.model.Deuda;
import hibernate.model.DeudaTipo;
import hibernate.model.Empresa;
import hibernate.model.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
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

/**
 *
 * @author Yornel
 */
public class DeudasController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private Empresa empresa;
    
    private Usuario empleado;
    
    // Columnas
    @FXML
    private TableColumn fechaColumna;
    
    @FXML
    private TableColumn detallesColumna;
    
    @FXML
    private TableColumn montoColumna;
    
    @FXML
    private TableColumn restanteColumna;
    
    @FXML
    private TableColumn cuotasColumna;
    
    @FXML
    private TableColumn tipoColumna;
    
    @FXML
    private TableColumn<Deuda, Deuda> borrarColumna;
    
    @FXML
    private Button buttonAgregar;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private TableView deudasTableView;
    
    private ObservableList<Deuda> data;
    
    ArrayList<Deuda> deudas;
    
    Stage dialogLoading;
    
    private DeudasEmpleadosController deudasEmpleadosController;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void setProgramaDeudas(DeudasEmpleadosController deudasEmpleadosController) {
        this.deudasEmpleadosController = deudasEmpleadosController;
    }
    
    @FXML
    private void agregarDeuda(ActionEvent event) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.CREAR)) {
               
               nuevaDeuda();
                  
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    public void nuevaDeuda() {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.CREAR)) {
               
                ArrayList<DeudaTipo> deudaTipos = new ArrayList<>();
                deudaTipos.addAll(new DeudaTipoDAO().findAll());
                
                String[] itemsTipos = new String[deudaTipos.size()];
                deudaTipos.stream().forEach((deudaTipo) -> {
                    itemsTipos[deudaTipos.indexOf(deudaTipo)] = deudaTipo.getNombre();
                });
                
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("Nueva Deuda");
                String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));
                Button buttonConfirmar = new Button("Crear");
                ChoiceBox choiceBoxTipos = new ChoiceBox();
                TextField fieldDetalles = new TextField();
                TextField fieldMonto = new TextField();
                TextField fieldCuotas = new TextField();
                Text textTipo = new Text("Tipo");
                Text textDetalles = new Text("Detalles");
                Text textMonto = new Text("Monto");
                Text textCuotas = new Text("Cuotas");
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(textTipo, choiceBoxTipos, textDetalles, fieldDetalles, textMonto, 
                        fieldMonto, textCuotas, fieldCuotas, buttonConfirmar).
                alignment(Pos.CENTER).padding(new Insets(20)).build()));
                choiceBoxTipos.setItems(FXCollections.observableArrayList(itemsTipos));
                fieldMonto.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
                fieldCuotas.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
                choiceBoxTipos.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        fieldCuotas.setText(deudaTipos.get(newValue.intValue()).getCuotas().toString());
                    }
                });
                buttonConfirmar.setOnAction((ActionEvent e) -> {
                    
                    if (choiceBoxTipos.getSelectionModel().isEmpty()) {
                        
                    } else if (fieldDetalles.getText().isEmpty()) {
                        
                    } else if (fieldMonto.getText().isEmpty()) {
                        
                    } else if (fieldCuotas.getText().isEmpty()) {
                        
                    } else {
                        
                        String tipo = choiceBoxTipos.getSelectionModel().getSelectedItem().toString();
                        String detalles = fieldDetalles.getText();
                        String monto = fieldMonto.getText();
                        String cuotas = fieldCuotas.getText();
                        
                        Deuda newDeuda = new Deuda();
                        newDeuda.setTipo(tipo);
                        newDeuda.setDetalles(detalles);
                        newDeuda.setMonto(Double.valueOf(monto));
                        newDeuda.setCuotas(Integer.parseInt(cuotas));
                        newDeuda.setPagada(Boolean.FALSE);
                        newDeuda.setAplazar(Boolean.FALSE);
                        newDeuda.setRestante(Double.valueOf(monto));
                        newDeuda.setCreacion(new Timestamp(new Date().getTime()));
                        newDeuda.setUltimaModificacion(new Timestamp(new Date().getTime()));
                        newDeuda.setUsuario(empleado);
                        
                        new DeudaDAO().save(newDeuda);
                        dialogStage.close();
                        
                        String detalle = "agrego una deudo al empleado " 
                            + empleado.getNombre() + " " + empleado.getApellido() 
                                + " por " + monto + "$";
                        aplicacionControl.au.saveElimino(detalle, aplicacionControl.permisos.getUsuario());
                        
                        setEmpleado(empleado);
                        deudasEmpleadosController.empleadoEditado(empleado);
                        
                        generacionCompletada();
                    }
                });  
                dialogStage.showAndWait();
                
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    public void generacionCompletada() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Completado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Deuda creada satisfactoriamente"), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
    }
    
    public void deleteDeuda(Deuda deuda) {
        if (Objects.equals(deuda.getMonto(), deuda.getRestante())) {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Confirmacion");
            String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new Button("Borrar");
            Button buttonNo= new Button("Cancelar");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
            children(new Text("¿Seguro que desea eliminar esta deuda?"), buttonOk, buttonNo).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            buttonOk.setOnAction((ActionEvent e) -> {
                new DeudaDAO().delete(deuda);
                HibernateSessionFactory.getSession().flush();
                data.remove(deuda);
                dialogStage.close();
                deudasEmpleadosController.empleadoEditado(empleado);
                
                // Registro para auditar
                String detalles = "elimino la deuda '" + deuda.getDetalles() + "' del empleado " 
                        + deuda.getUsuario().getNombre() + " " 
                        + deuda.getUsuario().getApellido();
                aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());
                    
                borradoCompleto();
            });
            buttonNo.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
            dialogStage.showAndWait();
        } else {
            borradoFallido();
        }
    }
    
    public void cambiarCuotasDeuda(Deuda deuda) {
        if (Objects.equals(deuda.getMonto(), 0d)) {
            
        } else {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Modificar Cuotas");
            String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new Button("Modificar");
            TextField fieldCuotas = new TextField();
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
            children(new Text("Ingrese la cantidad de cuotas:"), fieldCuotas, buttonOk).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            fieldCuotas.setMaxWidth(50);
            fieldCuotas.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
            fieldCuotas.setText(deuda.getCuotas().toString());
            buttonOk.setOnAction((ActionEvent e) -> {
                
                if (fieldCuotas.getText() != null) {
                    deuda.setCuotas(Integer.parseInt(fieldCuotas.getText()));
                    deuda.setUltimaModificacion(new Timestamp(new Date().getTime()));
                    HibernateSessionFactory.getSession().flush();
                    data.set(data.indexOf(deuda), deuda);
                    dialogStage.close();
                    deudasEmpleadosController.empleadoEditado(empleado);
                        
                    // Registro para auditar
                    String detalles = "edito las cuotas de la deuda '" + deuda.getDetalles() + "' del empleado " 
                            + deuda.getUsuario().getNombre() + " " 
                            + deuda.getUsuario().getApellido();
                    aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
                    
                    edicionCompletada();
                }
            });
            dialogStage.showAndWait();
        }
    }
    
    public void edicionCompletada() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Completado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Deuda editada satisfactoriamente."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
    }
    
    public void borradoFallido() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Error");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Esta deuda ya no se puede eliminar."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
    }
    
    public void imprimir(File file, Boolean enviarCorreo) {
        
        dialogWait();
        
        ReporteDeudas datasource = new ReporteDeudas();
        datasource.addAll((List<Deuda>) deudasTableView.getItems());
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_DEUDAS_EMPLEADO);
        
            Map<String, String> parametros = new HashMap();
            parametros.put("empleado", empleado.getNombre() + " " + empleado.getApellido());
            parametros.put("cedula", empleado.getCedula());
            parametros.put("cargo", empleado.getDetallesEmpleado().getCargo().getNombre());
            parametros.put("empresa", empresa.getNombre());
            parametros.put("siglas", empresa.getSiglas());
            parametros.put("correo", "Correo: " + empresa.getEmail());
            parametros.put("detalles", 
                         "Ruc: " + empresa.getNumeracion() 
                    + " - Direccion: " + empresa.getDireccion() 
                    + " - Tel: " + empresa.getTelefono1());
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "deudas_" + empleado.getNombre();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            if (enviarCorreo) {
                File pdf = File.createTempFile(filename, ".pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));  
                CorreoUtil.mandarCorreo(empleado.getDetallesEmpleado().getEmpresa().getNombre(), 
                        empleado.getEmail(), Const.ASUNTO_DEUDAS, 
                        "Reportes de deudas", 
                        pdf.getPath(), filename + ".pdf");
            }
            
            // Registro para auditar
            String detalles = "genero el recibo de deudas del empleado "
                    + empleado.getNombre() + " " + empleado.getApellido();
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
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Imprimir Deudas");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        CheckBox enviarCorreo = new CheckBox("Enviar documento al empleado");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("¿Que desea hacer?"), 
                buttonSiDocumento, buttonNoDocumento, enviarCorreo).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonSiDocumento.setOnAction((ActionEvent e) -> {
            File file = seleccionarDirectorio();
            if (file != null) {
                dialogStage.close();
                imprimir(file, enviarCorreo.isSelected());
            }
        });
        buttonNoDocumento.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            if (enviarCorreo.isSelected()) {
                imprimir(null, enviarCorreo.isSelected());
            } 
        });
        enviarCorreo.setSelected(true);
        dialogStage.showAndWait();
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
    
    public void borradoCompleto() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Completado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Deuda eliminada con exito."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
    }
    
    public void setEmpleado(Usuario empleado) {
        this.empleado = empleado;
        this.empresa = empleado.getDetallesEmpleado().getEmpresa();
        DeudaDAO deudaDao = new DeudaDAO();
        deudas = new ArrayList<>();
        deudas.addAll(deudaDao.findAllByEmpleadoId(empleado.getId()));
        data = FXCollections.observableArrayList(); 
        data.addAll(deudas);
        deudasTableView.setItems(data);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        fechaColumna.setCellValueFactory(new PropertyValueFactory<>("creacion"));
        detallesColumna.setCellValueFactory(new PropertyValueFactory<>("detalles"));
        montoColumna.setCellValueFactory(new PropertyValueFactory<>("monto"));
        restanteColumna.setCellValueFactory(new PropertyValueFactory<>("restante"));
        cuotasColumna.setCellValueFactory(new PropertyValueFactory<>("cuotas"));
        tipoColumna.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        borrarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        borrarColumna.setCellFactory(param -> new TableCell<Deuda, Deuda>() {
            private final Button borrarButton = new Button("Borrar");

            @Override
            protected void updateItem(Deuda deuda, boolean empty) {
                super.updateItem(deuda, empty);

                if (deuda == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(borrarButton);
                borrarButton.setOnAction(event -> {
                    deleteDeuda(deuda);
                });
            }
        });
        
        deudasTableView.setEditable(Boolean.FALSE);
        
        deudasTableView.setRowFactory( (Object tv) -> {
            TableRow<Deuda> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Deuda rowData = row.getItem();
                    cambiarCuotasDeuda(rowData);
                }
            });
            return row ;
        });
        buttonAgregar.setTooltip(
            new Tooltip("Agregar deuda")
        );
        buttonAgregar.setOnMouseEntered((MouseEvent t) -> {
            buttonAgregar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/agregar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonAgregar.setOnMouseExited((MouseEvent t) -> {
            buttonAgregar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/agregar.png'); "
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
    } 
    
    public static EventHandler<KeyEvent> numDecimalFilter() {

        EventHandler<KeyEvent> aux = (KeyEvent keyEvent) -> {
            if (!"0123456789.".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
                
            }
        };
        return aux;
    }
    
    public static EventHandler<KeyEvent> numFilter() {

        EventHandler<KeyEvent> aux = (KeyEvent keyEvent) -> {
            if (!"0123456789".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
                
            }
        };
        return aux;
    }
}
