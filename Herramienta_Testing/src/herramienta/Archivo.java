package herramienta;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Archivo {

    private Path ruta;
    private String nombre;
    private String codigo;
    private ArrayList<Clase> clases;
    
    public Archivo(String path, Charset encoding) {
        this.ruta = Paths.get(path);
        this.nombre = ruta.getFileName().toString();
        try {
            this.codigo = new String(Files.readAllBytes(ruta), encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.clases = Lexer.extraerClases(codigo);
    }
    
    public Path getRuta() {
        return ruta;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void addClase(Clase clase) {
        clases.add(clase);
    }
    
    public ArrayList<Clase> getClases() {
        return clases;
    }

    @Override
    public String toString() {
        return getNombre();
    }
    
   
    public int contarLineas() {
        return Lexer.contarLineas(codigo);
    }
    
}
