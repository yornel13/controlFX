/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Permisos;
import aplicacion.control.util.Roboto;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ClienteDAO;
import hibernate.dao.ControlEmpleadoDAO;
import hibernate.model.Cliente;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class ClientesController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private Button buttonAgregar;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private TableColumn numeracionColumna;
    
    @FXML
    private TableColumn nombreColumna;
    
    @FXML
    private TableColumn direccionColumna;
    
    @FXML
    private TableColumn telefonoColumna;
    
    @FXML
    private TableColumn detallesColumna;
    
    @FXML
    private TableColumn<Cliente, Cliente> pagosColumna;
    
    @FXML
    private TableColumn<Cliente, Cliente> borrarColumna;
    
    @FXML
    private Button buttonAdministrativo;
    
    @FXML
    private TableView clientesTableView;
    
    private ObservableList<Cliente> data;
    
    ArrayList<Cliente> clientes;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void goHome(ActionEvent event) {
        aplicacionControl.mostrarConfiguracion();
        stagePrincipal.close();
    }
    
    @FXML
    private void agregarCliente(ActionEvent event) {
        aplicacionControl.mostrarRegistrarCliente();
    }
    
    @FXML
    private void returnConfiguracion(ActionEvent event) {
        aplicacionControl.mostrarConfiguracion();
        stagePrincipal.close();
    } 
    
    @FXML
    private void mostrarEmpleadosSinCliente(ActionEvent event) {
        aplicacionControl.mostrarClienteVariables(null, stagePrincipal);
    }
    
    public void deleteCliente(Cliente cliente) {
        
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.CLIENTES, Permisos.Nivel.ELIMINAR)) {
                
                if (new ControlEmpleadoDAO().findAllByClienteId(cliente.getId()).isEmpty()) { 
                    
                    Stage dialogStage = new Stage();
                    dialogStage.initModality(Modality.APPLICATION_MODAL);
                    dialogStage.setResizable(false);
                    dialogStage.setTitle("Confirmación de borrado");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
                    dialogStage.getIcons().add(new Image(stageIcon));;
                    Button buttonConfirmar = new Button("Si Borrar");
                    Button buttonCancelar = new Button("No, no estoy seguro");
                    dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                    children(new Text("¿Borrar el cliente " + cliente.getNombre()+ "?"), 
                            buttonConfirmar, buttonCancelar).
                    alignment(Pos.CENTER).padding(new Insets(25)).build()));
                    buttonConfirmar.setOnAction((ActionEvent e) -> {

                        new ClienteDAO().delete(new ClienteDAO().findById(cliente.getId()));
                        HibernateSessionFactory.getSession().flush();
                        data.remove(cliente);
                        dialogStage.close();

                        // Registro para auditar
                        String detalles = "elimino el cliente " 
                                + cliente.getNombre();
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
    
    public void clienteEditado(Cliente cliente) {
        int posicion = 0;
        for (Cliente cli: data) {
            if (Objects.equals(cli.getId(), cliente.getId())) {
               posicion = data.indexOf(cli);
               break;
            }
        }
        data.set(posicion, cliente);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        clientesTableView.setEditable(Boolean.FALSE);
        
        ClienteDAO clientesDAO = new ClienteDAO();
        clientes = new ArrayList<>();
        clientes.addAll(clientesDAO.findAllActivo());
        
        data = FXCollections.observableArrayList(); 
        data.addAll(clientes);
        clientesTableView.setItems(data);
        
        numeracionColumna.setCellValueFactory(new PropertyValueFactory<>("ruc"));
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        telefonoColumna.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        
        direccionColumna.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        
        detallesColumna.setCellValueFactory(new PropertyValueFactory<>("detalles"));
        
        pagosColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        pagosColumna.setCellFactory(param -> new TableCell<Cliente, Cliente>() {
            private final Button pagarButton = new Button("Editar");

            @Override
            protected void updateItem(Cliente cliente, boolean empty) {
                super.updateItem(cliente, empty);

                if (cliente == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(pagarButton);
                pagarButton.setOnAction(event -> {
                    aplicacionControl.mostrarClienteVariables(cliente, stagePrincipal);
                });
            }
        });
        borrarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        borrarColumna.setCellFactory(param -> new TableCell<Cliente, Cliente>() {
            private final Button deleteButton = new Button("Borrar");

            @Override
            protected void updateItem(Cliente cliente, boolean empty) {
                super.updateItem(cliente, empty);

                if (cliente == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    deleteCliente(cliente);
                });
            }
        });
       
        clientesTableView.setRowFactory( (Object tv) -> {
            TableRow<Cliente> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Cliente rowData = row.getItem();
                    aplicacionControl.mostrarCliente(clientesDAO
                            .findById(rowData.getId()));
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
        
        {
            Image imageGuardia = new Image(getClass()
                    .getResourceAsStream("imagenes/bt_administrativo.png"));
            ImageView imageView = new ImageView(imageGuardia);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            buttonAdministrativo.setGraphic(imageView);
            buttonAdministrativo.setFont(Roboto.MEDIUM(15));
            buttonAdministrativo.setOnMouseEntered((MouseEvent t) -> {
                buttonAdministrativo.setStyle("-fx-background-color: #E0E0E0;");
            });
            buttonAdministrativo.setOnMouseExited((MouseEvent t) -> {
                buttonAdministrativo.setStyle("-fx-background-color: #039BE5;");
            });
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
