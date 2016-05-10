/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.DeudaDAO;
import hibernate.model.Deuda;
import hibernate.model.Empresa;
import hibernate.model.Usuario;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private TableColumn<Deuda, Deuda> borrarColumna;
    
    @FXML
    private TableView deudasTableView;
    
    private ObservableList<Deuda> data;
    
    ArrayList<Deuda> deudas;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void agregarDeuda(ActionEvent event) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.A_EMPLEADOS, Permisos.Nivel.CREAR)) {
               
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
            if (aplicacionControl.permisos.getPermiso(Permisos.A_ROL_DE_PAGO, Permisos.Nivel.EDITAR)) {
               
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("Nueva Deuda");
                String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));
                Button buttonConfirmar = new Button("Crear");
                TextField fieldDetalles = new TextField();
                TextField fieldMonto = new TextField();
                TextField fieldCuotas = new TextField();
                Text textDetalles = new Text("Detalles");
                Text textMonto = new Text("Monto");
                Text textCuotas = new Text("Cuotas");
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(textDetalles, fieldDetalles, textMonto, fieldMonto, 
                        textCuotas, fieldCuotas, buttonConfirmar).
                alignment(Pos.CENTER).padding(new Insets(25)).build()));
                dialogStage.show();
                fieldMonto.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
                fieldCuotas.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
                buttonConfirmar.setOnAction((ActionEvent e) -> {
                    
                    String detalles = fieldDetalles.getText();
                    String monto = fieldMonto.getText();
                    String cuotas = fieldCuotas.getText();
                    
                    if (detalles == null) {
                        
                    } else if (monto == null) {
                        
                    } else if (cuotas == null) {
                        
                    } else {
                        
                        Deuda newDeuda = new Deuda();
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
                data.remove(deuda);
                dialogStage.close();
                
                // Registro para auditar
                String detalles = "elimino la deuda '" + deuda.getDetalles() + "' del empleado " 
                        + deuda.getUsuario().getNombre() + " " 
                        + deuda.getUsuario().getApellido();
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
                    deuda.setCuotas(Integer.parseInt(fieldCuotas.getText()));
                    deuda.setUltimaModificacion(new Timestamp(new Date().getTime()));
                    HibernateSessionFactory.getSession().flush();
                    data.set(data.indexOf(deuda), deuda);
                    dialogStage.close();
                        
                    // Registro para auditar
                    String detalles = "edito las cuotas de la deuda '" + deuda.getDetalles() + "' del empleado " 
                            + deuda.getUsuario().getNombre() + " " 
                            + deuda.getUsuario().getApellido();
                    aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
                    
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
    
    public void setEmpleado(Usuario empleado) {
        this.empleado = empleado;
        DeudaDAO deudaDao = new DeudaDAO();
        deudas = new ArrayList<>();
        deudas.addAll(deudaDao.findAllByUsuarioId(empleado.getId()));
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
