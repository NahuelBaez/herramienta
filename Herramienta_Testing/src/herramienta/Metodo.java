package herramienta;

import java.util.ArrayList;

public class Metodo {

    private String nombre;
    private String declaracion;
    private String body;
    private ArrayList<Token> operadores;
    private ArrayList<Token> operandos;
    private int fanIn;
    private int fanOut;
    
    public Metodo(String nombre, String declaracion) {
        this.nombre = nombre;
        this.declaracion = declaracion.replaceAll("/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/", "").replaceAll("//.*[\n\r]", "").replaceAll("^\\s*\n", "");
        this.body = "";
        String[] body = declaracion.split("\\n");
        for (int i = 0; i < body.length; i++) {
            if (i != 0 && i != body.length - 1)
                this.body += body[i];
        }
        operadores = new ArrayList<Token>();
        operandos = new ArrayList<Token>();
        operadores.addAll(Lexer.extraerTokens(this.body, Token.OPERADOR));
        operandos.addAll(Lexer.extraerTokens(this.body, Token.OPERANDO));
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getDeclaracion() {
        return declaracion;
    }
    
    public String getBody() {
        return body;
    }
    
    public int getFanIn() {
        return fanIn;
    }

    public void setFanIn(int fanIn) {
        this.fanIn = fanIn;
    }

    public int getFanOut() {
        return fanOut;
    }

    public void setFanOut(int fanOut) {
        this.fanOut = fanOut;
    }

    public int calcularCC() {
        return McCabe.calcularCC(body);
    }
    
    public int calcularLongitud() {
        return Halstead.calcularLongitud(operadores, operandos);
    }
    
    public double calcularVolumen() {
        return Halstead.calcularVolumen(operadores, operandos);
    }
    
    public int contarOperadores() {
        return Halstead.contarTokens(operadores);
    }
    
    public int contarTotalOperadores() {
        return Halstead.contarTotalTokens(operadores);
    }
    
    public int contarOperandos() {
        return Halstead.contarTokens(operandos);
    }
    
    public int contarTotalOperandos() {
        return Halstead.contarTotalTokens(operandos);
    }
    
    @Override
    public String toString() {
        return getNombre();
    }
}
