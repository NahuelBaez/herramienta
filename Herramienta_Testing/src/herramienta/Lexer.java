package herramienta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

public class Lexer {
    
    private static final  String RGX_ID = "(?<operando>[a-zA-Z_][\\w\\.\\<\\>]*)";
    private static final  String[] RGX_OPERADOR = new String[] {"(?<op>if)", "(?<op>while)", "(?<op>for)", "(?<op>case)", "(?<op>\\|\\|)",
            "(?<op>\\&\\&)", "(?<op>==?)", "(?<op>>=?)", "(?<op><=?)",
            "(?<op>\\+\\+?)", "(?<op>--?)", "(?<op>\\*)", "(?<op>\\/)",
            "(?<op>\\[)", "(?<op>\\])", "(?<op>\\()", "(?<op>\\))", "(?<op>;)", "(?<op>,)",
            "(?<op>\\{)", "(?<op>\\})"};
    private static final  String[] RGX_OPERANDO = new String[] {"=\\s*" + RGX_ID + "\\s*;", "\\[\\s*" + RGX_ID + "\\s*\\]",
            "\\(\\s*" + RGX_ID + "\\s*\\)", RGX_ID + "\\s*==?", "[\\(|\\&\\&|\\|\\|]\\s*" + RGX_ID + "\\s*>=?",
            "[\\(|\\&\\&|\\|\\|]\\s*" + RGX_ID + "\\s*<=?", RGX_ID + "\\s*\\+\\+?", RGX_ID + "\\s*--?",
            RGX_ID + "\\s*\\["};
    private static final  String RGX_METODO = "([a-zA-Z_][\\w\\<\\>]*)";
    
    public static ArrayList<Token> extraerTokens(String str, int tipo) {
        ArrayList<Token> tokens = new ArrayList<Token>();
        String[] regex;
        
        if (tipo == Token.OPERADOR)
            regex = RGX_OPERADOR;
        else if (tipo == Token.OPERANDO)
            regex = RGX_OPERANDO;
        else
            return null; // tipo incorrecto
        
        for (String token : regex) {
            Pattern pat = Pattern.compile(token);
            Matcher mat = pat.matcher(str);
            while(mat.find()) {
                String key;
                if(tipo == Token.OPERANDO) {
                    key = mat.group("operando");
                    if (key.equals("true") || key.equals("false"))
                        continue;
                }
                else // tipo == OPERADOR
                    key = mat.group("op");
                if (key != null) {
                    tokens.add(new Token(tipo, key));
                }
            }
        }
        return tokens;
    }
    
    public static ArrayList<Clase> extraerClases(String codigo) {
        ArrayList<Clase> clases = new ArrayList<Clase>();
        CompilationUnit cu = JavaParser.parse(codigo);
        for (TypeDeclaration<?> type : cu.getTypes()) {
            Clase clase = new Clase(type.getNameAsString(), type.toString());
            for (MethodDeclaration method : type.getMethods()) {
                Metodo metodo = new Metodo(method.getNameAsString(), method.toString());
                clase.addMetodo(metodo);
            }
            clases.add(clase);
        }
        return clases;
    }
    
    public static int contarComentariosSimples(Metodo codigo) {
        int cant_comentarios = 0;
        String regex_comSimple = "//";
        Pattern pat = Pattern.compile(regex_comSimple);
        Matcher mat = pat.matcher(codigo.getBody());
        while(mat.find())
            cant_comentarios++;
        return cant_comentarios;
    }
    
    public static int contarComentariosMulti(Metodo codigo) {
        int cant_comentarios = 0;
        String regex_comMulti = "/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/";
        Pattern pat = Pattern.compile(regex_comMulti);
        Matcher mat = pat.matcher(codigo.getBody());
        while(mat.find())
            cant_comentarios += mat.group().split("\\n").length;
        return cant_comentarios;
    }
    
    public static int contarLineas(String codigo) {
        return codigo.split("\\n").length;
    }
    
    public static int cuentaLineas(Metodo metodo) { 
    	int cont =0;
    	cont=metodo.getBody().split("\\r").length;
    	return cont;

    }
    
    public static int calcularFanIn(ArrayList<Archivo> archivos, Metodo metodo) {
        int contador = 0;
        if (metodo.getNombre().equals("main"))
            return 0;
        String regex = "\\s" + metodo.getNombre() + "\\(";
        for (Archivo archivo : archivos) {
            Pattern pat = Pattern.compile(regex);
            Matcher mat = pat.matcher(archivo.getCodigo());
            while(mat.find())
                contador++;
        }
        return contador - 1;
    }
    
    public static int calcularFanOut(Metodo metodo) {
        int contador = 0;
        String regex = "[\\s.]?" + "(" + RGX_METODO + ")" + "\\(";
        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher(metodo.getBody());
        while(mat.find())
            contador++;
        return contador;
    }
    
}
