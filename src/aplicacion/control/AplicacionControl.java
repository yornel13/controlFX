/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Auditar;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.MaterialDesignButton;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.model.Cliente;
import hibernate.model.Empresa;
import hibernate.model.Identidad;
import hibernate.model.RolCliente;
import hibernate.model.Usuario;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;
import javafx.application.Platform;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.paint.Color;
import static javafx.application.Application.launch;

/**
 *
 * @author Yornel
 */
public class AplicacionControl extends Application {
    
    private Stage stagePrincipal;
    private AnchorPane rootPane;
    
    public Scene loginScene;
    public Permisos permisos;
    
    // Boton y texto de logeo
    public Button login;
    public Label usuarioLogin;
    
    public Auditar au;
    
    public EmpleadosController changeEmpleadosController;
    public EmpleadosTodosController changeEmpleadosTodosController;
    public ClientesController changeClientesController;
    public EmpresasController changeEmpresasController;
    public AdministradoresController changeAdministradoresController;
    public ActuarialesEmpleadosController changeActuarialesEmpleadosController;
    
    @Override
    public void start(Stage stagePrincipal) throws Exception {
        this.stagePrincipal = stagePrincipal;
        mostrarVentanaPrincipal();
    }
    
    public void login(Button login, Label usuarioLogin) {
        this.login = login;
        this.usuarioLogin = usuarioLogin;
        if (permisos != null) {
            permisos = null;
            login.setText("Ingresar");
            usuarioLogin.setText("");
        } else {
            if (loginScene == null || !loginScene.getWindow().isShowing()) {
                mostrarLogin();
            }
        }
    }
    
    public void loginCompletado(Usuario usuario) {
        permisos.setUsuario(usuario);
        login.setText("Salir");
        usuarioLogin.setText(permisos.getUsuario().getApellido()+ 
                " " + permisos.getUsuario().getNombre());
        au = new Auditar();
        au.saveRegistro(au.INGRESO, au.LOGEO, usuario, null);
    }
    
    public void mostrarLogin() {
        try {
            System.out.println("aplicacion.control.AplicacionControl.mostarLogin()");
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaLogin.fxml"));
            AnchorPane ventanaLogin = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Ingresar");
            String stageIcon = AplicacionControl.class.getResource("imagenes/user.png").toExternalForm();
            ventana.getIcons().add(new Image(stageIcon));
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            loginScene = new Scene(ventanaLogin);
            ventana.setScene(loginScene);
            LoginController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(this);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }
    
    public void noLogeado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("No hay usuario");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        MaterialDesignButton buttonOk = new MaterialDesignButton("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
        children(new Text("Debes ingresar tu usuario primero."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        dialogStage.show();
        buttonOk.setPrefWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
    }
    
    public void noSePuede() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Lo siento!");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        MaterialDesignButton buttonOk = new MaterialDesignButton("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
        children(new Text("Ya no se puede realizar esta acción."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        dialogStage.show();
        buttonOk.setPrefWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
    }
    
    public void noPermitido() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("No permitido");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        MaterialDesignButton buttonOk = new MaterialDesignButton("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
        children(new Text("No tienes permiso para ingresar aqui."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        dialogStage.show();
        buttonOk.setPrefWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
    }
    
    public void dataBaseError() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Error de Base de Datos");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        MaterialDesignButton buttonOk = new MaterialDesignButton("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
        children(new Text("No se pudo conectar al base de datos."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        dialogStage.show();
        buttonOk.setPrefWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
    }
    
    public void cerrarWindows() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Precaución");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        MaterialDesignButton buttonOk = new MaterialDesignButton("Si");
        MaterialDesignButton buttonNo = new MaterialDesignButton("no");
        HBox hBox = HBoxBuilder.create()
                .spacing(10.0) //In case you are using HBoxBuilder
                .padding(new Insets(5, 5, 5, 5))
                .alignment(Pos.CENTER)
                .children(buttonOk, buttonNo)
                .build();
        hBox.maxWidth(120);
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("¿Seguro que desea salir de la aplicacion?"), hBox).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        buttonOk.setMinWidth(50);
        buttonNo.setMinWidth(50);
        buttonOk.setOnAction((ActionEvent e) -> {
            HibernateSessionFactory.closeSession();
            Platform.exit();
            System.exit(0);
            dialogStage.close();
        });
        buttonNo.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    private void insertarDatosDeUsuarios(Button login, Label usuarioLogin) {
        this.login = login;
        this.usuarioLogin = usuarioLogin;
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.valueOf("#039BE5"));
        this.login.setOnMouseEntered((MouseEvent t) -> {
            this.login.setEffect(ds);
        });
        this.login.setOnMouseExited((MouseEvent t) -> {
            this.login.setEffect(null);
        });
       if (permisos != null) {
           login.setText("Salir");
           usuarioLogin.setText(permisos.getUsuario().getApellido()+ 
                " " + permisos.getUsuario().getNombre());
       }
    }
    
    /*
     * cargamos la ventana principal
     */
    
    public void mostrarVentanaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaPrincipal.fxml"));
            rootPane = (AnchorPane) loader.load();
            String image = AplicacionControl.class.getResource("imagenes/fondo_principal.jpg").toExternalForm();
            rootPane.setStyle("-fx-background-image: url('" + image + "'); " + "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
            Scene scene = new Scene(rootPane);
            stagePrincipal.setTitle("Bienvenido");
            String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
            stagePrincipal.getIcons().add(new Image(stageIcon));
            stagePrincipal.setResizable(false);
            stagePrincipal.setWidth(800);
            stagePrincipal.setHeight(630);
            stagePrincipal.setScene(scene);
            PrincipalController controller = loader.getController();
            controller.setStagePrincipal(stagePrincipal);
            controller.setProgramaPrincipal(this);
            insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
            Platform.setImplicitExit(false);
            stagePrincipal.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    cerrarWindows();
                    event.consume();
                }
            });
            stagePrincipal.show();
        } catch (IOException e) {
            //tratar la excepción.
        }
   }
   
   public void mostrarConfiguracion() {
        if (permisos == null) {
            noLogeado();
        } else {
            try {
                stagePrincipal.close();
                FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaConfiguracion.fxml"));
                AnchorPane ventanaConfiguracion = (AnchorPane) loader.load();
                Stage ventana = new Stage();
                ventana.setTitle("Administrador");
                String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                ventana.getIcons().add(new Image(stageIcon));
                ventana.setResizable(false);
                ventana.setWidth(800);
                ventana.setHeight(628);
                ventana.initOwner(stagePrincipal);
                Scene scene = new Scene(ventanaConfiguracion);
                ventana.setScene(scene);
                ConfiguracionController controller = loader.getController();
                controller.setStagePrincipal(ventana);
                controller.setProgramaPrincipal(this);
                insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                Platform.setImplicitExit(false);
                ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                   @Override
                    public void handle(WindowEvent event) {
                        cerrarWindows();
                        event.consume();
                    }
                });
                ventana.show();
                au.saveRegistro(au.INGRESO, au.CONFIGURACION, permisos.getUsuario(), null);

            } catch (Exception e) {
                e.printStackTrace();
                 //tratar la excepción
            }
        } 
    }
   
    public void mostrarInEmpresa(Empresa empresa) {
        if (permisos == null) { 
            noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.EMPRESAS, Permisos.Nivel.VER)) { 
                try {
                    stagePrincipal.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaInEmpresaHome.fxml"));
                    AnchorPane ventanaInEmpresa = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle(empresa.getSiglas());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    //String image = AplicacionControl.class.getResource("imagenes/fondo_1.jpg").toExternalForm();
                    //ventanaInEmpresa.setStyle("-fx-background-image: url('" + image + "'); " + "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
                    Scene scene = new Scene(ventanaInEmpresa);
                    ventana.setScene(scene);
                    InEmpresaController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.EMPRESA, permisos.getUsuario(), empresa.getNombre());
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
           } else {
               noPermitido();
           }
       }
    }
   
   public void mostrarRegistrarEmpresa() {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.EMPRESAS, Permisos.Nivel.CREAR)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaRegistrarEmpresa.fxml"));
                    AnchorPane ventanaRegistrarEmpresa = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Registrar empresa");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_crear.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
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
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarEmpleados(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.EMPLEADOS, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEmpleados.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Empleados");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    EmpleadosController controller = loader.getController();
                    changeEmpleadosController = controller;
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.EMPLEADOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarActuarialesEmpleados(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaActuarialesEmpleados.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Actuariales");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    ActuarialesEmpleadosController controller = loader.getController();
                    changeActuarialesEmpleadosController = controller;
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.PAGOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarQuincenalEmpleados(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaQuincenalEmpleados.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Adelanto Quincenal");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    QuincenalEmpleadosController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.PAGOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarDecimosEmpleados(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaDecimosEmpleados.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Acumulacion de decimos");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    DecimosEmpleadosController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.PAGOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarDecimosAcumuladoEmpleados(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaDecimosAcumuladoEmpleados.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Acumulacion de decimos");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    DecimosAcumuladoEmpleadosController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.PAGOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarDeudasEmpleados(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaDeudasEmpleados.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Deudas");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    DeudasEmpleadosController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.DEUDAS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarIessEmpleados(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaIessEmpleados.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("IESS");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    IessEmpleadosController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.PAGOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarAjusteTerceroEmpleados(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaAjusteTerceroEmpleados.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Ajuste de Decimo Tercero");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    AjusteTerceroEmpleadosController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.PAGOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarAjusteCuartoEmpleados(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaAjusteCuartoEmpleados.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Ajuste de Decimo Cuarto");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    AjusteCuartoEmpleadosController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.PAGOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarAjusteReservaEmpleados(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaAjusteReservaEmpleados.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Ajuste de Fondo de Reserva");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    AjusteReservaEmpleadosController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.PAGOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarAjusteVacacionesEmpleados(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaAjusteVacacionesEmpleados.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Ajuste de Vacaciones");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    AjusteVacacionesEmpleadosController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.PAGOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarDescuentosEmpleados(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaDescuentosEmpleados.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Descuentos");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    DescuentosEmpleadosController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.DEUDAS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarDescuentosTodosEmpleados(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaDescuentosTodosEmpleados.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Deudas por tipo de cuenta");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    DescuentosTodosEmpleadosController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.DEUDAS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarPlanillaIess(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaPlanillaIess.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Historial Laboral");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    PlanillaIessController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.PAGOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarDiasDerechoVacaciones(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaVacacionesEmpleados.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Dias de derecho a vacaciones");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    VacacionesEmpleadosController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.PAGOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarSueldoEmpleados(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaSueldoEmpleados.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Sueldo");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    SueldoEmpleadosController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.PAGOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarEmpleadosTodos(Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.EMPLEADOS, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEmpleadosTodos.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Todos los empleados");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    EmpleadosTodosController controller = loader.getController();
                    changeEmpleadosTodosController = controller;
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.EMPLEADOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
                noPermitido();
            }
       }
    }
    
    public void mostrarEmpleado(Usuario empleado) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.EMPLEADOS, Permisos.Nivel.VER)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEmpleado.fxml"));
                    AnchorPane ventanaEmpleado = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Detalles de empleado");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_mostrar.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleado);
                    ventana.setScene(scene);
                    EmpleadoController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpleado(empleado);
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.EMPLEADO, permisos.getUsuario(), empleado.getNombre());
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarEditarEmpleado(Usuario empleado) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.EMPLEADOS, Permisos.Nivel.EDITAR)) {
                try {
                    System.out.println("aplicacion.control.AplicacionControl.mostarEditarEmpleado()");
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEditarEmpleado.fxml"));
                    TabPane ventanaEditarEmpleado = (TabPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Editar empleado");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_editar.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
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
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarEmpresas(Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.EMPRESAS, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEmpresas.fxml"));
                    AnchorPane ventanaEmpresas = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Empresas");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpresas);
                    ventana.setScene(scene);
                    EmpresasController controller = loader.getController();
                    changeEmpresasController = controller;
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.EMPRESAS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarClientes(Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.CLIENTES, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    System.out.println("aplicacion.control.AplicacionControl.mostrarClientes()");
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaClientes.fxml"));
                    AnchorPane ventanaClientes = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Clientes");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaClientes);
                    ventana.setScene(scene);
                    ClientesController controller = loader.getController();
                    changeClientesController = controller;
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.CLIENTES, permisos.getUsuario(), null);

                } catch (Exception e) {
                     e.printStackTrace();
                     //tratar la excepción
                }
            } else {
                noPermitido();
            }
        }
    }
    
    public void mostrarClientesEmpresa(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.HORAS, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    System.out.println("aplicacion.control.AplicacionControl.mostrarClientes()");
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaClientesEmpresa.fxml"));
                    AnchorPane ventanaClientesEmpresa = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle(empresa.getNombre() + " - Rol de pago por cliente");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaClientesEmpresa);
                    ventana.setScene(scene);
                    ClientesEmpresaController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();

                } catch (Exception e) {
                     e.printStackTrace();
                     //tratar la excepción
                }
            } else {
               noPermitido();
            }
        }
    }
    
    public void mostrarClientesParaBonos(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.HORAS, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    System.out.println("aplicacion.control.AplicacionControl.mostrarClientes()");
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaClientesParaBonos.fxml"));
                    AnchorPane ventanaClientesEmpresa = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle(empresa.getNombre() + " - Bonos y Transporte");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaClientesEmpresa);
                    ventana.setScene(scene);
                    ClientesParaBonosController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();

                } catch (Exception e) {
                     e.printStackTrace();
                     //tratar la excepción
                }
            } else {
               noPermitido();
            }
        }
    }
    
    public void mostrarClientesParaRol(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.ROLES, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaClientesParaRoles.fxml"));
                    AnchorPane ventanaClientesEmpresa = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Clientes");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaClientesEmpresa);
                    ventana.setScene(scene);
                    ClientesParaRolesController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();

                } catch (Exception e) {
                     e.printStackTrace();
                     //tratar la excepción
                }
            } else {
               noPermitido();
            }
        }
    }
    
    public void mostrarEmpleadosParaRol(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.ROLES, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEmpleadosParaRoles.fxml"));
                    AnchorPane ventanaClientesEmpresa = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Empleados");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaClientesEmpresa);
                    ventana.setScene(scene);
                    EmpleadosParaRolController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                } catch (Exception e) {
                     e.printStackTrace();
                     //tratar la excepción
                }
            } else {
               noPermitido();
            }
        }
    }
    
    public void mostrarAdminitradores(Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    System.out.println("aplicacion.control.AplicacionControl.mostrarAdminitradores()");
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaAdministradores.fxml"));
                    AnchorPane ventanaAdministradores = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Administradores");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaAdministradores);
                    ventana.setScene(scene);
                    AdministradoresController controller = loader.getController();
                    changeAdministradoresController = controller;
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.ADMINITRADORES, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
               noPermitido();
            }
        }
    }
    
    public void mostrarRegistrarAdministrador() {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.VER)) {
                try {
                    System.out.println("aplicacion.control.AplicacionControl.mostrarRegistrarAdministrador()");
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaRegistrarAdministrador.fxml"));
                    AnchorPane ventanaRegistrarAdministrador = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Nuevo Administrador");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_crear.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaRegistrarAdministrador);
                    ventana.setScene(scene);
                    RegistrarAdministradorController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    ventana.show();
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarEmpresa(Empresa empresa) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.EMPRESAS, Permisos.Nivel.VER)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEmpresa.fxml"));
                    AnchorPane ventanaEmpresa = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Detalles de empresa");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_mostrar.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpresa);
                    ventana.setScene(scene);
                    EmpresaController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.EMPRESA, permisos.getUsuario(), empresa.getNombre());
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarEditarEmpresa(Empresa empresa) {
         if (permisos == null) {
           noLogeado();
         } else {
            if (permisos.getPermiso(Permisos.EMPRESAS, Permisos.Nivel.EDITAR)) {
                try {
                    System.out.println("aplicacion.control.AplicacionControl.mostarEditarEmpresa()");
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEditarEmpresa.fxml"));
                    AnchorPane ventanaEditarEmpresa = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Editar empresa");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_editar.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
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
            } else {
               noPermitido();
            }
        }
    }
    
    public void mostrarRegistrarCliente() {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.CLIENTES, Permisos.Nivel.CREAR)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaRegistrarCliente.fxml"));
                    AnchorPane ventanaRegistrarCliente = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Registrar cliente");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_crear.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
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
            } else {
                noPermitido();
            }
        }
    }
    
    public void mostrarCliente(Cliente cliente) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.CLIENTES, Permisos.Nivel.VER)) {
                try {
                    System.out.println("aplicacion.control.AplicacionControl.mostrarCliente()");
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaCliente.fxml"));
                    AnchorPane ventanaCliente = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Detalles de cliente");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_mostrar.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaCliente);
                    ventana.setScene(scene);
                    ClienteController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setCliente(cliente);
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.CLIENTE, permisos.getUsuario(), cliente.getNombre());
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
                noPermitido();
            }
        }
    }
    
    public void mostrarEditarCliente(Cliente cliente) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.CLIENTES, Permisos.Nivel.EDITAR)) {
                try {
                    System.out.println("aplicacion.control.AplicacionControl.mostrarEditarCliente()");
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEditarCliente.fxml"));
                    AnchorPane ventanaEditarCliente = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Editar cliente");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_editar.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
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
            } else {
                noPermitido();
            }
        }
    }
    
    public void mostrarHorasEmpleadosCliente(Empresa empresa, Cliente cliente, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
           if (permisos.getPermiso(Permisos.HORAS, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaHorasEmpleadosCliente.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    if (cliente == null) 
                        ventana.setTitle("Horario para personal administrativo");
                    else
                        ventana.setTitle("Horario para el cliente " + cliente.getNombre());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    HorasEmpleadosClienteController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setCliente(empresa, cliente);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.ROL_DE_PAGO, permisos.getUsuario(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
           } else {
               noPermitido();
           }
       } 
    }
    
    public void mostrarBonosEmpleados(Empresa empresa, Cliente cliente, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
           if (permisos.getPermiso(Permisos.HORAS, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaBonosEmpleados.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    if (cliente == null) 
                        ventana.setTitle("Bonos para personal administrativo");
                    else
                        ventana.setTitle("Bonos para el cliente " + cliente.getNombre());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    BonosEmpleadosController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa, cliente);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.ROL_DE_PAGO, permisos.getUsuario(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
           } else {
               noPermitido();
           }
       } 
    }
    
    public void mostrarRolDePagoCliente(Usuario empleado, Cliente cliente, Fecha inicio, Fecha fin) {
        if (permisos == null) {
           noLogeado();
        } else {
           if (permisos.getPermiso(Permisos.HORAS, Permisos.Nivel.CREAR)) {
               try {
                    System.out.println("aplicacion.control.AplicacionControl.mostrarRolDePago()");
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaRolDePagoCliente.fxml"));
                    AnchorPane ventanaRolDePago = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    if (cliente == null)
                        ventana.setTitle("Empleado: " + empleado.getApellido()+ " " 
                                + empleado.getNombre()+ " | Administrativo");
                    else
                        ventana.setTitle("Empleado: " + empleado.getApellido()+ " " 
                            + empleado.getNombre()+ " | Cliente: " + cliente.getNombre());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaRolDePago);
                    ventana.setScene(scene);
                    RolDePagoClienteController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpleado(empleado, cliente, inicio, fin);
                    ventana.show();
                    
                    // Registro para auditar
                    String detalles = "ingreso a rol de pago del empleado " 
                            + empleado.getApellido()+ " " + empleado.getNombre();
                    au.saveIngreso(detalles, permisos.getUsuario());

                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
           } else {
              noPermitido();
           }
       } 
    }
    
    public void mostrarEditarContrasena(Identidad identidad) {
        if (permisos == null) {
           noLogeado();
       } else {
           if (permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.CREAR)) {
               try {
                    System.out.println("aplicacion.control.AplicacionControl.mostarRegistrarEmpleado()");
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEditarContrasena.fxml"));
                    AnchorPane ventanaEditarContrasena = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Usuario: " + identidad.getNombreUsuario());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_editar.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEditarContrasena);
                    ventana.setScene(scene);
                    EditarContrasenaController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setIdentidad(identidad);
                    ventana.show();

                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
           } else {
              noPermitido();
           }
       }
    }
    
    public void mostrarEditarUsuario(Identidad identidad) {
        if (permisos == null) {
           noLogeado();
       } else {
           if (permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.CREAR)) {
               try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEditarUsuario.fxml"));
                    AnchorPane ventanaEditarUsuario = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Usuario: " + identidad.getNombreUsuario());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_editar.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEditarUsuario);
                    ventana.setScene(scene);
                    EditarUsuarioController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setIdentidad(identidad);
                    ventana.show();

                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
           } else {
              noPermitido();
           }
       }
    }
    
    public void mostrarEditarRol(Identidad identidad) {
        if (permisos == null) {
           noLogeado();
       } else {
           if (permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.CREAR)) {
               try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEditarRol.fxml"));
                    AnchorPane ventanaEditarPermisos = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Usuario: " + identidad.getNombreUsuario());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_editar.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEditarPermisos);
                    ventana.setScene(scene);
                    EditarRolController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setIdentidad(identidad);
                    ventana.show();

                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
           } else {
              noPermitido();
           }
       }
    }
    
    public void mostrarConstantes(Stage stage) {
        if (permisos == null) {
           noLogeado();
       } else {
           if (permisos.getPermiso(Permisos.EMPRESAS, Permisos.Nivel.EDITAR)) {
               try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaConstantes.fxml"));
                    AnchorPane ventanaConfiguracionEmpresa = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Constantes Globales");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaConfiguracionEmpresa);
                    ventana.setScene(scene);
                    ConstantesController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.CONFIGURACION, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
           } else {
              noPermitido();
           }
       }
    }
    
    public void mostrarClienteVariables(Cliente cliente, Stage stage) {
        if (permisos == null) {
           noLogeado();
       } else {
           if (permisos.getPermiso(Permisos.CLIENTES, Permisos.Nivel.EDITAR)) {
               try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaClienteVariables.fxml"));
                    AnchorPane ventanaConfiguracionEmpresa = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    if (cliente != null)
                        ventana.setTitle("Cliente " + cliente.getNombre() + " - Constantes.");
                    else
                        ventana.setTitle("Personal Administrativo - Constantes.");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaConfiguracionEmpresa);
                    ventana.setScene(scene);
                    ClienteVariablesController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setCliente(cliente);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.CONFIGURACION, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
           } else {
              noPermitido();
           }
       }
    }
    
    public void mostrarCargosDepartamentos(Stage stage) {
        if (permisos == null) {
           noLogeado();
       } else {
           if (permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.VER)) {
               try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaDepartamentosCargos.fxml"));
                    AnchorPane ventanaDepartamentosCargos = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Configuracion");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaDepartamentosCargos);
                    ventana.setScene(scene);
                    DepartamentosCargosController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
           } else {
              noPermitido();
           }
       }
    }
    
    public void mostrarAuditoria(Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaAuditar.fxml"));
                    AnchorPane ventanaAuditar = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Auditoria");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaAuditar);
                    ventana.setScene(scene);
                    AuditoriaController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.AUDITORIA, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
   public void mostrarRolCliente(Empresa empresa, Cliente cliente, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.ROLES, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaRolCliente.fxml"));
                    AnchorPane ventanaAuditar = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    if (cliente != null)
                        ventana.setTitle("Rol del Cliente " + cliente.getNombre());
                    else 
                        ventana.setTitle("Rol de pago para personal Administrativo");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaAuditar);
                    ventana.setScene(scene);
                    RolClienteController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setCliente(cliente, empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    if (cliente != null)
                        au.saveRegistro(au.INGRESO, au.ROL_DE_PAGO + " del cliente " 
                                + cliente.getNombre(), permisos.getUsuario(), null);
                    else
                        au.saveRegistro(au.INGRESO, au.ROL_DE_PAGO + " para "
                                + "personal administratico", permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarRolIndividual(Empresa empresa, Usuario empleado, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.ROLES, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaRolIndividual.fxml"));
                    AnchorPane ventanaAuditar = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Rol del Empleado " + empleado.getApellido()+ " " + empleado.getNombre());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaAuditar);
                    ventana.setScene(scene);
                    RolIndividualController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpleado(empleado, empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.ROL_DE_PAGO + " del empleado " 
                            + empleado.getNombre() + " " + empleado.getApellido(), 
                            permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarRolClienteEmpleado(RolCliente pago) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.ROLES, Permisos.Nivel.VER)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaRolClienteEmpleado.fxml"));
                    AnchorPane ventanaAuditar = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Rol de pago");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaAuditar);
                    ventana.setScene(scene);
                    RolClienteEmpleadoController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setRolInfo(pago);
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.ROL_DE_PAGO, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarAsignaHorarios(Empresa empresa) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.ROLES, Permisos.Nivel.VER)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaAsignarHorarios.fxml"));
                    AnchorPane ventanaAuditar = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Asignar Horarios");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaAuditar);
                    ventana.setScene(scene);
                    AsignarHorariosController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setEmpresa(empresa);
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.ROL_DE_PAGO, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarAsignaHorasExtras(Empresa empresa) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.ROLES, Permisos.Nivel.VER)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaAsignarHorasExtras.fxml"));
                    AnchorPane ventanaAuditar = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Asignar Horas Extras");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaAuditar);
                    ventana.setScene(scene);
                    AsignarHorasExtrasController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setEmpresa(empresa);
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.ROL_DE_PAGO, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarPagoQuincenal(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.PAGOS, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaPagoQuincenal.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Pago de Adelanto Quincenal");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    PagoQuincenalController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.PAGOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarPagoVacaciones(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.PAGOS, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaPagoVacaciones.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Pago de Vacaciones");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    PagoVacacionesController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.PAGOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
     
    public void mostrarPagoMensual(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.PAGOS, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaPagoMensual.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Pago Mensual de Empleados");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    PagoMensualController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.PAGOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarPagoDecimoTercero(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.PAGOS, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaPagoDecimoTercero.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Pago de Decimo Tercero");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    PagoDecimoTerceroController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.PAGOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
     public void mostrarPagoDecimoTerceroAnual(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.PAGOS, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaPagoDecimoTerceroAnual.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Pago de Decimo Tercero");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    PagoDecimoTerceroAnualController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.PAGOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
     
     public void mostrarPagoDecimoCuartoAnual(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.PAGOS, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaPagoDecimoCuartoAnual.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Pago de Decimo Cuarto");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    PagoDecimoCuartoAnualController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.PAGOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarPagoDecimoCuarto(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.PAGOS, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaPagoDecimoCuarto.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Pago de Decimo Cuarto");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    PagoDecimoCuartoController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.PAGOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarReportes(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.PAGOS, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaReportesEmpleados.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Reportes");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    ReportesEmpleadosController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.REPORTES, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarHorasEmpleadosPorDia(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
           if (permisos.getPermiso(Permisos.HORAS, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaHorasEmpleadosDia.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Horas empleados");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    HorasEmpleadosPorDiaController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.HORARIOS_EMPLEADOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
           } else {
              noPermitido();
           }
       } 
    }
    
    public void mostrarHorarios(Empresa empresa, Stage stage) {
        if (permisos == null) {
           noLogeado();
        } else {
           if (permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.VER)) {
                try {
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaHorarios.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Horarios");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    HorariosController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpresa(empresa);
                    insertarDatosDeUsuarios(controller.login, controller.usuarioLogin);
                    Platform.setImplicitExit(false);
                    ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            cerrarWindows();
                            event.consume();
                        }
                    });
                    ventana.show();
                    au.saveRegistro(au.INGRESO, au.HORARIOS, permisos.getUsuario(), null);
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
           } else {
              noPermitido();
           }
       } 
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
