/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Const;
import hibernate.dao.EmpresaDAO;
import hibernate.model.Empresa;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Yornel
 */
public class PrincipalController implements Initializable {
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private Label label;
    
    @FXML
    private ChoiceBox selector;
    
    @FXML
    private Button configuracion;
    
    ArrayList<Empresa> empresas;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (!empresas.isEmpty() && !selector.getSelectionModel().isEmpty()) {
            aplicacionControl.mostrarInEmpresa(empresas.get(selector.getSelectionModel().getSelectedIndex()));
        }  else {
            label.setText("Selecciona una empresa primero.");
        }
    }
    
    @FXML
    private void onSelectMenuButtonItem(ActionEvent event) {
        System.out.println("You clicked me menu!");
        //label.setText(menuButton.getCursor().toString());
    }
    
    @FXML
    private void onCLickConfiguration(ActionEvent event) throws IOException, Exception {
        aplicacionControl.mostrarLogin();
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        EmpresaDAO empresaDao = new EmpresaDAO();
        empresas = new ArrayList<>();
        empresas.addAll(empresaDao.findAll());
        
        String[] items = new String[empresas.size()];
        
        for (Empresa emp: empresas) {
            items[empresas.indexOf(emp)] = emp.getNombre();
        }
        
        selector.setItems(FXCollections.observableArrayList(items)); 
        
        String image = AplicacionControl.class.getResource("imagenes/config_admin.png").toExternalForm();
        
        Image adminImage = new Image(image);
        configuracion.setGraphic(new ImageView(adminImage));
        configuracion.setStyle(Const.BACKGROUND_COLOR_SEMI_TRANSPARENT);
        
    }    
}
