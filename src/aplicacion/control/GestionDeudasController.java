/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.CuotaDeudaTable;
import aplicacion.control.tableModel.DeudaTable;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
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
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class GestionDeudasController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private PagoMensualDetallesController pagoMensualDetallesController;
    
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
    private TableColumn<CuotaDeudaTable, CuotaDeudaTable> borrarColumna;
    
    @FXML
    private TableView deudasTableView;
    
    private ObservableList<CuotaDeudaTable> data;
    
    ArrayList<CuotaDeuda> cuotas;
    
    @FXML
    private Button buttonAgregar;
    
    @FXML
    private Button buttonListo;
    
    private Fecha fin;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    public void listo(ActionEvent event) {
        stagePrincipal.close();
    }
    
    public void setPagoTotalController(PagoMensualDetallesController pagoMensualDetallesController) {
        this.pagoMensualDetallesController = pagoMensualDetallesController;
    }
    
    @FXML
    private void agregarDeuda(ActionEvent event) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.CREAR)) {
               
               stagePrincipal.close();
               pagoMensualDetallesController.abrirDeudasEmpleado();
                  
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    private void guardar() {
        pagoMensualDetallesController.setInfoEditada(pagoMensualDetallesController.inicio, 
                pagoMensualDetallesController.fin, 
                pagoMensualDetallesController.empleado.getId());
    }
    
    
    public void nuevaDeuda() {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.EDITAR)) {
               
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
                dialogStage.show();
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
                        new UsuarioDAO().getSession().refresh(newDeuda.getUsuario());
                        dialogStage.close();
                        
                        String detalle = "agrego una deudo al empleado " 
                            + empleado.getApellido()+ " " 
                                + empleado.getNombre()
                                + " por " + monto + "$";
                        aplicacionControl.au.saveAgrego(detalle, 
                                aplicacionControl.permisos.getUsuario());
                        
                        setEmpleado(empleado, fin);
                        
                        guardar();
                        
                        generacionCompletada();
                    } 
                });  
                  
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
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
            children(new Text("¿Seguro que desea eliminar esta deuda?"), buttonOk).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            dialogStage.show();
            buttonOk.setOnAction((ActionEvent e) -> {
                new DeudaDAO().delete(deuda);
                HibernateSessionFactory.getSession().flush();
                new UsuarioDAO().getSession().refresh(deuda.getUsuario());
                data.remove(deuda);
                dialogStage.close();
                
                // Registro para auditar
                String detalles = "elimino la deuda '" + deuda.getDetalles() 
                        + "' del empleado " 
                        + deuda.getUsuario().getApellido()+ " " 
                        + deuda.getUsuario().getNombre();
                aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());
                    
                borradoCompleto();
            });
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
            dialogStage.show();
            fieldCuotas.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
            fieldCuotas.setText(deuda.getCuotas().toString());
            buttonOk.setOnAction((ActionEvent e) -> {
                
                if (fieldCuotas.getText() != null) {
                    /*deuda.setCuotas(Integer.parseInt(fieldCuotas.getText()));
                    deuda.setUltimaModificacion(new Timestamp(new Date().getTime()));
                    HibernateSessionFactory.getSession().flush();
                    new UsuarioDAO().getSession().refresh(deuda.getUsuario());
                    data.set(data.indexOf(deuda), deuda);
                    dialogStage.close();*/
                        
                    // Registro para auditar
                    String detalles = "edito las cuotas de la deuda '" + deuda.getDetalles() + "' del empleado " 
                            + deuda.getUsuario().getApellido()+ " " 
                            + deuda.getUsuario().getNombre();
                    aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
                    
                    guardar();
                    
                    edicionCompletada();
                }
            });
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
        children(new Text("Deuda editada satisfactoriamente"), buttonOk).
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
    
    public void setEmpleado(Usuario empleado, Fecha fin) {
        this.empleado = empleado;
        this.fin = fin;
        CuotaDeudaDAO cuotaDeudaDAO = new CuotaDeudaDAO();
        cuotas = new ArrayList<>();
        cuotas.addAll(cuotaDeudaDAO.findAllByFechaAndEmpleadoId(empleado.getId(), fin.getFecha()));
        data = FXCollections.observableArrayList(); 
        for (CuotaDeuda cuota: cuotas) {
            CuotaDeudaTable cuotaTable = new CuotaDeudaTable();
            cuotaTable.setId(cuota.getId());
            cuotaTable.setMonto(cuota.getMonto());
            cuotaTable.setFechaString(Fechas.getFechaConMes(cuota.getDeuda().getCreacion()));
            cuotaTable.setFecha(new Fecha(cuota.getFecha()));
            cuotaTable.setTipo(cuota.getDeuda().getTipo());
            cuotaTable.setDetalles(cuota.getDetalles());
            cuotaTable.setCuotaDeuda(cuota);
            cuotaTable.setCuotas(cuota.getDeuda().getCuotasTotal());
            data.addAll(cuotaTable);
        }
        deudasTableView.setItems(data);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        fechaColumna.setCellValueFactory(new PropertyValueFactory<>("fechaString"));
        tipoColumna.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        montoColumna.setCellValueFactory(new PropertyValueFactory<>("monto"));
        detallesColumna.setCellValueFactory(new PropertyValueFactory<>("detalles"));
        cuotasColumna.setCellValueFactory(new PropertyValueFactory<>("cuotas"));
        borrarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        borrarColumna.setCellFactory(param -> new TableCell<CuotaDeudaTable, CuotaDeudaTable>() {
            private final CheckBox checkBoxDeuda = new CheckBox();

            @Override
            protected void updateItem(CuotaDeudaTable deuda, boolean empty) {
                super.updateItem(deuda, empty);

                if (deuda == null) {
                    setGraphic(null);
                    return;
                }
                
                setGraphic(checkBoxDeuda);
                if (checkBoxDeuda != null)
                    checkBoxDeuda.setSelected(false);
                checkBoxDeuda.setOnAction(event -> {
                     /*deuda.setAplazar(checkBoxDeuda.isSelected());
                     HibernateSessionFactory.getSession().flush();
                     guardar();*/
                });
            }

            
        });
        
        deudasTableView.setEditable(Boolean.FALSE);
        
        deudasTableView.setRowFactory( (Object tv) -> {
            TableRow<Deuda> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    /*Deuda rowData = row.getItem();
                    cambiarCuotasDeuda(rowData);*/
                }
            });
            return row ;
        });
        
        buttonAgregar.setTooltip(
            new Tooltip("Ajustar deudas")
        );
        buttonAgregar.setOnMouseEntered((MouseEvent t) -> {
            buttonAgregar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/configurar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonAgregar.setOnMouseExited((MouseEvent t) -> {
            buttonAgregar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/configurar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        buttonListo.setTooltip(
            new Tooltip("Hecho")
        );
        buttonListo.setOnMouseEntered((MouseEvent t) -> {
            buttonListo.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/icon_listo.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonListo.setOnMouseExited((MouseEvent t) -> {
            buttonListo.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/icon_listo.png'); "
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
