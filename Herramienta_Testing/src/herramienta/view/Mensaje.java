package herramienta.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class Mensaje {
    
    private String titulo, contenido, detalles;
    private AlertType atype;

    public Mensaje(String titulo, String contenido, String detalles, AlertType atype) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.detalles = detalles;
        this.atype = atype;
    }
    
    public Mensaje(String titulo, String contenido, AlertType atype) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.atype = atype;
    }
    
    public void show() {
        Alert alert = new Alert(atype);
        alert.setTitle(titulo);
        alert.setContentText(contenido);
        alert.setResizable(false);
        if (detalles != null) {
            TextArea area_detalles = new TextArea(detalles);
            area_detalles.setEditable(false);
            GridPane expandable = new GridPane();
            expandable.add(area_detalles, 0, 0);
            alert.getDialogPane().setExpandableContent(expandable);
        }
        alert.showAndWait();
    }
    
}
