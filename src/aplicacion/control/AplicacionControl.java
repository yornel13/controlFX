/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Auditar;
import aplicacion.control.util.Permisos;
import hibernate.model.Cliente;
import hibernate.model.Empresa;
import hibernate.model.Identidad;
import hibernate.model.Usuario;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;

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
        usuarioLogin.setText(permisos.getUsuario().getNombre() + 
                " " + permisos.getUsuario().getApellido());
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
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Debes ingresar tu usuario primero."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
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
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Ya no se puede realizar esta acción."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
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
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("No tienes permiso para ingresar aqui."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
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
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("No se pudo conectar al base de datos."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
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
        Button buttonOk = new Button("Si");
        Button buttonNo = new Button("no");
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
            Platform.exit();
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
       if (permisos != null) {
           login.setText("Salir");
           usuarioLogin.setText(permisos.getUsuario().getNombre() + 
                " " + permisos.getUsuario().getApellido());
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
           if (true) {
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
           } else {
              noPermitido();
           }
       } 
    }
   
    public void mostrarInEmpresa(Empresa empresa) {
        if (permisos == null) {
           noLogeado();
       } else {
           if (permisos.getPermiso(Permisos.A_EMPRESAS, Permisos.Nivel.VER)) {
               try {
                    System.out.println("aplicacion.control.AplicacionControl.mostarInEmpresa()");
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
                    String image = AplicacionControl.class.getResource("imagenes/fondo_in_empresa.jpg").toExternalForm();
                    ventanaInEmpresa.setStyle("-fx-background-image: url('" + image + "'); " + "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
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
            if (permisos.getPermiso(Permisos.A_EMPRESAS, Permisos.Nivel.CREAR)) {
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
    
    public void mostrarEmpleados(Empresa empresa) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.A_EMPLEADOS, Permisos.Nivel.VER)) {
                try {
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
    
    public void mostrarActuarialesEmpleados(Empresa empresa) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.A_PAGOS, Permisos.Nivel.VER)) {
                try {
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
    
    public void mostrarQuincenalEmpleados(Empresa empresa) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.A_PAGOS, Permisos.Nivel.VER)) {
                try {
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
    
    public void mostrarDecimosEmpleados(Empresa empresa) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.A_PAGOS, Permisos.Nivel.VER)) {
                try {
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
    
    public void mostrarDeudasEmpleados(Empresa empresa) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.A_PAGOS, Permisos.Nivel.VER)) {
                try {
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
    
    public void mostrarEmpleadosTodos() {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.A_EMPLEADOS, Permisos.Nivel.VER)) {
                try {
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
    
    public void mostrarRegistrarEmpleado(Empresa empresa) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.A_EMPLEADOS, Permisos.Nivel.CREAR)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaRegistrarEmpleado.fxml"));
                    TabPane ventanaRegistrarEmpleado = (TabPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Nuevo empleado");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_crear.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
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
            } else {
              noPermitido();
            }
        }
    }
    
    public void mostrarEmpleado(Usuario empleado) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.A_EMPLEADOS, Permisos.Nivel.VER)) {
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
            if (permisos.getPermiso(Permisos.A_EMPLEADOS, Permisos.Nivel.EDITAR)) {
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
    
    public void mostrarEmpresas() {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.A_EMPRESAS, Permisos.Nivel.VER)) {
                try {
                    System.out.println("aplicacion.control.AplicacionControl.mostarEmpresas()");
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
    
    public void mostrarClientes() {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.A_CLIENTES, Permisos.Nivel.VER)) {
                try {
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
    
    public void mostrarClientesEmpresa(Empresa empresa) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.A_CLIENTES, Permisos.Nivel.VER)) {
                try {
                    System.out.println("aplicacion.control.AplicacionControl.mostrarClientes()");
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaClientesEmpresa.fxml"));
                    AnchorPane ventanaClientesEmpresa = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Rol de pago por cliente");
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
    
    public void mostrarAdminitradores() {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.VER)) {
                try {
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
            if (permisos.getPermiso(Permisos.A_EMPRESAS, Permisos.Nivel.VER)) {
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
            if (permisos.getPermiso(Permisos.A_EMPRESAS, Permisos.Nivel.EDITAR)) {
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
            if (permisos.getPermiso(Permisos.A_CLIENTES, Permisos.Nivel.CREAR)) {
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
            if (permisos.getPermiso(Permisos.A_CLIENTES, Permisos.Nivel.VER)) {
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
            if (permisos.getPermiso(Permisos.A_CLIENTES, Permisos.Nivel.EDITAR)) {
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
    
    public void mostrarHorasEmpleados(Empresa empresa) {
        if (permisos == null) {
           noLogeado();
        } else {
           if (permisos.getPermiso(Permisos.A_HORAS_EMPLEADO, Permisos.Nivel.VER)) {
                try {
                    System.out.println("aplicacion.control.AplicacionControl.mostrarHorasEmpleados()");
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaHorasEmpleados.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Rol de pago por empleado");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    HorasEmpleadosController controller = loader.getController();
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
    
    public void mostrarHorasEmpleadosCliente(Empresa empresa, Cliente cliente) {
        if (permisos == null) {
           noLogeado();
        } else {
           if (permisos.getPermiso(Permisos.A_HORAS_EMPLEADO, Permisos.Nivel.VER)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaHorasEmpleadosCliente.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Rol de pago para el cliente " + cliente.getNombre());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(700);
                    ventana.setHeight(468);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    HorasEmpleadosClienteController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setCliente(empresa, cliente);
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
    
    public void mostrarHorasEmpleadosSinCliente(Empresa empresa) {
        if (permisos == null) {
           noLogeado();
        } else {
           if (permisos.getPermiso(Permisos.A_HORAS_EMPLEADO, Permisos.Nivel.VER)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaHorasEmpleadosSinCliente.fxml"));
                    AnchorPane ventanaEmpleados = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Rol de pago");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(700);
                    ventana.setHeight(468);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaEmpleados);
                    ventana.setScene(scene);
                    HorasEmpleadosSinClienteController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
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
    
    public void mostrarRolDePago(Usuario empleado, Timestamp inicio, Timestamp fin) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.A_ROL_DE_PAGO, Permisos.Nivel.VER)) {
                try {
                    System.out.println("aplicacion.control.AplicacionControl.mostrarRolDePago()");
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaRolDePago.fxml"));
                    AnchorPane ventanaRolDePago = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle(empleado.getNombre() + " " + empleado.getApellido());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaRolDePago);
                    ventana.setScene(scene);
                    RolDePagoController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpleado(empleado, inicio, fin);
                    ventana.show();
                    
                    // Registro para auditar
                    String detalles = "ingreso a rol de pago del empleado " 
                            + empleado.getNombre() + " " + empleado.getApellido();
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
    
    public void mostrarRolDePagoCliente(Usuario empleado, Cliente cliente, Timestamp inicio, Timestamp fin) {
        if (permisos == null) {
           noLogeado();
        } else {
           if (permisos.getPermiso(Permisos.A_ROL_DE_PAGO, Permisos.Nivel.VER)) {
               try {
                    System.out.println("aplicacion.control.AplicacionControl.mostrarRolDePago()");
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaRolDePagoCliente.fxml"));
                    AnchorPane ventanaRolDePago = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Empleado: " + empleado.getNombre() + " " 
                            + empleado.getApellido() + " | Cliente: " + cliente.getNombre());
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
                            + empleado.getNombre() + " " + empleado.getApellido();
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
    
    public void mostrarRolDePagoSinCliente(Usuario empleado, Timestamp inicio, Timestamp fin) {
        if (permisos == null) {
           noLogeado();
        } else {
           if (permisos.getPermiso(Permisos.A_ROL_DE_PAGO, Permisos.Nivel.VER)) {
               try {
                    System.out.println("aplicacion.control.AplicacionControl.mostrarRolDePago()");
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaRolDePagoSinCliente.fxml"));
                    AnchorPane ventanaRolDePago = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Empleado: " + empleado.getNombre() + " " 
                            + empleado.getApellido());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaRolDePago);
                    ventana.setScene(scene);
                    RolDePagoSinClienteController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(this);
                    controller.setEmpleado(empleado, inicio, fin);
                    ventana.show();
                    
                    // Registro para auditar
                    String detalles = "ingreso a rol de pago del empleado " 
                            + empleado.getNombre() + " " + empleado.getApellido();
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
    
    public void mostrarConfiguracionEmpresa(Empresa empresa) {
        if (permisos == null) {
           noLogeado();
       } else {
           if (permisos.getPermiso(Permisos.A_EMPRESAS, Permisos.Nivel.VER)) {
               try {
                    stagePrincipal.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaConfiguracionEmpresa.fxml"));
                    AnchorPane ventanaConfiguracionEmpresa = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle(empresa.getSiglas() + " - configuracion");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaConfiguracionEmpresa);
                    ventana.setScene(scene);
                    ConfiguracionEmpresaController controller = loader.getController();
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
    
    public void mostrarCargosDepartamentos() {
        if (permisos == null) {
           noLogeado();
       } else {
           if (true) {
               try {
                    stagePrincipal.close();
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
    
    public void mostrarPagos(Empresa empresa) {
        if (permisos == null) {
           noLogeado();
       } else {
           if (permisos.getPermiso(Permisos.A_ROL_DE_PAGO, Permisos.Nivel.VER)) {
               try {
                    stagePrincipal.close();
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaPagos.fxml"));
                    AnchorPane ventanaPagos = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle(empresa.getSiglas());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaPagos);
                    ventana.setScene(scene);
                    PagosController controller = loader.getController();
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
    
    public void mostrarAuditoria() {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.VER)) {
                try {
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
    
    public void mostrarTotalPagosEmpleados(Empresa empresa) {
        if (permisos == null) {
           noLogeado();
        } else {
            if (permisos.getPermiso(Permisos.A_ROL_DE_PAGO, Permisos.Nivel.VER)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaPagosTotalEmpleado.fxml"));
                    AnchorPane ventanaAuditar = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Pagos Totales");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.setWidth(800);
                    ventana.setHeight(628);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaAuditar);
                    ventana.setScene(scene);
                    PagosTotalEmpleadoController controller = loader.getController();
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
