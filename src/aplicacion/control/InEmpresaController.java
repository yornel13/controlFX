/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.MaterialDesignButton;
import aplicacion.control.util.Roboto;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Empresa;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class InEmpresaController implements Initializable {
    
    Empresa empresa;

    private Stage stagePrincipal;

    private AplicacionControl aplicacionControl;
    
    @FXML
    private Label labelEmpresaSeguridad;

    @FXML
    private Label numeracionLabel;

    @FXML
    private Label empresaLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private Button buttonEmpleados;
    
    @FXML
    private MenuButton buttonHoras;
    
     @FXML
    private MenuButton buttonDeudas;
    
    @FXML
    private MenuButton buttonPagos;
    
    @FXML
    private MenuButton buttonRoles;
            
    @FXML
    private Button buttonReportes;
    
    @FXML
    private MenuButton buttonAjustes;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private MenuButton buttonEdicion;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }

    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }

    @FXML
    private void goHome(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarVentanaPrincipal();
    }

    @FXML
    private void verEmpleados(ActionEvent event) {
        aplicacionControl.mostrarEmpleados(empresa, stagePrincipal);
    }
    
    @FXML
    private void verReportes(ActionEvent event) {
        aplicacionControl.mostrarReportes(empresa, stagePrincipal);
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        empresaLabel.setText(empresa.getNombre());
        empresaLabel.setFont(Roboto.MEDIUM(18));
        numeracionLabel.setText("Nruc: " + empresa.getNumeracion());
        numeracionLabel.setFont(Roboto.REGULAR(14));
        labelEmpresaSeguridad.setFont(Roboto.REGULAR(14));
        totalLabel.setFont(Roboto.REGULAR(14));
        totalLabel.setText("Total de empleados: " 
                + new UsuarioDAO().countEmpleados(empresa.getId()));
    }
 
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        {
            Image imageGuardia = new Image(getClass().getResourceAsStream("imagenes/bt_guardia.png"));
            ImageView imageView = new ImageView(imageGuardia);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            buttonEmpleados.setGraphic(imageView);
            buttonEmpleados.setFont(Roboto.MEDIUM(15));
            buttonEmpleados.setOnMouseEntered((MouseEvent t) -> {
                buttonEmpleados.setStyle("-fx-background-color: #E0E0E0;");
            });
            buttonEmpleados.setOnMouseExited((MouseEvent t) -> {
                buttonEmpleados.setStyle("-fx-background-color: #039BE5;");
            });
        }
        {
            Image imageGuardia = new Image(getClass().getResourceAsStream("imagenes/bt_horas.png"));
            ImageView imageView = new ImageView(imageGuardia);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            buttonHoras.setGraphic(imageView);
            buttonHoras.setFont(Roboto.MEDIUM(15));
            buttonHoras.getItems().clear();
            MenuItem menuAsignar = new MenuItem("Asignar Horario");
            buttonHoras.getItems().add(menuAsignar);
            MenuItem menuAsignarExtras = new MenuItem("Asignar Horas Extras");
            buttonHoras.getItems().add(menuAsignarExtras);
            MenuItem menuItemEmpleado = new MenuItem("Por Empleado");
            buttonHoras.getItems().add(menuItemEmpleado);
            MenuItem menuItemCliente = new MenuItem("Rol Cliente");
            buttonHoras.getItems().add(menuItemCliente);
            MenuItem menuItemHorarios = new MenuItem("Horarios");
            buttonHoras.getItems().add(menuItemHorarios);
            menuItemEmpleado.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarHorasEmpleadosPorDia(empresa, stagePrincipal);
            });
            menuItemCliente.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarClientesEmpresa(empresa, stagePrincipal);
            });
            menuAsignar.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarAsignaHorarios(empresa);
            });
            menuAsignarExtras.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarAsignaHorasExtras(empresa);
            });
            menuItemHorarios.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarHorarios(empresa, stagePrincipal);
            });
            buttonHoras.setOnMouseEntered((MouseEvent t) -> {
                buttonHoras.setStyle("-fx-background-color: #E0E0E0;");
            });
            buttonHoras.setOnMouseExited((MouseEvent t) -> {
                buttonHoras.setStyle("-fx-background-color: #039BE5;");
            });
        }
        {
            Image imageGuardia = new Image(getClass().getResourceAsStream("imagenes/bt_pagos.png"));
            ImageView imageView = new ImageView(imageGuardia);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            buttonPagos.setGraphic(imageView);
            buttonPagos.setFont(Roboto.MEDIUM(15));
            buttonPagos.getItems().clear();
            MenuItem menuItemQuincenal = new MenuItem("Quincenal");
            buttonPagos.getItems().add(menuItemQuincenal);
            MenuItem menuItemMensual = new MenuItem("Mensual");
            buttonPagos.getItems().add(menuItemMensual);
            MenuItem menuItemVacaciones = new MenuItem("Vacaciones");
            buttonPagos.getItems().add(menuItemVacaciones);
            MenuItem menuItemDecimos = new MenuItem("Decimos");
            buttonPagos.getItems().add(menuItemDecimos);
            menuItemQuincenal.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarPagoQuincenal(empresa, stagePrincipal);
            });
            menuItemMensual.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarPagoMensual(empresa, stagePrincipal);
            });
            menuItemDecimos.setOnAction((ActionEvent actionEvent) -> {
                mostrarDecimos();
            });
            menuItemVacaciones.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarPagoVacaciones(empresa, stagePrincipal);
            });
            buttonPagos.setOnMouseEntered((MouseEvent t) -> {
                buttonPagos.setStyle("-fx-background-color: #E0E0E0;");
            });
            buttonPagos.setOnMouseExited((MouseEvent t) -> {
                buttonPagos.setStyle("-fx-background-color: #039BE5;");
            });
        }
        {
            Image imageGuardia = new Image(getClass().getResourceAsStream("imagenes/bt_roles.png"));
            ImageView imageView = new ImageView(imageGuardia);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            buttonRoles.setGraphic(imageView);
            buttonRoles.setFont(Roboto.MEDIUM(15));
            buttonRoles.getItems().clear();
            MenuItem menuItemCliente = new MenuItem("Cliente");
            buttonRoles.getItems().add(menuItemCliente);
            MenuItem menuItemEmpleado = new MenuItem("Individual");
            buttonRoles.getItems().add(menuItemEmpleado);
            menuItemCliente.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarClientesParaRol(empresa, stagePrincipal);
            });
             menuItemEmpleado.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarEmpleadosParaRol(empresa, stagePrincipal);
            });
            buttonRoles.setOnMouseEntered((MouseEvent t) -> {
                buttonRoles.setStyle("-fx-background-color: #E0E0E0;");
            });
            buttonRoles.setOnMouseExited((MouseEvent t) -> {
                buttonRoles.setStyle("-fx-background-color: #039BE5;");
            });
        }
        {
            Image imageGuardia = new Image(getClass().getResourceAsStream("imagenes/bt_edicion.png"));
            ImageView imageView = new ImageView(imageGuardia);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            buttonEdicion.setGraphic(imageView);
            buttonEdicion.setFont(Roboto.MEDIUM(15));
            buttonEdicion.getItems().clear();
            MenuItem menuItemQuincenal = new MenuItem("Adelanto Quincenal");
            buttonEdicion.getItems().add(menuItemQuincenal);
            MenuItem menuItemSueldo = new MenuItem("Sueldo");
            buttonEdicion.getItems().add(menuItemSueldo);
            MenuItem menuItemDecimos = new MenuItem("Acumulacion Decimos");
            buttonEdicion.getItems().add(menuItemDecimos);
            MenuItem menuItemActuariales = new MenuItem("Actuariales");
            buttonEdicion.getItems().add(menuItemActuariales);
            MenuItem menuItemIess = new MenuItem("IESS Acumulado");
            buttonEdicion.getItems().add(menuItemIess);
            MenuItem menuItemPlantilla = new MenuItem("Planilla IESS");
            buttonEdicion.getItems().add(menuItemPlantilla);
            MenuItem menuItemVacaciones = new MenuItem("Dias de Vacaciones");
            buttonEdicion.getItems().add(menuItemVacaciones);
            MenuItem menuItemBonos = new MenuItem("Bono y Transporte");
            buttonEdicion.getItems().add(menuItemBonos);
            
            menuItemQuincenal.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarQuincenalEmpleados(empresa, stagePrincipal);
            });
            menuItemSueldo.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarSueldoEmpleados(empresa, stagePrincipal);
            });
            menuItemDecimos.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarDecimosEmpleados(empresa, stagePrincipal);
            });
            menuItemActuariales.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarActuarialesEmpleados(empresa, stagePrincipal);
            });
            menuItemIess.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarIessEmpleados(empresa, stagePrincipal);
            });
            menuItemPlantilla.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarPlanillaIess(empresa, stagePrincipal);     
            });
            menuItemVacaciones.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarDiasDerechoVacaciones(empresa, stagePrincipal);
            });
            menuItemBonos.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarClientesParaBonos(empresa, stagePrincipal);     
            });
            buttonEdicion.setOnMouseEntered((MouseEvent t) -> {
                buttonEdicion.setStyle("-fx-background-color: #E0E0E0;");
            });
            buttonEdicion.setOnMouseExited((MouseEvent t) -> {
                buttonEdicion.setStyle("-fx-background-color: #039BE5;");
            });
        }
        {
            Image imageGuardia = new Image(getClass().getResourceAsStream("imagenes/bt_reportes.png"));
            ImageView imageView = new ImageView(imageGuardia);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            buttonReportes.setGraphic(imageView);
            buttonReportes.setFont(Roboto.MEDIUM(15));
            buttonReportes.setOnMouseEntered((MouseEvent t) -> {
                buttonReportes.setStyle("-fx-background-color: #E0E0E0;");
            });
            buttonReportes.setOnMouseExited((MouseEvent t) -> {
                buttonReportes.setStyle("-fx-background-color: #039BE5;");
            });
        }
        {
            Image imageGuardia = new Image(getClass().getResourceAsStream("imagenes/bt_deuda.png"));
            ImageView imageView = new ImageView(imageGuardia);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            buttonDeudas.setGraphic(imageView);
            buttonDeudas.setFont(Roboto.MEDIUM(15));
            buttonDeudas.getItems().clear();
            MenuItem menuItemEmpleado = new MenuItem("Por empleado");
            buttonDeudas.getItems().add(menuItemEmpleado);
            menuItemEmpleado.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarDeudasEmpleados(empresa, stagePrincipal);
            });
            MenuItem menuItemCuenta = new MenuItem("Por cuenta");
            buttonDeudas.getItems().add(menuItemCuenta);
            menuItemCuenta.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarDescuentosTodosEmpleados(empresa, stagePrincipal);
            });
            MenuItem menuItemDescuentos = new MenuItem("Efectuadas");
            buttonDeudas.getItems().add(menuItemDescuentos);
            menuItemDescuentos.setOnAction((ActionEvent actionEvent) -> {
                 aplicacionControl.mostrarDescuentosEmpleados(empresa, stagePrincipal);    
            });
            buttonDeudas.setOnMouseEntered((MouseEvent t) -> {
                buttonDeudas.setStyle("-fx-background-color: #E0E0E0;");
            });
            buttonDeudas.setOnMouseExited((MouseEvent t) -> {
                buttonDeudas.setStyle("-fx-background-color: #039BE5;");
            });
        }
        {
            Image imageGuardia = new Image(getClass().getResourceAsStream("imagenes/bt_variables.png"));
            ImageView imageView = new ImageView(imageGuardia);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            buttonAjustes.setGraphic(imageView);
            buttonAjustes.setFont(Roboto.MEDIUM(15));
            buttonAjustes.getItems().clear();
            MenuItem menuItem1 = new MenuItem("Decimo Tercero");
            buttonAjustes.getItems().add(menuItem1);
            menuItem1.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarAjusteTerceroEmpleados(empresa, stagePrincipal);
            });
            MenuItem menuItem2 = new MenuItem("Decimo Cuarto");
            buttonAjustes.getItems().add(menuItem2);
            menuItem2.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarAjusteCuartoEmpleados(empresa, stagePrincipal);
            });
            MenuItem menuItem3 = new MenuItem("Fondo de Reserva");
            buttonAjustes.getItems().add(menuItem3);
            menuItem3.setOnAction((ActionEvent actionEvent) -> {
                aplicacionControl.mostrarAjusteReservaEmpleados(empresa, stagePrincipal);
            });
            buttonAjustes.setOnMouseEntered((MouseEvent t) -> {
                buttonAjustes.setStyle("-fx-background-color: #E0E0E0;");
            });
            buttonAjustes.setOnMouseExited((MouseEvent t) -> {
                buttonAjustes.setStyle("-fx-background-color: #039BE5;");
            });
        }
        
        buttonAtras.setOnMouseEntered((MouseEvent t) -> {
            buttonAtras.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/atras.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonAtras.setOnMouseExited((MouseEvent t) -> {
            buttonAtras.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/atras.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
    }
    
    public void mostrarDecimos() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_select.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonTercero = new MaterialDesignButton("DECIMO TERCERO");
        Button buttonCuarto = new MaterialDesignButton("DECIMO CUARTO");
        HBox hBox = HBoxBuilder.create()
                .spacing(10.0) //In case you are using HBoxBuilder
                .padding(new Insets(5, 5, 5, 5))
                .alignment(Pos.CENTER)
                .children(buttonTercero, buttonCuarto)
                .build();
        hBox.maxWidth(120);
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Selecione el decimo"), hBox).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        
        buttonTercero.setStyle("-fx-background-color: #039BE5; "
                + "-fx-text-fill: white;");
        buttonTercero.setMinHeight(55);
        buttonTercero.setMaxWidth(170);
        buttonTercero.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            aplicacionControl.mostrarPagoDecimoTercero(empresa, stagePrincipal);
        });
        buttonTercero.setOnMouseEntered((MouseEvent t) -> {
            buttonTercero.setStyle("-fx-background-color: #E0E0E0; "
                    + "-fx-text-fill: white;");
        });
        buttonTercero.setOnMouseExited((MouseEvent t) -> {
            buttonTercero.setStyle("-fx-background-color: #039BE5; "
                    + "-fx-text-fill: white;");
        });
        buttonTercero.setFont(Roboto.MEDIUM(15));
            
        buttonCuarto.setStyle("-fx-background-color: #039BE5; "
                + "-fx-text-fill: white;");
        buttonCuarto.setMinHeight(55);
        buttonCuarto.setMaxWidth(170);
        buttonCuarto.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            aplicacionControl.mostrarPagoDecimoCuarto(empresa, stagePrincipal);
        });
        buttonCuarto.setOnMouseEntered((MouseEvent t) -> {
            buttonCuarto.setStyle("-fx-background-color: #E0E0E0; "
                    + "-fx-text-fill: white;");
        });
        buttonCuarto.setOnMouseExited((MouseEvent t) -> {
            buttonCuarto.setStyle("-fx-background-color: #039BE5; "
                    + "-fx-text-fill: white;");
        });
        buttonCuarto.setFont(Roboto.MEDIUM(15));
        dialogStage.show();
    }

    // obtener dia sin horas
    public Timestamp getToday() throws ParseException {

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date today = new Date();

        Date todayWithZeroTime = formatter.parse(formatter.format(today));

        return new Timestamp(todayWithZeroTime.getTime());
    }

    public Timestamp getTodayWithHora() {
        return new Timestamp(new Date().getTime());
    }

    public static EventHandler<KeyEvent> numFilter() {

        EventHandler<KeyEvent> aux = (KeyEvent keyEvent) -> {
            if (!"0123456789".contains(keyEvent.getCharacter())) {
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
