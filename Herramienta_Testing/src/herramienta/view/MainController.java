package herramienta.view;


import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

import herramienta.Archivo;
import herramienta.Clase;
import herramienta.Lexer;
import herramienta.Main;
import herramienta.Metodo;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;

public class MainController {
    
    private File carpeta_actual;
    private ArrayList<Archivo> archivos;
    @FXML private TextArea area_codigo;
    
    // Listas:
    @FXML private ListView<Archivo> lista_archivos;
    @FXML private ListView<Clase> lista_clases;
    @FXML private ListView<Metodo> lista_metodos;
    
    // Labels:
    @FXML private Label lineas_totales;
    @FXML private Label porcentaje_comentarios;
    @FXML private Label fan_in;
    @FXML private Label fan_out;
    @FXML private Label complejidad;
    @FXML private Label hal_longitud;
    @FXML private Label hal_volumen;
    @FXML private Label operadores_unicos;
    @FXML private Label operadores_totales;
    @FXML private Label operandos_unicos;
    @FXML private Label operandos_totales;
    
    @FXML
    private void initialize() {
        reestablecerValoresArchivo();
        reestablecerValores();
        
        lista_archivos.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent event) {
                reestablecerValoresArchivo();
                reestablecerValores();
                Archivo archivo_seleccionado = lista_archivos.getSelectionModel().getSelectedItem();
                if (archivo_seleccionado != null) {
                    ObservableList<Clase> lista = FXCollections.observableArrayList(archivo_seleccionado.getClases());
                    lista_clases.setItems(lista);
                    area_codigo.setText(archivo_seleccionado.getCodigo());
                    int lineas = archivo_seleccionado.contarLineas();
                    lineas_totales.setText(String.valueOf(lineas));
//                    int comentarios = archivo_seleccionado.contarComentarios();
//                    double porcentaje = (double)comentarios/(double)lineas*100.0;
//                    porcentaje_comentarios.setText(String.format("%.2f", porcentaje) + "%");
//                    if (porcentaje > 15)
//                        porcentaje_comentarios.setTextFill(Color.LIMEGREEN);
//                    else
//                        porcentaje_comentarios.setTextFill(Color.INDIANRED);
                        
                }
            };
        });
        
        lista_clases.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent event) {
                reestablecerValores();
                Clase clase_seleccionada = lista_clases.getSelectionModel().getSelectedItem();
                if (clase_seleccionada != null) {
                    ObservableList<Metodo> lista = FXCollections.observableArrayList(clase_seleccionada.getMetodos());
                    lista_metodos.setItems(lista);
                    area_codigo.setText(clase_seleccionada.getDeclaracion());
                }
            };
        });
        
        lista_metodos.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent event) {
                reestablecerValores();
                Metodo metodo_seleccionado = lista_metodos.getSelectionModel().getSelectedItem();
                if (metodo_seleccionado != null) {
                    area_codigo.setText(metodo_seleccionado.getDeclaracion());
                    fan_in.setText(String.valueOf(Lexer.calcularFanIn(archivos, metodo_seleccionado)));
                    fan_out.setText(String.valueOf(Lexer.calcularFanOut(metodo_seleccionado)));
                    int cc = metodo_seleccionado.calcularCC();
                    complejidad.setText(String.valueOf(cc));
                    if (cc >= 10)
                        complejidad.setTextFill(Color.RED);
                    else if (cc >= 8)
                        complejidad.setTextFill(Color.INDIANRED);
                    else
                        complejidad.setTextFill(Color.LIMEGREEN);
                    hal_longitud.setText(String.valueOf(metodo_seleccionado.calcularLongitud()));
                    hal_volumen.setText(String.format("%.2f", metodo_seleccionado.calcularVolumen()));
                    operadores_unicos.setText(String.valueOf(metodo_seleccionado.contarOperadores()));
                    operadores_totales.setText(String.valueOf(metodo_seleccionado.contarTotalOperadores()));
                    operandos_unicos.setText(String.valueOf(metodo_seleccionado.contarOperandos()));
                    operandos_totales.setText(String.valueOf(metodo_seleccionado.contarTotalOperandos()));
                }
            };
        });
    }
    
    public void exit(ActionEvent event) {
        Platform.exit();
    }
    
    public void buscarCarpeta(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        carpeta_actual = directoryChooser.showDialog(Main.getStage());
        cargarCarpeta(null);
    }
    
    public void cargarCarpeta(ActionEvent event) {
        reestablecerListas();
        reestablecerValores();
        reestablecerValoresArchivo();
        archivos = new ArrayList<Archivo>();
        if(carpeta_actual != null){
            for (File f : carpeta_actual.listFiles()) {
                if (f.isFile() && FilenameUtils.getExtension(f.getAbsolutePath()).equals("java")) {
                    try {
                        Archivo archivo = new Archivo(f.getAbsolutePath(), StandardCharsets.UTF_8);
                        archivos.add(archivo);
                    } catch (Exception e) {
                        new Mensaje("No se pudo cargar el archivo", "Error de sintaxis", e.getMessage(), AlertType.ERROR).show();;
                        return;
                    }
                }
            }
            if (archivos.isEmpty()) {
                new Mensaje("No se encontraron archivos",
                        "La carpeta seleccionada debe contener al menos un archivo con extensi�n .java", AlertType.ERROR);
            }
            ObservableList<Archivo> lista = FXCollections.observableArrayList(archivos);
            lista_archivos.setItems(lista);
        }
    }

    public void reestablecerValoresArchivo() {
        area_codigo.setText("");
        lineas_totales.setText("-");
        porcentaje_comentarios.setText("-");
        porcentaje_comentarios.setTextFill(Color.WHITE);
        lista_metodos.setItems(null);
    }
    
    public void reestablecerValores() {
        area_codigo.setText("");
        fan_in.setText("-");
        fan_out.setText("-");
        complejidad.setText("-");
        hal_longitud.setText("-");
        hal_volumen.setText("-");
        operadores_unicos.setText("-");
        operadores_totales.setText("-");
        operandos_unicos.setText("-");
        operandos_totales.setText("-");
    }
    
    public void reestablecerListas() {
        lista_archivos.setItems(null);
        lista_clases.setItems(null);
        lista_metodos.setItems(null);
    }
    
}
