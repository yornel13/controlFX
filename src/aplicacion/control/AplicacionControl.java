/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.model.Empresa;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
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
            stagePrincipal.setResizable(false);
            stagePrincipal.setHeight(425);
            stagePrincipal.setWidth(600);
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
            ventana.setResizable(false);
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
            ventana.setResizable(false);
            ventana.setWidth(600);
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
            ventana.setTitle("Registrar empresa");
            ventana.setResizable(false);
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
   
    public void mostarInEmpresa(Empresa empresa) {
        try {
            System.out.println("aplicacion.control.AplicacionControl.mostarInEmpresa()");
            stagePrincipal.close();
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaInEmpresaHome.fxml"));
            AnchorPane ventanaInEmpresa = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle(empresa.getSiglas());
            ventana.setResizable(false);
            ventana.setHeight(425);
            ventana.setWidth(600);
            ventana.initOwner(stagePrincipal);
            String image = AplicacionControl.class.getResource("imagenes/pasillo_empresa.jpg").toExternalForm();
            ventanaInEmpresa.setStyle("-fx-background-image: url('" + image + "'); " + "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
            Scene scene = new Scene(ventanaInEmpresa);
            ventana.setScene(scene);
            InEmpresaController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(this);
            controller.setEmpresa(empresa);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }
    
    public void mostarEmpleados() {
        try {
            System.out.println("aplicacion.control.AplicacionControl.mostarEmpleados()");
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEmpleados.fxml"));
            AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Empleados");
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaEmpleados);
            ventana.setScene(scene);
            EmpleadosController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(this);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }
    
    public void mostarRegistrarEmpleado() {
        try {
            System.out.println("aplicacion.control.AplicacionControl.mostarRegistrarEmpleado()");
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaRegistrarEmpleado.fxml"));
            TabPane ventanaRegistrarEmpleado = (TabPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Nuevo empleado");
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaRegistrarEmpleado);
            ventana.setScene(scene);
            RegistrarEmpleadoController controller = loader.getController();
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
