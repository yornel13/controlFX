/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteDescuentosVarios;
import aplicacion.control.tableModel.DescuentoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import hibernate.model.Empresa;
import hibernate.model.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import static aplicacion.control.util.Fechas.getFechaConMes;
import aplicacion.control.util.Numeros;
import static aplicacion.control.util.Numeros.round;
import hibernate.dao.CuotaDeudaDAO;
import hibernate.dao.DeudaTipoDAO;
import hibernate.model.CuotaDeuda;
import hibernate.model.DeudaTipo;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import net.sf.jasperreports.engine.JREmptyDataSource;
import static aplicacion.control.util.Fechas.getFechaConMes;
import static aplicacion.control.util.Numeros.round;
import java.util.Collection;

/**
 *
 * @author Yornel
 */
public class DescuentosTodosEmpleadosController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private TableView empleadosTableView;
    
    @FXML
    private TextField filterField;
    
    @FXML 
    private TableColumn cedulaColumna;
    
    @FXML 
    private TableColumn nombreColumna;
    
    @FXML 
    private TableColumn tipoColumna;
    
    @FXML 
    private TableColumn valorColumna;
    
    @FXML 
    private TableColumn estadoColumna;
    
    @FXML
    private TableColumn<DescuentoTable, DescuentoTable> marcarColumna;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private Button buttonAnterior;
    
    @FXML
    private Button buttonSiguiente;
    
    @FXML
    private Label totalText;
    
    private ObservableList<DescuentoTable> data;
    
    ArrayList<Usuario> usuarios;
    private Empresa empresa;
    
    @FXML
    private ChoiceBox selectorDiaDe;
    
    @FXML
    private ChoiceBox selectorMesDe;
    
    @FXML
    private ChoiceBox selectorAnoDe;
    
    @FXML
    private ChoiceBox selectorDiaHa;
    
    @FXML
    private ChoiceBox selectorMesHa;
    
    @FXML
    private ChoiceBox selectorAnoHa;
    
    @FXML
    private ChoiceBox choiceBoxCuenta;
    
    ArrayList<DeudaTipo> tiposDeudas;
    
    List<CuotaDeuda> cuotasDeuda;
        
    List<DescuentoTable> descuentos;
    
    private Fecha inicio;
    private Fecha fin;
    
    Stage dialogLoading;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void returnEmpresa(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarInEmpresa(empresa);
    } 
    
    @FXML
    public void onClickMore(ActionEvent event) {
        inicio = inicio.plusMonths(1);
        fin = fin.plusMonths(1);
        
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa);  
        setTableInfo();
    }
    
    @FXML
    public void onClickLess(ActionEvent event) {
        inicio = inicio.minusMonths(1);
        fin = fin.minusMonths(1);
        
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa); 
        setTableInfo();
    }
    
    public void dialogWait() {
        dialogLoading = new Stage();
        dialogLoading.initModality(Modality.APPLICATION_MODAL);
        dialogLoading.setResizable(false);
        dialogLoading.setTitle("Cargando...");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_loading.png").toExternalForm();
        dialogLoading.getIcons().add(new Image(stageIcon));
        dialogLoading.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Cargando espere...")).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogLoading.show();
    }
    
    public void imprimir(File file) {
        
        dialogWait();
        
        ReporteDescuentosVarios datasource = new ReporteDescuentosVarios();
        
        
        List<DescuentoTable> descuentosToPrint = new ArrayList<>();
        String tipo = "";
        int count = 0;
        Double total = 0d;
        int fullCount = 0;
        Double fullTotal = 0d;
        
        /**
         * For organize array descuentos
         */
        HashMap<String, List<DescuentoTable>> descuentosHash = new HashMap<>();
        
        for (DescuentoTable descuentoTable: (List<DescuentoTable>) 
                empleadosTableView.getItems()) {
            
            String tipoDeudaNombre = descuentoTable.getTipoDeuda().getNombre();
            if (!descuentosHash.containsKey(tipoDeudaNombre)) {
                List<DescuentoTable> list = new ArrayList<>();
                list.add(descuentoTable);

                descuentosHash.put(tipoDeudaNombre, list);
            } else {
                descuentosHash.get(tipoDeudaNombre).add(descuentoTable);
            }
        }
        
        List<DescuentoTable> descuentosArray = new ArrayList<>();
        for (Map.Entry<String, List<DescuentoTable>> descuentosList: 
                descuentosHash.entrySet()) {
            
            for (DescuentoTable descuentoTable: descuentosList.getValue()) {
                descuentosArray.add(descuentoTable);
            }
        }
        /**
         * For organize array descuentos
         */
        
        for (DescuentoTable descuentoTable: descuentosArray) {
            
            fullCount ++;
            fullTotal += descuentoTable.getValor();
            
            if (tipo.equalsIgnoreCase(descuentoTable.getTipo())) {
                count++;
                total += descuentoTable.getValor();
            } else {
                if (count > 0) {
                    DescuentoTable title = new DescuentoTable();
                    title.setNombres("TOTAL POR CUENTA:     "+count);
                    title.setCedula("");
                    title.setValor(total);
                    descuentosToPrint.add(title);
                    
                    DescuentoTable espacio = new DescuentoTable();
                    espacio.setNombres("");
                    espacio.setCedula("");
                    descuentosToPrint.add(espacio);
                    count = 0;
                    total = 0d;
                }
                count++;
                total += descuentoTable.getValor();
                
                DescuentoTable title = new DescuentoTable();
                title.setNombres(descuentoTable.getTipo());
                title.setCedula("");
                descuentosToPrint.add(title);
            }
            tipo = descuentoTable.getTipo();
            
            descuentosToPrint.add(descuentoTable);
        }
        
        if (descuentosToPrint.size() > 1) {
            DescuentoTable title = new DescuentoTable();
            title.setNombres("TOTAL POR CUENTA:     "+count);
            title.setCedula("");
            title.setValor(total);
            descuentosToPrint.add(title);
        }
        
        datasource.addAll(descuentosToPrint);
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_DESCUENTOS_EMPLEADOS);
        
            Map<String, Object> parametros = new HashMap();
            parametros.put("empresa", empresa.getNombre());
            parametros.put("siglas", empresa.getSiglas());
            parametros.put("correo", "Correo: " + empresa.getEmail());
            parametros.put("detalles", 
                         "Ruc: " + empresa.getNumeracion() 
                    + " - Direccion: " + empresa.getDireccion() 
                    + " - Tel: " + empresa.getTelefono1());
            parametros.put("lapso", getFechaConMes(inicio)  + " al " + getFechaConMes(fin));
            parametros.put("total", round(fullTotal).toString());
            parametros.put("desc", String.valueOf(fullCount));
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint;
            if (descuentosToPrint.isEmpty())
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
            else
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "descuentos_" + System.currentTimeMillis();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            
            // Registro para auditar
            String detalles = "genero el recibo de descuentos mensual de todos los empleado";
            aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
            
            dialogoCompletado();
            
            
        } catch (JRException | IOException ex) {
            Logger.getLogger(PagosTotalEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dialogLoading.close();
        }
    }
    
    public void dialogoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Imprimir");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Impresión de descuentos completada!!"), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    public File seleccionarDirectorio() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Selecciona un directorio para guardar el recibo");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));    
        return fileChooser.showDialog(stagePrincipal);
    }
    
    @FXML
    public void dialogoImprimir(ActionEvent event) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Imprimir");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Seleccionar ruta");
        Button buttonNoDocumento = new Button("Salir");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Seleccione la ruta de guardado"), 
                buttonSiDocumento, buttonNoDocumento).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonSiDocumento.setOnAction((ActionEvent e) -> {
            File file = seleccionarDirectorio();
            if (file != null) {
                dialogStage.close();
                imprimir(file);
            }
        });
        buttonNoDocumento.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    public void setEmpresa(Empresa empresa) throws ParseException {
        this.empresa = empresa; 
        
        inicio = Fechas.getFechaActual();
        inicio.setDia("01");
        fin = inicio.plusMonths(1).minusDays(1);
           
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa);
       
        setTableInfo();
    }
    
    public void setTableInfo() {
        
        
        data = FXCollections.observableArrayList(); 
        
        cuotasDeuda = new CuotaDeudaDAO().findAllByFecha(fin.getFecha());
        
        descuentos = new ArrayList<>(); 
                
        filterCuenta();

        callFilter();

    }
    
    void callFilter() {
        FilteredList<DescuentoTable> filteredData = new FilteredList<>(data, p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(empleado -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (empleado.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (empleado.getApellido().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (empleado.getCedula().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (empleado.getTipo().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } 
                return false; // Does not match.
            });
        });
        
        SortedList<DescuentoTable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(empleadosTableView.comparatorProperty());
        empleadosTableView.setItems(sortedData);
        chequearFiltro(filteredData);
        sumarTotal();
        
        empleadosTableView.getItems().addListener(new ListChangeListener<DescuentoTable>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends DescuentoTable> c) {
                sumarTotal();
            }
        });
    }
    
    void filterCuenta() {
        
        data = FXCollections.observableArrayList(); 
        
        boolean all = choiceBoxCuenta.getSelectionModel().getSelectedIndex() == 0;
        
        for (CuotaDeuda cuotaDeuda: cuotasDeuda) {
            if (cuotaDeuda.getDeuda().getUsuario().getDetallesEmpleado()
                    .getEmpresa().getId().equals(empresa.getId())) {
                
                DescuentoTable descuento = new DescuentoTable();
                descuento.setNombre(cuotaDeuda.getDeuda().getUsuario().getNombre());
                descuento.setApellido(cuotaDeuda.getDeuda().getUsuario().getApellido());
                descuento.setNombres(cuotaDeuda.getDeuda().getUsuario().getApellido()
                        +" "+cuotaDeuda.getDeuda().getUsuario().getNombre());
                descuento.setCedula(cuotaDeuda.getDeuda().getUsuario().getCedula());
                descuento.setEmpleado(cuotaDeuda.getDeuda().getUsuario());
                descuento.setValor(cuotaDeuda.getMonto());
                descuento.setTipo(cuotaDeuda.getDeuda().getTipoDeuda().getNombre());
                descuento.setTipoDeuda(cuotaDeuda.getDeuda().getTipoDeuda());
                if (cuotaDeuda.getPagoMes() == null)
                    descuento.setEstado("Pendiente");
                else
                    descuento.setEstado("Pagado");
                descuento.setPagoMes(cuotaDeuda.getPagoMes());
            
                if (all)
                    data.add(descuento); 
                else {
                    DeudaTipo deudaTipo = tiposDeudas.get(choiceBoxCuenta
                            .getSelectionModel().getSelectedIndex()-1);
                    if (deudaTipo.getId().equals(descuento.getTipoDeuda().getId())) {
                        data.add(descuento); 
                    }
                }
            }
        }
        empleadosTableView.setItems(data);
        callFilter();
    }
    
    
    void chequearFiltro(FilteredList<DescuentoTable> filteredData) {
        filteredData.setPredicate(empleado -> {
            // If filter text is empty, display all persons.
            if (filterField.getText() == null || filterField.getText().isEmpty()) {
                return true;
            }
            // Compare first name and last name of every person with filter text.
            String lowerCaseFilter = filterField.getText().toLowerCase();

            if (empleado.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (empleado.getApellido().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (empleado.getCedula().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches cedula.
                } else if (empleado.getEmpleado().getDetallesEmpleado().getCargo()
                        .getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches cargo.
                } else if (empleado.getEmpleado().getDetallesEmpleado().getDepartamento()
                        .getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches departamento.
                } 
            return false; // Does not match.
        });
    }
    
    void sumarTotal() {
        System.out.println("calculando total deudas");
        Double monto = 0d;
        
         for (DescuentoTable descuentoTable: (List<DescuentoTable>) 
                    empleadosTableView.getItems()) {
                monto += descuentoTable.getValor();
        }
        
        totalText.setText("Total:  $"+Numeros.round(monto).toString());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        
        System.out.println("Descuentos todos los los empleados");
        
        empleadosTableView.setEditable(Boolean.FALSE);
        
        cedulaColumna.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombres"));
       
        tipoColumna.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        
        valorColumna.setCellValueFactory(new PropertyValueFactory<>("valor"));
        
        estadoColumna.setCellValueFactory(new PropertyValueFactory<>("estado"));
        
        marcarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        marcarColumna.setCellFactory(param -> new TableCell<DescuentoTable, DescuentoTable>() {

            @Override
            protected void updateItem(DescuentoTable descuentoTable, boolean empty) {
                super.updateItem(descuentoTable, empty);

                if (descuentoTable == null) {
                    setGraphic(null);
                    getTableRow().setStyle("");
                    return;
                }
                
                if (descuentoTable.getPagoMes() != null) {
                    getTableRow().setStyle("-fx-background-color:#A5D6A7");
                } else {
                    getTableRow().setStyle("");
                }
            } 
        });
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<DescuentoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    DescuentoTable rowData = row.getItem();
                    // nothing to do
                }
            });
            return row;
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
        buttonImprimir.setTooltip(
            new Tooltip("Imprimir")
        );
        buttonImprimir.setOnMouseEntered((MouseEvent t) -> {
            buttonImprimir.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/imprimir.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonImprimir.setOnMouseExited((MouseEvent t) -> {
            buttonImprimir.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/imprimir.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        buttonAnterior.setTooltip(
            new Tooltip("Mes Anterior")
        );
        buttonAnterior.setOnMouseEntered((MouseEvent t) -> {
            buttonAnterior.setStyle("-fx-background-color: #29B6F6;");
        });
        buttonAnterior.setOnMouseExited((MouseEvent t) -> {
            buttonAnterior.setStyle("-fx-background-color: #039BE5;");
        });
        buttonSiguiente.setTooltip(
            new Tooltip("Mes Siguiente")
        );
        buttonSiguiente.setOnMouseEntered((MouseEvent t) -> {
            buttonSiguiente.setStyle("-fx-background-color: #29B6F6;");
        });
        buttonSiguiente.setOnMouseExited((MouseEvent t) -> {
            buttonSiguiente.setStyle("-fx-background-color: #039BE5;");
        });
        
        selectorDiaDe.setItems(Fechas.arraySpinnerDia());
        selectorMesDe.setItems(Fechas.arraySpinnerMes());
        selectorAnoDe.setItems(Fechas.arraySpinnerAno());
        selectorDiaHa.setItems(Fechas.arraySpinnerDia());
        selectorMesHa.setItems(Fechas.arraySpinnerMes());
        selectorAnoHa.setItems(Fechas.arraySpinnerAno());
        
        selectorDiaDe.setDisable(true);
        selectorMesDe.setDisable(true);
        selectorAnoDe.setDisable(true);
        selectorDiaHa.setDisable(true);
        selectorMesHa.setDisable(true);
        selectorAnoHa.setDisable(true);
        
        tiposDeudas = (ArrayList<DeudaTipo>) new DeudaTipoDAO().findAll();
        String[] itemsTposDeuda = new String[tiposDeudas.size()+1];
        itemsTposDeuda[0] = "-> TODOS <-";
        tiposDeudas.stream().forEach((obj) -> {
            itemsTposDeuda[tiposDeudas.indexOf(obj)+1] = obj.getNombre();
        });
        choiceBoxCuenta.setItems(FXCollections.observableArrayList(itemsTposDeuda)); 
        choiceBoxCuenta.getSelectionModel().selectFirst();
        choiceBoxCuenta.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, 
                    Number oldValue, Number newNalue) {
                filterCuenta();
            }
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
