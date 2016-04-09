/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class AplicacionControl extends Application {
    
    private Stage stagePrincipal;
    private AnchorPane rootPane;
    
    @Override
    public void start(Stage stagePrincipal) throws Exception {
        this.stagePrincipal = stagePrincipal;
        mostrarVentanaPrincipal();
    }
    
    /*
     * cargamos la ventana principal
     */
    
    
    public void cerrar() {
        
    }
    
    public void mostrarVentanaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaPrincipal.fxml"));
            rootPane = (AnchorPane) loader.load();
            String image = AplicacionControl.class.getResource("imagenes/guardia_fondo.jpg").toExternalForm();
            rootPane.setStyle("-fx-background-image: url('" + image + "'); " + "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
            Scene scene = new Scene(rootPane);
            stagePrincipal.setTitle("Principal");
            stagePrincipal.setScene(scene);
            PrincipalController controller = loader.getController();
            controller.setProgramaPrincipal(this);
            stagePrincipal.show();
        } catch (IOException e) {
            //tratar la excepción.
        }
   }
    
   public void mostarLogin() {
        try {
            System.out.println("aplicacion.control.AplicacionControl.mostarLogin()");
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaLogin.fxml"));
            AnchorPane ventanaLogin = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Login");
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaLogin);
            ventana.setScene(scene);
            LoginController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(this);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }
   
   public void mostarConfiguracion() {
        try {
            stagePrincipal.close();
            System.out.println("aplicacion.control.AplicacionControl.mostarConfiguracion()");
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaConfiguracion.fxml"));
            AnchorPane ventanaConfiguracion = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Administrador");
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaConfiguracion);
            ventana.setScene(scene);
            ConfiguracionController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(this);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }
   
   public void mostarRegistrarEmpresa() {
        try {
            System.out.println("aplicacion.control.AplicacionControl.mostarRegistrarEmpresa()");
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaRegistrarEmpresa.fxml"));
            AnchorPane ventanaRegistrarEmpresa = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Administrador");
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaRegistrarEmpresa);
            ventana.setScene(scene);
            RegistrarEmpresaController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(this);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
