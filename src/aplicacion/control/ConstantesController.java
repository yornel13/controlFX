/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Const;
import aplicacion.control.util.Permisos;
import aplicacion.control.util.Roboto;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ConstanteDAO;
import hibernate.model.Constante;
import hibernate.model.Empresa;
import java.net.URL;
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
public class ConstantesController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private Label valorDecimoCuarto;
    
    @FXML
    private Label valorIess;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Label tituloLabel;
    
    private Empresa empresa;
    
    Constante decimoCuarto;
    Constante iess;
    
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void setDecimoCuarto(Constante decimoCuarto) {
        this.decimoCuarto = decimoCuarto;
        if (decimoCuarto != null) {
            valorDecimoCuarto.setText("$" + this.decimoCuarto.getValor());
        } else {
            valorDecimoCuarto.setText("$0.0");
        }
    }
    
    public void setIess(Constante iess) {
        this.iess = iess;
        if (iess != null) {
            valorIess.setText(this.iess.getValor() + "%");
        } else {
            valorIess.setText("0.0%");
        }
    }
    
    @FXML
    private void returnConfiguracion(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarConfiguracion();
    }
   
    @FXML
    private void cambiarDecimoCuarto(ActionEvent event) {
        if (aplicacionControl.permisos == null) {
            aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.EDITAR)) {
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("Decimo Cuarto");
                String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));
                Button cambiarValorDecimoCuarto = new Button("Cambiar");
                TextField fieldDecimoCuarto = new TextField();
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(new Text("¿Escriba el nuevo valor?"), fieldDecimoCuarto, cambiarValorDecimoCuarto).
                alignment(Pos.CENTER).padding(new Insets(25)).build()));
                fieldDecimoCuarto.setPrefWidth(150);
                cambiarValorDecimoCuarto.setPrefWidth(100);
                fieldDecimoCuarto.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
                dialogStage.show();
                cambiarValorDecimoCuarto.setOnAction((ActionEvent e) -> {
                    Double newValorDecimoCuarto;
                    if (fieldDecimoCuarto.getText().isEmpty()) {
                        newValorDecimoCuarto = 0d;
                    } else {
                        newValorDecimoCuarto = Double.parseDouble(fieldDecimoCuarto.getText());
                    }
                    if (decimoCuarto != null) {
                        decimoCuarto.setActivo(false);
                        HibernateSessionFactory.getSession().flush();
                    }
                    Constante newContante = new Constante();
                    newContante.setActivo(Boolean.TRUE);
                    newContante.setNombre(Const.DECIMO_CUARTO);
                    newContante.setValor(newValorDecimoCuarto.toString());
                    new ConstanteDAO().save(newContante);
                    setInfo();
                    dialogStage.close();
                    
                    // Registro para auditar
                    String detalles = "ajusto el valor del Decimo Cuarto a " + newValorDecimoCuarto;
                    aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
                }); 
            } else {
                aplicacionControl.noPermitido();
            }
        } 
    }
    
    @FXML
    private void cambiarIess(ActionEvent event) {
        if (aplicacionControl.permisos == null) {
            aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.EDITAR)) {
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("IESS");
                String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));
                Button cambiarValorDecimoCuarto = new Button("Cambiar");
                TextField fieldDecimoCuarto = new TextField();
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(new Text("¿Escriba el nuevo porcentaje?"), fieldDecimoCuarto, cambiarValorDecimoCuarto).
                alignment(Pos.CENTER).padding(new Insets(25)).build()));
                fieldDecimoCuarto.setPrefWidth(150);
                cambiarValorDecimoCuarto.setPrefWidth(100);
                fieldDecimoCuarto.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
                dialogStage.show();
                cambiarValorDecimoCuarto.setOnAction((ActionEvent e) -> {
                    Double newValorIess;
                    if (fieldDecimoCuarto.getText().isEmpty()) {
                        newValorIess = 0d;
                    } else {
                        newValorIess = Double.parseDouble(fieldDecimoCuarto.getText());
                    }
                    if (iess != null) {
                        iess.setActivo(false);
                        HibernateSessionFactory.getSession().flush();
                    }
                    Constante newContante = new Constante();
                    newContante.setActivo(Boolean.TRUE);
                    newContante.setNombre(Const.IESS);
                    newContante.setValor(newValorIess.toString());
                    new ConstanteDAO().save(newContante);
                    setInfo();
                    dialogStage.close();
                    
                    // Registro para auditar
                    String detalles = "ajusto el valor del iess a " + newValorIess + "%";
                    aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
                }); 
            } else {
                aplicacionControl.noPermitido();
            }
        } 
    }
    
    public void setInfo() {
       setDecimoCuarto(new ConstanteDAO().findUniqueResultByNombre(Const.DECIMO_CUARTO));
        setIess(new ConstanteDAO().findUniqueResultByNombre(Const.IESS));  
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        setInfo();
        
        tituloLabel.setFont(Roboto.MEDIUM(22));
        
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
