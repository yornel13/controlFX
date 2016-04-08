/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.dao.EmpresaDAO;
import hibernate.model.Empresa;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Yornel
 */
public class VentanaPrincipalController implements Initializable {
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private Label label;
    
    @FXML
    private MenuButton menuButton;
    
    @FXML
    private ToggleButton configuracion;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        for(MenuItem item : menuButton.getItems()) {
                    CheckMenuItem checkMenuItem = (CheckMenuItem) item;
                    if(checkMenuItem.isSelected()) {
                        label.setText("Selected item :" + checkMenuItem.getText());
                    }
                }
    }
    
    @FXML
    private void onSelectMenuButtonItem(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText(menuButton.getCursor().toString());
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
        menuButton.getItems().clear();
        menuButton.getItems().add(new CheckMenuItem("Registrar empresa"));
        for (Empresa emp: empresas) {
           menuButton.getItems().add(new CheckMenuItem(emp.getNombre()));
        }
        
        menuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println(".handle()");
                for(MenuItem item : menuButton.getItems()) {
                    CheckMenuItem checkMenuItem = (CheckMenuItem) item;
                    if(checkMenuItem.isSelected()) {
                        System.out.println("Selected item :" + checkMenuItem.getText());
                    }
                }
            }
        });
        
        File file = new File("imagenes/settings.png");
        Image image = new Image(file.toURI().toString());
        
       
        
        
        
        
        
    }    
    
}
