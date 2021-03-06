/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.FilterMaxValue;
import hibernate.HibernateSessionFactory;
import hibernate.dao.EmpresaDAO;
import hibernate.model.Empresa;
import java.io.IOException;
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
public class EditarEmpresaController implements Initializable {
    
    private AplicacionControl aplicacionControl;
    
    private Stage stagePrincipal;
    
     private Empresa empresa;
    
    @FXML
    private TextField nombreField;
    
    @FXML
    private TextField siglasField;
    
    @FXML
    private TextField numeracionField;
    
    @FXML
    private TextField telefono1Field;
    
    @FXML
    private TextField telefono2Field;
    
    @FXML
    private TextField webField;
    
    @FXML
    private TextField faxField;
    
    @FXML
    private TextField direccionField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField diaCorteField;
    
    @FXML
    private Label errorText;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        nombreField.setText(empresa.getNombre());
        siglasField.setText(empresa.getSiglas());
        numeracionField.setText(empresa.getNumeracion());
        telefono1Field.setText(empresa.getTelefono1());
        telefono2Field.setText(empresa.getTelefono2());
        webField.setText(empresa.getWeb());
        faxField.setText(empresa.getFax());
        direccionField.setText(empresa.getDireccion());
        emailField.setText(empresa.getEmail());
        diaCorteField.setText(empresa.getComienzoMes().toString());
    }
   
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
    }
    
    @FXML
    private void onSelectMenuButtonItem(ActionEvent event) {
        System.out.println("You clicked me menu!");
        //label.setText(menuButton.getCursor().toString());
    }
    
    @FXML
    private void onCLickGuardar(ActionEvent event) throws IOException, Exception {
        if (nombreField.getText().isEmpty()) {
            errorText.setText("Debe ingresar el nombre de la Emprea");
        } else if (siglasField.getText().isEmpty()) {
            errorText.setText("Debe ingresar las siglas de la Empresa");
        } else if (numeracionField.getText().isEmpty()) {
            errorText.setText("Ingrese el RUC");
        } else if (telefono1Field.getText().isEmpty()) {
            errorText.setText("Debe ingresar al menos un telefono");
        } else if (direccionField.getText().isEmpty()) {
            errorText.setText("Debe ingresar la direccion de la Empresa");
        } else if (emailField.getText().isEmpty()) {
            errorText.setText("Debe ingresar el email de la Empresa");
        } else if (diaCorteField.getText().isEmpty()) {
            errorText.setText("Debe ingresar el dia de corte para los pagos");
        } else {
            
            empresa = new EmpresaDAO().findById(empresa.getId());
            
            empresa.setNombre(nombreField.getText());
            empresa.setSiglas(siglasField.getText());
            empresa.setNumeracion(numeracionField.getText());
            empresa.setTelefono1(telefono1Field.getText());
            empresa.setTelefono2(telefono2Field.getText());
            empresa.setDireccion(direccionField.getText());
            empresa.setFax(faxField.getText());
            empresa.setWeb(webField.getText());
            empresa.setEmail(emailField.getText());
            empresa.setUltimaModificacion(new Timestamp(new Date().getTime()));
            empresa.setComienzoMes(Integer.parseInt(diaCorteField.getText()));
            
            confirmarGuardado();
        }
    }
    
    public void confirmacionPositiva() {
        stagePrincipal.close();

        // Registro para auditar
        String detalles = "edito la empresa " 
                + empresa.getNombre();
        aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());

        completado();

        EmpresasController ec = aplicacionControl.changeEmpresasController;
        if (ec != null) {
            ec.empresaEditada(empresa);
        }
    }
    
    public void completado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Completado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Empresa editada satisfactoriamente."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
    }
    
    public void confirmarGuardado() {
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Confirmar modificación");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_editar.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("Si, modificar");
        Button buttonCancelar = new Button("No, no estoy seguro");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("¿Seguro que desea modificar esta empresa?"), buttonOk, buttonCancelar).
        alignment(Pos.CENTER).padding(new Insets(25)).build()));
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            HibernateSessionFactory.getSession().flush();
            confirmacionPositiva();
        });
        buttonOk.setOnKeyPressed((KeyEvent event) -> {
            dialogStage.close();
            HibernateSessionFactory.getSession().flush();
            confirmacionPositiva();
        });
        buttonCancelar.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            HibernateSessionFactory.getSession().clear();
        });
        dialogStage.showAndWait();
        
    }
    
    @FXML
    public void onClickCancelar(ActionEvent event) {
        stagePrincipal.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        diaCorteField.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
        diaCorteField.addEventFilter(KeyEvent.KEY_TYPED, new FilterMaxValue(30));
        
        numeracionField.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
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
