package herramienta;

import java.util.ArrayList;

public class Clase {
    
    private String nombre;
    private String declaracion;
    private ArrayList<Metodo> metodos;
    
    public Clase(String nombre, String declaracion) {
        this.nombre = nombre;
        this.declaracion = declaracion;
        this.metodos = new ArrayList<Metodo>();
    }
    
    public String getNombre() {
        return nombre;
    }

    public String getDeclaracion() {
        return declaracion;
    }
    
    public void addMetodo(Metodo metodo) {
        metodos.add(metodo);
    }
    
    public ArrayList<Metodo> getMetodos() {
        return metodos;
    }
    
    public int contarComentarios(Metodo metodo) {
        return Lexer.contarComentariosSimples(metodo) + Lexer.contarComentariosMulti(metodo);
    }
    
    
    @Override
    public String toString() {
        return getNombre();
    }
}
