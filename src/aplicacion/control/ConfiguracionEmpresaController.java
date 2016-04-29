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
import hibernate.model.Empresa;
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
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class ConfiguracionEmpresaController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private Label valorSeguro;
    
    @FXML
    private Label valorUniforme;
    
    private Empresa empresa;
    
    Seguro seguro;
    Uniforme uniforme;
    
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        setSeguro(new SeguroDAO().findByEmpresaId(empresa.getId()));
        setUniforme(new UniformeDAO().findByEmpresaId(empresa.getId()));
        
    }
    
    public void setSeguro(Seguro seguro) {
        this.seguro = seguro;
        if (seguro != null) {
            valorSeguro.setText(seguro.getValor().toString() + "$");
        } else {
            valorSeguro.setText("0.0$");
        }
    }
    
    public void setUniforme(Uniforme uniforme) {
        this.uniforme = uniforme;
        if (uniforme != null) {
            valorUniforme.setText(uniforme.getValor().toString() + "$");
        } else {
            valorUniforme.setText("0.0$");
        }
    }
    
    @FXML
    private void goHome(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarInEmpresa(empresa);
    }
     
    @FXML
    private void cambiarSeguro(ActionEvent event) {
        if (aplicacionControl.permisos == null) {
            aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.A_EMPRESAS, Permisos.Nivel.EDITAR)) {
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
                    newSeguro.setEmpresa(empresa);
                    newSeguro.setNombre("Seguro");
                    newSeguro.setValor(newSeguroValue);
                    newSeguro.setFecha(new Timestamp(new Date().getTime()));
                    new SeguroDAO().save(newSeguro);
                    setEmpresa(empresa);
                    dialogStage.close();
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
            if (aplicacionControl.permisos.getPermiso(Permisos.A_EMPRESAS, Permisos.Nivel.EDITAR)) {
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
                    newUniforme.setEmpresa(empresa);
                    newUniforme.setNombre("Uniforme");
                    newUniforme.setValor(newUniformeValue);
                    newUniforme.setFecha(new Timestamp(new Date().getTime()));
                    new UniformeDAO().save(newUniforme);
                    setEmpresa(empresa);
                    dialogStage.close();
                }); 
            } else {
                aplicacionControl.noPermitido();
            }
        } 
    }   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
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
