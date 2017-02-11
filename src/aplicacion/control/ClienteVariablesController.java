/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.SeguroDAO;
import hibernate.dao.UniformeDAO;
import hibernate.model.Cliente;
import hibernate.model.Constante;
import hibernate.model.Seguro;
import hibernate.model.Uniforme;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class ClienteVariablesController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private Label valorSeguro;
    
    @FXML
    private Label valorUniforme;
    
    @FXML
    private Label valorDecimoCuarto;
    
    @FXML
    private Label valorIess;
    
    @FXML
    private Button buttonAtras;
    
    private Cliente cliente;
    
    Seguro seguro;
    Uniforme uniforme;
    Constante decimoCuarto;
    Constante iess;
    
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        if (cliente != null) {
            setSeguro(new SeguroDAO().findByClienteId(cliente.getId()));
            setUniforme(new UniformeDAO().findByClienteId(cliente.getId()));
        } else {
            setSeguro(new SeguroDAO().findAdministrativo());
            setUniforme(new UniformeDAO().findAdministrativo());
        }
        
    }
    
    public void setSeguro(Seguro seguro) {
        this.seguro = seguro;
        if (seguro != null) {
            valorSeguro.setText("$" + this.seguro.getValor().toString());
        } else {
            valorSeguro.setText("$0.0");
        }
    }
    
    public void setUniforme(Uniforme uniforme) {
        this.uniforme = uniforme;
        if (uniforme != null) {
            valorUniforme.setText("$" + this.uniforme.getValor().toString());
        } else {
            valorUniforme.setText("$0.0");
        }
    }
    
    @FXML
    private void returnClientes(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarClientes(stagePrincipal);
    }
     
    @FXML
    private void cambiarSeguro(ActionEvent event) {
        if (aplicacionControl.permisos == null) {
            aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.EDITAR)) {
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("Seguro");
                String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));
                Button cambiarValorSeguro = new Button("Cambiar");
                TextField fieldSeguro = new TextField();
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(new Text("¿Escriba el nuevo valor?"), fieldSeguro, cambiarValorSeguro).
                alignment(Pos.CENTER).padding(new Insets(25)).build()));
                fieldSeguro.setPrefWidth(150);
                cambiarValorSeguro.setPrefWidth(100);
                fieldSeguro.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
                dialogStage.show();
                cambiarValorSeguro.setOnAction((ActionEvent e) -> {
                    Double newSeguroValue;
                    if (fieldSeguro.getText().isEmpty()) {
                        newSeguroValue = 0d;
                    } else {
                        newSeguroValue = Double.parseDouble(fieldSeguro.getText());
                    }
                    if (seguro != null) {
                        seguro.setActivo(false);
                        HibernateSessionFactory.getSession().flush();
                    }
                    Seguro newSeguro = new Seguro();
                    newSeguro.setActivo(Boolean.TRUE);
                    if (cliente != null) {
                        newSeguro.setCliente(cliente);
                        newSeguro.setNombre("Seguro");
                    } else {
                        newSeguro.setNombre("administrativo");
                    }
                    newSeguro.setValor(newSeguroValue);
                    newSeguro.setFecha(new Timestamp(new Date().getTime()));
                    new SeguroDAO().save(newSeguro);
                    setCliente(cliente);
                    dialogStage.close();
                    
                    // Registro para auditar
                    String detalles;
                    if (cliente != null) {
                        detalles = "edito el valor del seguro del cliente " 
                            + cliente.getNombre();
                    } else {
                        detalles = "edito el valor del seguro del personal "
                                + "administrativo";
                    }
                    aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
                }); 
            } else {
                aplicacionControl.noPermitido();
            }
        } 
    }
    
    @FXML
    private void cambiarUniforme(ActionEvent event) {
        if (aplicacionControl.permisos == null) {
            aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.EDITAR)) {
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("Uniforme");
                String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));
                Button cambiarValorUniforme = new Button("Cambiar");
                TextField fieldSeguro = new TextField();
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(new Text("¿Escriba el nuevo valor?"), fieldSeguro, cambiarValorUniforme).
                alignment(Pos.CENTER).padding(new Insets(25)).build()));
                fieldSeguro.setPrefWidth(150);
                cambiarValorUniforme.setPrefWidth(100);
                fieldSeguro.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
                dialogStage.show();
                cambiarValorUniforme.setOnAction((ActionEvent e) -> {
                    Double newUniformeValue;
                    if (fieldSeguro.getText().isEmpty()) {
                        newUniformeValue = 0d;
                    } else {
                        newUniformeValue = Double.parseDouble(fieldSeguro.getText());
                    }
                    if (uniforme != null) {
                        uniforme.setActivo(false);
                        HibernateSessionFactory.getSession().flush();
                    }
                    Uniforme newUniforme = new Uniforme();
                    newUniforme.setActivo(Boolean.TRUE);
                    if (cliente != null) {
                        newUniforme.setCliente(cliente);
                        newUniforme.setNombre("Uniforme");
                    } else {
                        newUniforme.setNombre("administrativo");
                    }
                    newUniforme.setValor(newUniformeValue);
                    newUniforme.setFecha(new Timestamp(new Date().getTime()));
                    new UniformeDAO().save(newUniforme);
                    setCliente(cliente);
                    dialogStage.close();
                    
                    // Registro para auditar
                    String detalles;
                    if (cliente != null) {
                        detalles = "edito el valor del uniforme del cliente " 
                            + cliente.getNombre();
                    } else {
                        detalles = "edito el valor del uniforme del personal "
                                + "administrativo";
                    }
                    aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
                }); 
            } else {
                aplicacionControl.noPermitido();
            }
        } 
    }   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    
    public static EventHandler<KeyEvent> numDecimalFilter() {

        EventHandler<KeyEvent> aux = (KeyEvent keyEvent) -> {
            if (!"0123456789.".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
                
            }
        };
        return aux;
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
