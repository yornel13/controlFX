/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.EmpresaTable;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.EmpresaDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Cliente;
import hibernate.model.Empresa;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class EmpresasController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private Empresa empresa;
   
    @FXML
    private Button agregarButton;
    
    @FXML
    private Pane imagenLabel;
    
    @FXML
    private Button homeButton;
    
     @FXML
    private TableColumn numeracionColumna;
    
    @FXML
    private TableColumn siglasColumna;
    
    @FXML
    private TableColumn nombreColumna;
    
    @FXML
    private TableColumn tipoColumna;
    
    @FXML
    private TableColumn creadoColumna;
    
    @FXML
    private TableColumn<EmpresaTable, EmpresaTable> borrarColumna;
    
    @FXML
    private TableView empresasTableView;
    
    @FXML
    private Button buttonAgregar;
    
    @FXML
    private Button buttonAtras;
    
    private ObservableList<EmpresaTable> data;
    
    ArrayList<Empresa> empresas;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void returnConfiguracion(ActionEvent event) {
        aplicacionControl.mostrarConfiguracion();
        stagePrincipal.close();
    }
    
    @FXML
    private void agregarEmpresa(ActionEvent event) {
        aplicacionControl.mostrarRegistrarEmpresa();
    }
    
    public void deleteEmpresa(EmpresaTable empresaTable) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.EMPRESAS, Permisos.Nivel.ELIMINAR)) {
               
                if (new UsuarioDAO().findByEmpresaId(empresaTable.getId()).isEmpty()) { 
                
                    Stage dialogStage = new Stage();
                    dialogStage.initModality(Modality.APPLICATION_MODAL);
                    dialogStage.setResizable(false);
                    dialogStage.setTitle("Confirmación de borrado");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
                    dialogStage.getIcons().add(new Image(stageIcon));;
                    Button buttonConfirmar = new Button("Si Borrar");
                    Button buttonCancelar = new Button("No, no estoy seguro");
                    dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                    children(new Text("¿Esta seguro que desea borrar la empresa " + empresaTable.getNombre()+ "?"), 
                            buttonConfirmar, buttonCancelar).
                    alignment(Pos.CENTER).padding(new Insets(25)).build()));
                    buttonConfirmar.setOnAction((ActionEvent e) -> {

                        new EmpresaDAO().delete(new EmpresaDAO().findById(empresaTable.getId()));
                        HibernateSessionFactory.getSession().flush();
                        data.remove(empresaTable);
                        dialogStage.close();

                        // Registro para auditar
                        String detalles = "elimino la empresa " 
                                + empresaTable.getNombre();
                        aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());
                    });
                    buttonCancelar.setOnAction((ActionEvent e) -> {
                       dialogStage.close();
                    });
                    dialogStage.showAndWait();
                
                } else {
                    aplicacionControl.noSePuede();
                }

                  
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    public void empresaEditada(Empresa emp) {
        for (EmpresaTable empresaTable: data) {
            if(Objects.equals(empresaTable.getId(), emp.getId())) {
                EmpresaTable empresa = new EmpresaTable();
                empresa.id.set(emp.getId());
                empresa.nombre.set(emp.getNombre());
                empresa.siglas.set(emp.getSiglas());
                empresa.numeracion.set(emp.getNumeracion());
                empresa.diaCortePago.set(emp.getComienzoMes());
                empresa.creacion.set(emp.getCreacion().toString());
                empresa.tipo.set(emp.getTipo());
                data.set(data.indexOf(empresaTable), empresa);
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        empresasTableView.setEditable(Boolean.FALSE);
        
        EmpresaDAO empresaDAO = new EmpresaDAO();
        empresas = new ArrayList<>();
        empresas.addAll(empresaDAO.findAll());
        
        if (!empresas.isEmpty()) {
           data = FXCollections.observableArrayList(); 
           for (Empresa emp: empresas) {
               EmpresaTable empresa = new EmpresaTable();
               empresa.id.set(emp.getId());
               empresa.nombre.set(emp.getNombre());
               empresa.siglas.set(emp.getSiglas());
               empresa.numeracion.set(emp.getNumeracion());
               if (emp.getComienzoMes() == 1)
                    empresa.diaCortePago.set(30);
               else 
                    empresa.diaCortePago.set(emp.getComienzoMes() - 1);
                   
               empresa.creacion.set(emp.getCreacion().toString());
               empresa.tipo.set(emp.getTipo());
               data.add(empresa);
           }
           empresasTableView.setItems(data);
        }
        
        numeracionColumna.setCellValueFactory(new PropertyValueFactory<>("numeracion"));
        
        siglasColumna.setCellValueFactory(new PropertyValueFactory<>("siglas"));
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        tipoColumna.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        
        creadoColumna.setCellValueFactory(new PropertyValueFactory<>("creacion"));
        
        borrarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        borrarColumna.setCellFactory(param -> new TableCell<EmpresaTable, EmpresaTable>() {
            private final Button deleteButton = new Button("Borrar");

            @Override
            protected void updateItem(EmpresaTable empresaTable, boolean empty) {
                super.updateItem(empresaTable, empty);

                if (empresaTable == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    deleteEmpresa(empresaTable);
                });
            }
        });
        
        empresasTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpresaTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpresaTable rowData = row.getItem();
                    aplicacionControl.mostrarEmpresa(empresaDAO.findById(rowData.getId()));
                }
            });
            return row ;
        });
        
         buttonAgregar.setTooltip(
            new Tooltip("Agregar nuevo cliente")
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
