package herramienta;

import java.util.ArrayList;

public class Halstead {

    public static int contarTokens(ArrayList<Token> tokens) {
        ArrayList<String> tokens_unicos = new ArrayList<String>();
        for (Token token : tokens) {
            if (!tokens_unicos.contains(token.getValor()))
                tokens_unicos.add(token.getValor());
        }
        return tokens_unicos.size();
    }
    
    public static int contarTotalTokens(ArrayList<Token> tokens) {
        return tokens.size();
    }
    
    public static int calcularLongitud(ArrayList<Token> operadores, ArrayList<Token> operandos) {
        return contarTotalTokens(operadores) + contarTotalTokens(operandos);
    }
    
    public static double calcularVolumen(ArrayList<Token> operadores, ArrayList<Token> operandos) {
        double n = (double)(contarTokens(operadores) + contarTokens(operandos));
        return calcularLongitud(operadores, operandos) * Math.sqrt(n);
    }
    
}
