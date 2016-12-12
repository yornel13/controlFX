/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.MaterialDesignButtonBlue;
import aplicacion.control.util.Roboto;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ClienteDAO;
import hibernate.dao.ControlEmpleadoDAO;
import hibernate.dao.HorarioDAO;
import hibernate.dao.RolClienteDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Cliente;
import hibernate.model.ControlEmpleado;
import hibernate.model.Empresa;
import hibernate.model.Horario;
import hibernate.model.RolCliente;
import hibernate.model.Usuario;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class AsignarHorariosController implements Initializable {
    
    private Stage stagePrincipal;
    
    @FXML
    private Button buttonAgregar;
    
    @FXML
    private Button buttonBorrar;
    
    @FXML
    private Button buttonDistribuir;
    
    @FXML
    private Button buttonSave;
    
    @FXML
    private Button buttonEmpleados;
    
    @FXML
    private TableView empleadosTableView;
    
    @FXML
    private DatePicker pickerDe;
    
    @FXML 
    private DatePicker pickerHasta;
    
    @FXML
    private TableColumn empleadoColumna;
    
    @FXML
    private TableColumn<EmpleadoTable, EmpleadoTable>  removeColumna;
    
    @FXML
    private TableView turnosTableView;
    
    @FXML
    private TableColumn turnoColumna;
    
    @FXML
    private TableColumn clienteColumna;
    
    @FXML
    private TableColumn diasColumna;
    
    private ObservableList<EmpleadoTable> data;
    
    private ObservableList<ControlEmpleado> dataTurnos;
    
    private Stage ventanaSelectEmpleado;
    
    ArrayList<Usuario> empleados;
    ArrayList<Usuario> empleadosSelected;
    private Empresa empresa;
    private List<Horario> horarios;
    private List<Cliente> clientes;
    
    private ControlEmpleado ultimoControl;
    private Date inicio;
    private Date fin;
    
    Stage stage;
    Dialog<Void> dialog;
    
    private ArrayList<ControlEmpleado> rangoControls;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        empleados = new ArrayList<>();
        empleadosSelected = new ArrayList<>();
        empleados.addAll(usuarioDAO.findByEmpresaId(empresa.getId()));
        horarios = (List<Horario>) new HorarioDAO().findAll();
        clientes = new ClienteDAO().findAllActivo();
     
    }
    
    void setEmpleado(List<Usuario> usuarios) {
        empleadosSelected.addAll(usuarios);
        empleados.removeAll(usuarios);
        addEmpleadoToData(usuarios);
        ventanaSelectEmpleado = null;
    }
    
    @FXML
    private void onAddTurno(ActionEvent event) throws IOException {
        selecionarHorario(null);
    }
    
    @FXML
    private void onRemoveTurno(ActionEvent event) throws IOException {
        if (!dataTurnos.isEmpty()
                && turnosTableView.getSelectionModel() != null
                && turnosTableView.getSelectionModel().getSelectedIndex() != -1)
            dataTurnos.remove(turnosTableView.getSelectionModel().getSelectedIndex());
        
    }
    
    @FXML
    private void onDistribuir(ActionEvent event) {
        if (!canDistribute()) {  // comprueba si se pueden distribuir los turnos
            return;
        }
        
        final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000; //Milisegundos al día
        long diferenciaLong = (fin.getTime() - inicio.getTime())/MILLSECS_PER_DAY; 
        Integer dias = (int) diferenciaLong; // cantidad que dias que hay en el rango de fechas
        dias++; // se le suma 1 a la cantidad de dias para que se cuente el dia desde el cual comienza
        
        int count = 0; 
        
        empleadosTableView.getColumns().removeAll(empleadosTableView.getColumns()); // se limpia la tabla por si ya habian
        empleadosTableView.getColumns().addAll(removeColumna, empleadoColumna);     // columnas de dias agregadas
        for (EmpleadoTable empleadoTable: data) {                                   // Se borran los turnos anteriores agregados a los empleados
            empleadoTable.setTurnos(new ArrayList<>());
        } 
        
        createArrayControles(dias);
        
        while (dias > 0) {
            
            for (EmpleadoTable empleadoTable: data) {
                ControlEmpleado control = rangoControls.get(count).clone();
                control.setUsuario(empleadoTable.getUsuario());
                empleadoTable.getTurnos().add(control);
            }
            
            addDaysColumns(count);
            dias--;
            count++;
        }
        
    }
    
    public void createArrayControles(Integer diasDelRango) {
        int cantidad = diasDelRango;
        
        ArrayList<ControlEmpleado> controlesCrear = new ArrayList<>();   // control diarios que salen de turnosTableView (no tienen fecha)
        for (ControlEmpleado ce: dataTurnos) {
            Integer dias = Integer.valueOf(ce.getDias());
            while (dias > 0) {
                controlesCrear.add(ce.clone());
                dias--;
            }
        }
                            
        DateTime fecha = new DateTime(inicio.getTime());
        rangoControls = new ArrayList<>();
        int count = 0;
        while (cantidad > 0) {           // se agregan los controles limitados por el rango de fecha
            for (ControlEmpleado ce: controlesCrear) {    // y se le agrega la fecha correspondiente 
                ControlEmpleado control = ce.clone();
                control.setFecha(new Date(fecha.plusDays(count).getMillis()));
                rangoControls.add(control);
                cantidad--;
                count++;
                if (cantidad == 0) {break;}
            }
        } 
    }
    
    @FXML
    private void save(ActionEvent event) {
        if (data.isEmpty() || rangoControls.isEmpty()) {
            dialogCanSave();
        } else {
            ExecutorService executor = Executors.newFixedThreadPool(1);
            Runnable worker = new AsignarHorariosController.DataBaseThread();
            executor.execute(worker);
            executor.shutdown();

            loadingMode();
        }
      
    }
    
    private void loadingMode(){
        dialog = new Dialog<>();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(stage);//stage here is the stage of your webview
        dialog.initStyle(StageStyle.TRANSPARENT);
        Label loader = new Label("   Cargando, por favor espere...");
        loader.setContentDisplay(ContentDisplay.LEFT);
        loader.setGraphic(new ProgressIndicator());
        dialog.getDialogPane().setGraphic(loader);
        dialog.getDialogPane().setStyle("-fx-background-color: #E0E0E0;");
        dialog.getDialogPane().setPrefSize(250, 75);
        DropShadow ds = new DropShadow();
        ds.setOffsetX(1.3); 
        ds.setOffsetY(1.3); 
        ds.setColor(Color.DARKGRAY);
        dialog.getDialogPane().setEffect(ds);
        dialog.show();
    }
    
    public void dialogCanSave() {
        Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("");
            String stageIcon = AplicacionControl.class
                    .getResource("imagenes/icon_error.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new Button("ok");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
            children(new Text("Primero debes agregar horarios a los empleados."), buttonOk).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            buttonOk.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
            buttonOk.setOnKeyPressed((KeyEvent event1) -> {
                dialogStage.close();
            });
            dialogStage.show();
    }
    
    @FXML
    public void onSelectEmpleado(ActionEvent event) throws IOException {
        if (ventanaSelectEmpleado != null) {
            ventanaSelectEmpleado.close();
        }
        
        FXMLLoader loader = new FXMLLoader(AplicacionControl.class
                .getResource("ventanas/VentanaSeleccionarEmpleadoMultiple.fxml"));
        AnchorPane ventanaRolDePago = (AnchorPane) loader.load();
        ventanaSelectEmpleado = new Stage();
        ventanaSelectEmpleado.setTitle("");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/security_dialog.png").toExternalForm();
        ventanaSelectEmpleado.getIcons().add(new Image(stageIcon));
        ventanaSelectEmpleado.setResizable(false);
        ventanaSelectEmpleado.initOwner(stagePrincipal);
        Scene scene = new Scene(ventanaRolDePago);
        ventanaSelectEmpleado.setScene(scene);
        SeleccionarEmpleadoMultipleController controller = loader.getController();
        controller.setStagePrincipal(ventanaSelectEmpleado);
        controller.setParentController(this);
        controller.setUsuarios(empleados);   
        ventanaSelectEmpleado.show();
    }
    
    Boolean canDistribute() {
        if (pickerDe.getValue() == null || pickerHasta.getValue() == null) {
            errorFechas();
            return false;
        }
        if (dataTurnos.size() < 1) {
            errorTurnos();
            return false;
        }
        if (data.size() < 1) {
            errorEmpleados();
            return false;
        }
        inicio = Date.valueOf(pickerDe.getValue());
        fin = Date.valueOf(pickerHasta.getValue());
        
        if (fin.before(inicio)) {
            errorFechas();
            return false;
        }
        for (ControlEmpleado control: dataTurnos) {
            String dias = control.getDias();
            try {
                int diasInt = Integer.valueOf(dias);
                if (diasInt < 1) {
                    errorDiasTurnos();
                    return false;
                }
            } catch (Exception e) {
                errorDiasTurnos();
                return false;
            }
        }
        return true;
    }
    
    void addDaysColumns(int count) {
        DateTime fecha = new DateTime(inicio.getTime());
        fecha = fecha.plusDays(count);
        String day = fecha.toCalendar(Locale.getDefault())
                    .getDisplayName(Calendar .DAY_OF_WEEK, Calendar.LONG, 
                            Locale.getDefault());
        TableColumn columna = new TableColumn(
                day+" "+fecha.getDayOfMonth()+"/"+fecha.getMonthOfYear());
        columna.setPrefWidth(120);
        columna.setStyle("-fx-alignment: center;");
        columna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<EmpleadoTable,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<EmpleadoTable, String> data) {
                
                if (data.getValue().getTurnos() == null) 
                    return null;
                
                return new ReadOnlyStringWrapper(getTextHorario(data.getValue()
                        .getTurnos().get(count)));
            }
        });
        empleadosTableView.getColumns().add(columna);
    }
    
    public void errorFechas() {
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new MaterialDesignButtonBlue("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
        children(new Text("Rango de fechas incorrecto."), 
                buttonOk).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        dialogStage.show();
        buttonOk.setMaxWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
    }
    
    public void errorTurnos() {
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new MaterialDesignButtonBlue("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
        children(new Text("Debe agregar al menos 1 turno."), 
                buttonOk).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        dialogStage.show();
        buttonOk.setMaxWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
    }
    
    public void errorDiasTurnos() {
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new MaterialDesignButtonBlue("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
        children(new Text("A uno o mas de los turnos no se le selecciono dias."), 
                buttonOk).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        dialogStage.show();
        buttonOk.setMaxWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
    }
    
    public void existenRolesCliente() {
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new MaterialDesignButtonBlue("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
        children(new Text("Uno o mas empleados ya tiene rol cliente creado en este rango de fechas."), 
                buttonOk).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        dialogStage.show();
        buttonOk.setMaxWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
    }
    
    public void dialogoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Horarios guardados con exito."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        dialogStage.show();
    }
    
    public void errorEmpleados() {
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new MaterialDesignButtonBlue("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
        children(new Text("Debe agregar al menos 1 empleado."), 
                buttonOk).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        dialogStage.show();
        buttonOk.setMaxWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
            empleadosTableView.setEditable(Boolean.FALSE);
            turnosTableView.setEditable(Boolean.TRUE);
            
            setTableInf();
            
            data = FXCollections.observableArrayList(); 
            empleadosTableView.setItems(data);
            dataTurnos = FXCollections.observableArrayList(); 
            turnosTableView.setItems(dataTurnos);
            
        {
            Image imageGuardia = new Image(getClass().getResourceAsStream("imagenes/bt_guardia.png"));
            ImageView imageView = new ImageView(imageGuardia);
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);
            buttonEmpleados.setGraphic(imageView);
            buttonEmpleados.setFont(Roboto.MEDIUM(15));
            buttonEmpleados.setOnMouseEntered((MouseEvent t) -> {
                buttonEmpleados.setStyle("-fx-background-color: #0390E5;");
            });
            buttonEmpleados.setOnMouseExited((MouseEvent t) -> {
                buttonEmpleados.setStyle("-fx-background-color: #E0E0E0;");
            });
        }
       
        {
            buttonAgregar.setFont(Roboto.MEDIUM(12));
            buttonAgregar.setOnMouseEntered((MouseEvent t) -> {
                buttonAgregar.setStyle("-fx-background-color: #0390E5;");
            });
            buttonAgregar.setOnMouseExited((MouseEvent t) -> {
                buttonAgregar.setStyle("-fx-background-color: #E0E0E0;");
            });
        }
        {
            buttonBorrar.setFont(Roboto.MEDIUM(12));
            buttonBorrar.setOnMouseEntered((MouseEvent t) -> {
                buttonBorrar.setStyle("-fx-background-color: #0390E5;");
            });
            buttonBorrar.setOnMouseExited((MouseEvent t) -> {
                buttonBorrar.setStyle("-fx-background-color: #E0E0E0;");
            });
        }
        {
            buttonDistribuir.setFont(Roboto.MEDIUM(12));
            buttonDistribuir.setOnMouseEntered((MouseEvent t) -> {
                buttonDistribuir.setStyle("-fx-background-color: #0390E5;");
            });
            buttonDistribuir.setOnMouseExited((MouseEvent t) -> {
                buttonDistribuir.setStyle("-fx-background-color: #E0E0E0;");
            });
        }
        {
            Image imageGuardia = new Image(getClass().getResourceAsStream("imagenes/save_blue.png"));
            ImageView imageView = new ImageView(imageGuardia);
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            buttonSave.setGraphic(imageView);
            buttonSave.setFont(Roboto.MEDIUM(13));
            buttonSave.setOnMouseEntered((MouseEvent t) -> {
                buttonSave.setStyle("-fx-background-color: #0390E5;");
            });
            buttonSave.setOnMouseExited((MouseEvent t) -> {
                buttonSave.setStyle("-fx-background-color: #ef5350;");
            });
        }
    } 

    private void setTableInf() {
        
        empleadoColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<EmpleadoTable,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<EmpleadoTable, String> data) {
                return new ReadOnlyStringWrapper(data.getValue().getApellido() 
                        + " " + data.getValue().getNombre());
            }
        });
        
        removeColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        removeColumna.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            private final Button deleteButton = new Button("Borrar");

            @Override
            protected void updateItem(EmpleadoTable empleadoTable, boolean empty) {
                super.updateItem(empleadoTable, empty);

                if (empleadoTable == null) {
                    setGraphic(null);
                    getTableRow().setStyle("");
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    removeEmpleado(empleadoTable);
                });
                
                deleteButton.setText("x");
            }

            
        });
        
        turnoColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<ControlEmpleado, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<ControlEmpleado, String> data) {
                
                return new ReadOnlyStringWrapper(getTextHorario(data.getValue()));
            }
        });
        
        clienteColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<ControlEmpleado, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<ControlEmpleado, String> data) {
                
                if(data.getValue().getCliente() == null) 
                    return  null;
                
                return new ReadOnlyStringWrapper(data.getValue().getCliente().getNombre());
            }
        });
        
        diasColumna.setCellValueFactory(new PropertyValueFactory<>("dias"));
        diasColumna.setCellFactory(TextFieldTableCell.forTableColumn());
        diasColumna.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<ControlEmpleado, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<ControlEmpleado, String> t) {
                    Integer newValue;
                    try {
                        newValue = Integer.valueOf(t.getNewValue());
                        if (newValue <= 0 || newValue > 31) {
                            newValue = Integer.valueOf(t.getOldValue());
                            //dialogoErrorCuotas();
                        }
                    } catch (NumberFormatException e) {
                        newValue = Integer.valueOf(t.getOldValue());
                        //dialogoErrorCuotas();
                    }
                    ControlEmpleado controlEmpleado = ((ControlEmpleado) t.getTableView().getItems()
                            .get(t.getTablePosition().getRow()));     
                    
                    controlEmpleado.setDias(newValue.toString());
                    dataTurnos.set(dataTurnos.indexOf(controlEmpleado), controlEmpleado);
                }
            }
        );
    }
    
    public String getTextHorario(ControlEmpleado controlEmpleado) {
        
        if (controlEmpleado.getCaso().equals(Const.LIBRE)) {
            return "Libre";
        } else if (controlEmpleado.getCaso().equals(Const.FALTA)) {
            return "Falta";
        } else if (controlEmpleado.getCaso().equals(Const.VACACIONES)) {
            return "Vacaciones";
        } else if (controlEmpleado.getCaso().equals(Const.PERMISO)) {
            return "Permiso";
        } else if (controlEmpleado.getCaso().equals(Const.DM)) {
            return "D. Medico";
        } else if (controlEmpleado.getCaso().equals(Const.CM)) {
            return "C. Medica";
        } else {
           return getLapso(controlEmpleado.getEntrada(), controlEmpleado.getSalida());
        }
    }

    public String getLapso(Time entrada, Time salida) {
        
        String lapso = Fechas.getHora(entrada)
                        +" - "+Fechas.getHora(salida);
        
        return lapso;
    }
    
    void selecionarHorario(ControlEmpleado controlEmpleado) throws IOException {
        FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaHorarioEmpleadoCliente.fxml"));
        AnchorPane ventanaRolDePago = (AnchorPane) loader.load();
        Stage ventana = new Stage();
        ventana.setTitle("Seleccionar turno");
        String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
        ventana.getIcons().add(new Image(stageIcon));
        ventana.setResizable(false);
        ventana.initOwner(stagePrincipal);
        Scene scene = new Scene(ventanaRolDePago);
        ventana.setScene(scene);
        HorarioEmpleadoClienteController controller = loader.getController();
        controller.setStagePrincipal(ventana);
        controller.setAsignarHorarioController(this);
        controller.setStage(ventana);
        controller.setEmpleado(horarios, clientes, controlEmpleado);
        ventana.show();
    }
    
    private void removeEmpleado(EmpleadoTable empleadoTable) {
        empleados.add(empleadoTable.getUsuario());
        empleadosSelected.remove(empleadoTable.getUsuario());
        data.remove(empleadoTable);
    }

    private void addEmpleadoToData(List<Usuario> usuarios) {
        
        usuarios.stream().map((user) -> {
            EmpleadoTable empleado = new EmpleadoTable();
            empleado.setId(user.getId());
            empleado.setNombre(user.getNombre());
            empleado.setApellido(user.getApellido());
            empleado.setCedula(user.getCedula());
            empleado.setEmpresa(user.getDetallesEmpleado()
                    .getEmpresa().getNombre());
            empleado.setTelefono(user.getTelefono());
            empleado.setDepartamento(user.getDetallesEmpleado()
                    .getDepartamento().getNombre());
            empleado.setCargo(user.getDetallesEmpleado()
                    .getCargo().getNombre());
            empleado.setActivo(user.getActivo());
            empleado.setUsuario(user);
            if (rangoControls != null) {
                empleado.setTurnos(new ArrayList<>());
                for (ControlEmpleado controlEmpleado: rangoControls) {
                    ControlEmpleado control = controlEmpleado.clone();
                    control.setUsuario(empleado.getUsuario());
                    empleado.getTurnos().add(control);
                }
            }
            return empleado;
        }).forEach((empleado) -> {
            data.add(empleado);
        });
    }

    void setHorarioToTurnos(ControlEmpleado controlEmpleado) {
        dataTurnos.add(controlEmpleado);
    }

    public class DataBaseThread implements Runnable {

        public DataBaseThread(){
        }

        @Override
        public void run() {
    
            new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        cancel();
                        save();
                    }

             }, 1000, 1000); 
        }
        
        private void save() {
            ArrayList<Usuario> usuariosConRol = new ArrayList<>();
            for (int i=0; i < (rangoControls.size()); i++) {
                Date fechaAConsultar = new Date((new DateTime(inicio
                        .getTime()).plusDays(i)).getMillis());
                RolClienteDAO rolClienteDAO = new RolClienteDAO();
                System.out.println(fechaAConsultar.toString());
                List<RolCliente> rolClientes;
                rolClientes = rolClienteDAO.findAllByEntreFechaAndEmpresaId(
                        fechaAConsultar, empresa.getId());
                if (!rolClientes.isEmpty()) {
                    for (RolCliente rol: rolClientes) {
                        for (EmpleadoTable usuario: data) {
                            if (rol.getUsuario().getId() 
                                    .equals(usuario.getId())) {
                                
                                boolean agregar = true;
                                for (Usuario user: usuariosConRol) {
                                    if (user.getId().equals(usuario.getId())) {
                                        agregar = false;
                                    }
                                }
                                if (agregar) {
                                    usuariosConRol.add(usuario.getUsuario());
                                }
                            }
                        }
                    }
                }
            }
            System.out.println(usuariosConRol.size());
            
            if (!usuariosConRol.isEmpty()) {
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        closeDialogMode();
                        existenRolesCliente();
                    }
                }); 
            } else {
                
                ArrayList<ControlEmpleado> controlsEncontrados = new ArrayList<>();   // turnos que deben borrarse
                for (EmpleadoTable usuario: data) {            // buscando registro de turnos ya creados a cada empleados
                    controlsEncontrados.addAll(new ControlEmpleadoDAO()
                            .findAllByEmpleadoIdInDeterminateTime(usuario.getId()
                                    ,inicio , fin));
                }
                System.out.println(controlsEncontrados.size());
                
                for (EmpleadoTable usuario: data) {
                    for (ControlEmpleado control: usuario.getTurnos()) {
                        new ControlEmpleadoDAO().save(control);         // guardando los turnos
                    }
                }
                
                for (ControlEmpleado controlEmpleadoDelete: controlsEncontrados) {    // borrando turnos viejos
                    new ControlEmpleadoDAO().delete(controlEmpleadoDelete);
                }
                HibernateSessionFactory.getSession().flush();
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        closeDialogMode();
                        dialogoCompletado();
                    }
                });
            }
        }
    }
    
    public void closeDialogMode() {
        if (dialog != null) {
           Stage toClose = (Stage) dialog.getDialogPane()
                   .getScene().getWindow();
           toClose.close();
           dialog.close();
           dialog = null;
        }
    }
}
