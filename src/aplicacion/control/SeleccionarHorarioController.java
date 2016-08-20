/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.model.Cliente;
import hibernate.model.Horario;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author Yornel
 */
public class SeleccionarHorarioController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
   
    @FXML
    private TableView horariosTableView;
    
    @FXML
    private TextField filterField;
    
    @FXML 
    private TableColumn lapsoColumna;
    
    @FXML 
    private TableColumn nombreColumna;
    
    @FXML 
    private TableColumn normalesColumna;
    
    @FXML 
    private TableColumn recargoColumna;
    
    @FXML 
    private TableColumn sobretiempoColumna;
    
    private ObservableList<Horario> data;
    
    List<Horario> horarios;
    
    private HorarioEmpleadoController horarioEmpleadoController;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void setHorarioEmpleadoController(HorarioEmpleadoController 
            horarioEmpleadoController) {
        this.horarioEmpleadoController = horarioEmpleadoController;
    }
  
    private void returnHorario(Horario horario) {
        stagePrincipal.close();
        horarioEmpleadoController.setHorario(horario);
    }
    
    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
        
        data = FXCollections.observableArrayList(); 
        
        if (!horarios.isEmpty()) {
            data.addAll(horarios);
        }
                
        horariosTableView.setItems(data);
        
        FilteredList<Horario> filteredData = new FilteredList<>(data, p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(horario -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (horario.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (getLapso(horario).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        
        SortedList<Horario> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(horariosTableView.comparatorProperty());
        horariosTableView.setItems(sortedData);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        horariosTableView.setEditable(Boolean.FALSE);
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
       
        lapsoColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<Horario,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<Horario, String> data) {
                
                String lapso;
                
                if (data.getValue().getHoraInicio() == 0) {
                    lapso = "12am";
                } else if (data.getValue().getHoraInicio() < 12) {
                    lapso = data.getValue().getHoraInicio() + "am";
                } else if (data.getValue().getHoraInicio() == 12) {
                    lapso = "12pm";
                } else {
                    lapso = (data.getValue().getHoraInicio() - 12) + "pm";
                }
                
                if (data.getValue().getHoraFin()== 0) {
                    lapso = lapso + "-" + "12am";
                } else if (data.getValue().getHoraFin() < 12) {
                    lapso = lapso + "-" + data.getValue().getHoraFin() + "am";
                } else if (data.getValue().getHoraFin() == 12) {
                    lapso = lapso + "-" + "12pm";
                } else {
                    lapso = lapso + "-" + (data.getValue().getHoraFin() - 12) + "pm";
                }
                
                return new ReadOnlyStringWrapper(lapso);
            }
        });
        
        normalesColumna.setCellValueFactory(new PropertyValueFactory<>("normales"));
        
        recargoColumna.setCellValueFactory(new PropertyValueFactory<>("recargo"));
        
        sobretiempoColumna.setCellValueFactory(new PropertyValueFactory<>("sobretiempo"));
        
        horariosTableView.setRowFactory( (Object tv) -> {
            TableRow<Horario> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    returnHorario(row.getItem());
                }
            });
            return row ;
        });
    } 
    
    public String getLapso(Horario horario) {
        String lapso;
                
        if (horario.getHoraInicio() == 0) {
            lapso = "12am";
        } else if (horario.getHoraInicio() < 12) {
            lapso = horario.getHoraInicio() + "am";
        } else if (horario.getHoraInicio() == 12) {
            lapso = "12pm";
        } else {
            lapso = (horario.getHoraInicio() - 12) + "pm";
        }

        if (horario.getHoraFin()== 0) {
            lapso = lapso + "-" + "12am";
        } else if (horario.getHoraFin() < 12) {
            lapso = lapso + "-" + horario.getHoraFin() + "am";
        } else if (horario.getHoraFin() == 12) {
            lapso = lapso + "-" + "12pm";
        } else {
            lapso = lapso + "-" + (horario.getHoraFin() - 12) + "pm";
        }
        
        return lapso;
    }
    
    public static Timestamp getToday() throws ParseException {
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date today = new Date();

        Date todayWithZeroTime = formatter.parse(formatter.format(today));
        
        return new Timestamp(todayWithZeroTime.getTime());
    }
    
    public static String getMonthName(int month){
        Calendar cal = Calendar.getInstance();
        // Calendar numbers months from 0
        cal.set(Calendar.MONTH, month - 1);
        return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
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
