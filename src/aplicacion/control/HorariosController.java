/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import static aplicacion.control.DeudasController.numDecimalFilter;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.MaterialDesignButton;
import aplicacion.control.util.MaterialDesignButtonBlue;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.HorarioDAO;
import hibernate.model.Empresa;
import hibernate.model.Horario;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.LocalTimeStringConverter;

/**
 *
 * @author Yornel
 */
public class HorariosController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private Empresa empresa;
    
    @FXML
    private Button buttonAgregar;
    
    @FXML
    private Button buttonAtras;
    
    @FXML 
    private TableColumn lapsoColumna;
    
    @FXML 
    private TableColumn nombreColumna;
    
    @FXML 
    private TableColumn efectivasColumna;
    
    @FXML 
    private TableColumn normalesColumna;
    
    @FXML 
    private TableColumn recargoColumna;
    
    @FXML 
    private TableColumn sobretiempoColumna;
    
    @FXML
    private TableColumn<Horario, Horario>  borrarColumna;
    
    @FXML
    private TableView horariosTableView;
    
    private ObservableList<Horario> data;
    
    ArrayList<Horario> horarios;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void agregarHorario(ActionEvent event) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.TOTAL, 
                    Permisos.Nivel.CREAR)) {
               
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("Nuevo Horario");
                String stageIcon = AplicacionControl.class.getResource("imagenes/icon_editar.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));
                Button buttonConfirmar = new MaterialDesignButtonBlue("Crear");
                TextField fieldNombre = new TextField();
                TextField fieldRC = new TextField();
                TextField fieldST = new TextField();
                Spinner spinnderDe = spinnerDe();
                Spinner spinnerHasta = spinnerHasta();
               
                
                Text textNombre = new Text("Nombre");
                Text textDe = new Text("De (Hora)");
                Text textHasta = new Text("Hasta (Hora)");
                Text textRC = new Text("Horas Recargo");
                Text textST = new Text("Horas Sobretiempo");
                CheckBox medioDia = new CheckBox("Medio dia");
                Text textNormales = new Text();
                textNormales.setText("8 horas normales");
                fieldRC.setText("0");
                fieldST.setText("0");
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(textNombre, fieldNombre, textDe, spinnderDe, textHasta, 
                        spinnerHasta, textRC, fieldRC,
                        textST, fieldST, medioDia, textNormales, buttonConfirmar).
                alignment(Pos.CENTER).padding(new Insets(20)).build()));
                medioDia.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if (newValue) {
                            textNormales.setText("4 horas normales");
                        } else {
                            textNormales.setText("8 horas normales");
                        }
                    }
                });
                fieldRC.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
                fieldST.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
                
                buttonConfirmar.setOnAction((ActionEvent e) -> {
                    
                    Horario horario = new Horario();
                    horario.setNombre(fieldNombre.getText());
                    try {
                        String s1 = spinnderDe.getEditor().getText();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        long ms1 = sdf.parse(s1).getTime();
                        Time timeDe = new Time(ms1);
                        String s2 = spinnerHasta.getEditor().getText();
                        long ms2 = sdf.parse(s2).getTime();
                        Time timeHasta = new Time(ms2);
                        //Time timeDe = Time.valueOf(spinnderDe.getEditor().getText());
                        //Time timeHasta = Time.valueOf(spinnerHasta.getEditor().getText());
                        
                        horario.setEntrada(timeDe);
                        horario.setSalida(timeHasta);
                        if (textNormales.getText().contains("4")) {
                           horario.setNormales(4d); 
                           horario.setMedioDia(true);
                        } else {
                            horario.setNormales(8d);
                            horario.setMedioDia(false);
                        }
                        horario.setRecargo(Double.valueOf(fieldRC.getText()));
                        horario.setSobretiempo(Double.valueOf(fieldST.getText()));
                        horario.setCreacion(new Timestamp(new Date().getTime()));
                        new HorarioDAO().save(horario);
                        setEmpresa(empresa);
                        dialogStage.close();
                    } catch (ParseException ex) {
                        errorHoras();
                    }
                });
                dialogStage.show();
                  
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    public void errorHoras() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        MaterialDesignButton buttonOk = new MaterialDesignButton("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
        children(new Text("Hay un problema en las horas De o Hasta,"), 
                new Text("por favor use el formato adecuado HH:mm."), buttonOk).
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
    
    @FXML
    private void returnEmpresa(ActionEvent event) {
        aplicacionControl.mostrarInEmpresa(empresa);
        stagePrincipal.close();
    } 
    
    public Spinner<LocalTime> spinnerDe() {
        Spinner<LocalTime> spinner = new Spinner<>(new SpinnerValueFactory<LocalTime>() {
            
            {
                setConverter(new LocalTimeStringConverter(DateTimeFormatter
                        .ofPattern("HH:mm"), DateTimeFormatter.ofPattern("HH:mm")));
            }
            
            @Override
            public void decrement(int steps) {
                if (getValue() == null)
                    setValue(LocalTime.of(5, 59));
                else {
                    LocalTime time = (LocalTime) getValue();
                    setValue(time.minusMinutes(steps));
                }
            }

            @Override
            public void increment(int steps) {
                if (this.getValue() == null)
                    setValue(LocalTime.of(6, 1));
                else {
                    LocalTime time = (LocalTime) getValue();
                    setValue(time.plusMinutes(steps));
                }
            }
        });
        spinner.setEditable(true);
        TextField text = spinner.getEditor();
        text.setText("06:00");
        
        return spinner;
    }
    
    public Spinner<LocalTime> spinnerHasta() {
        Spinner<LocalTime> spinner = new Spinner<>(new SpinnerValueFactory<LocalTime>() {
            
            {
                setConverter(new LocalTimeStringConverter(DateTimeFormatter
                        .ofPattern("HH:mm"), DateTimeFormatter.ofPattern("HH:mm")));
            }
            
            @Override
            public void decrement(int steps) {
                if (getValue() == null)
                    setValue(LocalTime.of(13, 59));
                else {
                    LocalTime time = (LocalTime) getValue();
                    setValue(time.minusMinutes(steps));
                }
            }

            @Override
            public void increment(int steps) {
                if (this.getValue() == null)
                    setValue(LocalTime.of(14, 1));
                else {
                    LocalTime time = (LocalTime) getValue();
                    setValue(time.plusMinutes(steps));
                }
            }
        });
        spinner.setEditable(true);
        TextField text = spinner.getEditor();
        text.setText("14:00");
        
        return spinner;
    }
    
    public void deleteHorario(Horario horario) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.ELIMINAR)) {
                
                confirmacionBorrado(horario);
                  
            } else {
                aplicacionControl.noPermitido();
            }
        } 
    }
    
    public void confirmacionBorrado(Horario horario) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        MaterialDesignButtonBlue buttonOk = new MaterialDesignButtonBlue("Si");
        MaterialDesignButtonBlue buttonNo = new MaterialDesignButtonBlue("no");
        HBox hBox = HBoxBuilder.create()
                .spacing(10.0) //In case you are using HBoxBuilder
                .padding(new Insets(5, 5, 5, 5))
                .alignment(Pos.CENTER)
                .children(buttonOk, buttonNo)
                .build();
        hBox.maxWidth(120);
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("¿Seguro que desea borrar este horario?"), hBox).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        buttonOk.setMinWidth(50);
        buttonNo.setMinWidth(50);
        buttonOk.setOnAction((ActionEvent e) -> {
            new HorarioDAO().delete(horario);
            HibernateSessionFactory.getSession().flush();
            data.remove(horario);
            horarios.remove(horario);
            dialogStage.close();
        });
        buttonNo.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        HorarioDAO horarioDAO = new HorarioDAO();
        horarios = new ArrayList<>();
        horarios.addAll(horarioDAO.findAll());
        data = FXCollections.observableArrayList(); 
        data.addAll(horarios);
        horariosTableView.setItems(data);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        horariosTableView.setEditable(Boolean.FALSE);
        
        buttonAgregar.setTooltip(
            new Tooltip("Agregar nuevo empleado")
        );
        buttonAgregar.setOnMouseEntered((MouseEvent t) -> {
            buttonAgregar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/agregar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonAgregar.setOnMouseExited((MouseEvent t) -> {
            buttonAgregar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/agregar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
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
        
        lapsoColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<Horario,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<Horario, String> data) {
                
                String lapso = Fechas.getHora(data.getValue().getEntrada())
                        +" - "+Fechas.getHora(data.getValue().getSalida());
                
                return new ReadOnlyStringWrapper(lapso);
            }
        });
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
       
        efectivasColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<Horario,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<Horario, String> data) {
                
                String lapso = Fechas.differenceBetweenHours(data.getValue()
                        .getEntrada(), data.getValue().getSalida());
                
                return new ReadOnlyStringWrapper(lapso);
            }
        });
        
        normalesColumna.setCellValueFactory(new PropertyValueFactory<>("normales"));
        
        recargoColumna.setCellValueFactory(new PropertyValueFactory<>("recargo"));
        
        sobretiempoColumna.setCellValueFactory(new PropertyValueFactory<>("sobretiempo"));
        
        borrarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        borrarColumna.setCellFactory(param -> new TableCell<Horario, Horario>() {
            private final MaterialDesignButtonBlue deleteButton = new MaterialDesignButtonBlue("Borrar");
            
            @Override
            protected void updateItem(Horario horario, boolean empty) {
                super.updateItem(horario, empty);

                if (horario == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    deleteHorario(horario);
                });
            }
        });
        
        horariosTableView.setRowFactory( (Object tv) -> {
            TableRow<Horario> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    
                }
            });
            return row ;
        });
        
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
