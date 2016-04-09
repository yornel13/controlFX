/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

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
    private ToggleButton configuracion;
    
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
    private void onCLickConfiguration(ActionEvent event) throws IOException, Exception {
        aplicacionControl.mostarLogin();
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        EmpresaDAO empresaDao = new EmpresaDAO();
        ArrayList<Empresa> empresas = new ArrayList<>();
        empresas.addAll(empresaDao.findAll());
        
        String[] items = new String[empresas.size()];
        
        for (Empresa emp: empresas) {
            items[empresas.indexOf(emp)] = emp.getNombre();
        }
        
        selector.setItems(FXCollections.observableArrayList(items));  
    }    
}
