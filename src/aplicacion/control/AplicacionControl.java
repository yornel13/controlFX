/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Permisos;
import hibernate.model.Cliente;
import hibernate.model.Empresa;
import hibernate.model.Usuario;
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
    
    public Permisos permisos;
    
    @Override
    public void start(Stage stagePrincipal) throws Exception {
        this.stagePrincipal = stagePrincipal;
        mostrarVentanaPrincipal();
    }
    
    public void login() {
        
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
    
   public void mostrarLogin() {
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
   
   public void mostrarConfiguracion() {
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
   
   public void mostrarRegistrarEmpresa() {
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
   
    public void mostrarInEmpresa(Empresa empresa) {
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
    
    public void mostrarEmpleados(Empresa empresa) {
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
            controller.setEmpresa(empresa);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }
    
    public void mostrarRegistrarEmpleado(Empresa empresa) {
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
            controller.setEmpresa(empresa);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }
    
    public void mostrarEmpleado(Usuario empleado) {
        try {
            System.out.println("aplicacion.control.AplicacionControl.mostrarEmpleado()");
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEmpleado.fxml"));
            AnchorPane ventanaEmpleado = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Detalles de empleado");
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaEmpleado);
            ventana.setScene(scene);
            EmpleadoController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(this);
            controller.setEmpleado(empleado);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }
    
    public void mostrarEditarEmpleado(Usuario empleado) {
        try {
            System.out.println("aplicacion.control.AplicacionControl.mostarEditarEmpleado()");
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEditarEmpleado.fxml"));
            TabPane ventanaEditarEmpleado = (TabPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Editar empleado");
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaEditarEmpleado);
            ventana.setScene(scene);
            EditarEmpleadoController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(this);
            controller.setEmpleado(empleado);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }
    
    public void mostrarEmpresas() {
        try {
            System.out.println("aplicacion.control.AplicacionControl.mostarEmpresas()");
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEmpresas.fxml"));
            AnchorPane ventanaEmpresas = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Empresas");
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaEmpresas);
            ventana.setScene(scene);
            EmpresasController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(this);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }
    
    public void mostrarEmpresa(Empresa empresa) {
        try {
            System.out.println("aplicacion.control.AplicacionControl.mostrarEmpleado()");
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEmpresa.fxml"));
            AnchorPane ventanaEmpresa = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Detalles de empresa");
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaEmpresa);
            ventana.setScene(scene);
            EmpresaController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(this);
            controller.setEmpresa(empresa);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }
    
    public void mostrarEditarEmpresa(Empresa empresa) {
        try {
            System.out.println("aplicacion.control.AplicacionControl.mostarEditarEmpresa()");
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEditarEmpresa.fxml"));
            AnchorPane ventanaEditarEmpresa = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Editar empleado");
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaEditarEmpresa);
            ventana.setScene(scene);
            EditarEmpresaController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(this);
            controller.setEmpresa(empresa);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }
    
    public void mostrarRegistrarCliente() {
        try {
            System.out.println("aplicacion.control.AplicacionControl.mostarRegistrarCliente()");
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaRegistrarCliente.fxml"));
            AnchorPane ventanaRegistrarCliente = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Registrar cliente");
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaRegistrarCliente);
            ventana.setScene(scene);
            RegistrarClienteController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(this);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }
    
    public void mostrarClientes() {
        try {
            System.out.println("aplicacion.control.AplicacionControl.mostrarClientes()");
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaClientes.fxml"));
            AnchorPane ventanaClientes = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Clientes");
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaClientes);
            ventana.setScene(scene);
            ClientesController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(this);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }
    
    public void mostrarCliente(Cliente cliente) {
        try {
            System.out.println("aplicacion.control.AplicacionControl.mostrarCliente()");
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaCliente.fxml"));
            AnchorPane ventanaCliente = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Detalles de cliente");
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaCliente);
            ventana.setScene(scene);
            ClienteController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(this);
            controller.setCliente(cliente);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }
    
    public void mostrarEditarCliente(Cliente cliente) {
        try {
            System.out.println("aplicacion.control.AplicacionControl.mostrarEditarCliente()");
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEditarCliente.fxml"));
            AnchorPane ventanaEditarCliente = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Editar cliente");
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaEditarCliente);
            ventana.setScene(scene);
            EditarClienteController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(this);
            controller.setCliente(cliente);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }
    
    public void mostrarHorasEmpleados(Empresa empresa) {
        try {
            System.out.println("aplicacion.control.AplicacionControl.mostrarHorasEmpleados()");
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaHorasEmpleados.fxml"));
            AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Empleados");
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaEmpleados);
            ventana.setScene(scene);
            HorasEmpleadosController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(this);
            controller.setEmpresa(empresa);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }
    
    public void mostrarRolDePago(Usuario empleado) {
        try {
            System.out.println("aplicacion.control.AplicacionControl.mostrarRolDePago()");
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaRolDePago.fxml"));
            AnchorPane ventanaRolDePago = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Rol de Pago");
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaRolDePago);
            ventana.setScene(scene);
            RolDePagoController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(this);
            controller.setEmpleado(empleado);
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
