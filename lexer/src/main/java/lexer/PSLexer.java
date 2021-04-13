package lexer;

import token.Token;
import token.PrintScriptTokenFactory;

import java.util.*;

public class PSLexer implements Lexer {
    HashMap<String, Token> identifiersMap;

    @Override
    public List<Token> identifyTokens(List<String> text) {
        List<Token> tokens = new ArrayList<>();
        identifiersMap = new HashMap<>();
        for (int i = 0; i < text.size(); i++) {
            Line currentLine = lineToChars(text.get(i),i);
            tokens.addAll(lineToTokenRefactored(currentLine));
        }
        return tokens;
    }

    private Line lineToChars(String line, int i) {
        List<String> chars = Arrays.asList(line.split("(?!^)"));
        return new Line(chars,i);
    }

    /*private List<Token> lineToToken(Line line) {
        String currentWord = "";
        List<Token> list = new ArrayList<>();
        for (int i = 0; i < line.size(); i++) {
            currentWord += line.get(i);
            if (currentWord.equals(" ")) {
                currentWord = "";
                continue;
            }
            Optional<Token> token = Optional.empty();
            //si detecta un número
            if (currentWord.matches("\\d+")) {
                String number = currentWord;
                for (int j = i + 1; j < line.size(); j++) {
                    if (line.get(j).equals(" ") | line.get(j).equals(";")) {
                        currentWord = number;
                        i = j - 1;
                        break;
                    }
                    number += line.get(j);
                }
                //Si es un integer se devuelve, sino, es que parseó un float y lo devuelve.
                if (isNumber(currentWord)) token = Optional.of(PrintScriptTokenFactory.integer(line.getLineNumber(), i, currentWord));
                else token = Optional.of(PrintScriptTokenFactory.floatingPoint(line.getLineNumber(), i, currentWord));
            }
            //si lo anterior fue un let registra un identifier
            else if (variableWasDeclared(list)) {
                String variableName = "";
                for (int j = i; j < line.size(); j++) {
                    //if (line.get(j).equals(";") | line.get(j).equals(":") | line.get(j).equals(" ")) {
                    if (line.get(j).matches("[:; ]")) {
                        currentWord = variableName;
                        i = j - 1;
                        break;
                    }
                    variableName += line.get(j);
                    i = j;
                }
                token = Optional.of(PrintScriptTokenFactory.identifier(currentWord, line.getLineNumber(), i));
                //Agrega el identifier al Map para futuras referencias.
                identifiersMap.put(currentWord, token.get());
            }
            //si currentWord empieza y termina con "
            else if (isString(currentWord)) {
                token = Optional.of(PrintScriptTokenFactory.string(line.getLineNumber(), i, currentWord));
                //Si ponés "hola"; o "hola" ; es lo mismo, ya no se pierde el ; si no hay un espacio.
            }
            //si no cumple nada va al genérico
            else {
                token = tokenIdentifier(currentWord, line.getLineNumber(), i);
            }
            //si el token existe y es válido, se agrega y reinicia el string
            if (token.isPresent()) {
                list.add(token.get());
                currentWord = "";
            }
        }
        return list;
    }*/

    private List<Token> lineToTokenRefactored(Line line) {
        String currentWord = "";
        List<Token> list = new ArrayList<>();
        for (int i = 0; i < line.size(); i++) {
            currentWord += line.get(i);
            Optional<Token> token = Optional.empty();
            if(currentWord.equals(" ")) {
                currentWord = "";
                continue;
            }
            if(isNumber(currentWord)) i = numberVerification(currentWord,line,i,token);
            else if (variableWasDeclared(list)) i = identifierVerification(currentWord,line,i,token);
            else if (isString(currentWord)) token = Optional.of(PrintScriptTokenFactory.string(line.getLineNumber(), i, currentWord));
            else token = tokenIdentifier(currentWord, line.getLineNumber(), i);

            if (token.isPresent()) {
                list.add(token.get());
                currentWord = "";
            }
        }
        return list;
    }

    private int numberVerification(String currentWord, Line line, int i, Optional<Token> token){
        if (currentWord.matches("\\d+")) {
            StringBuilder number = new StringBuilder(currentWord);
            for (int j = i + 1; j < line.size(); j++) {
                if (line.get(j).equals(" ") | line.get(j).equals(";")) {
                    currentWord = number.toString();
                    i = j - 1;
                    break;
                }
                number.append(line.get(j));
            }
            if (isNumber(currentWord)) token.set(PrintScriptTokenFactory.integer(line.getLineNumber(), i, currentWord));
            else token.set(PrintScriptTokenFactory.floatingPoint(line.getLineNumber(), i, currentWord));
        }
        return i;
    }

    private int identifierVerification(String currentWord, Line line, int i, Optional<Token> token){
            StringBuilder variableName = new StringBuilder();
            for (int j = i; j < line.size(); j++) {
                if (line.get(j).matches("[:; ]")) {
                    currentWord = variableName.toString();
                    i = j - 1;
                    break;
                }
                variableName.append(line.get(j));
                i = j;
            }
            token.set(PrintScriptTokenFactory.identifier(currentWord, line.getLineNumber(), i));
            //Agrega el identifier al Map para futuras referencias.
            identifiersMap.put(currentWord, token.get());
            return i;
    }

    private boolean isString(String currentWord) {
        return currentWord.length() > 1 && currentWord.charAt(0) == '"' && currentWord.charAt(currentWord.length() - 1) == '"';
    }

    private boolean variableWasDeclared(List<Token> list) {
        return !list.isEmpty() && list.get(list.size() - 1).getValue().equals("let");
    }

    private boolean isNumber(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Optional<Token> tokenIdentifier(String token, int lineNumber, int columnNumber) {
        return switch (token) {
            case "let" -> Optional.of(PrintScriptTokenFactory.let(lineNumber, columnNumber));
            case "string" -> Optional.of(PrintScriptTokenFactory.stringType(lineNumber, columnNumber));
            case "number" -> Optional.of(PrintScriptTokenFactory.numberType(lineNumber, columnNumber));
            case "=" -> Optional.of(PrintScriptTokenFactory.assignation(lineNumber, columnNumber));
            case ":" -> Optional.of(PrintScriptTokenFactory.colon(lineNumber, columnNumber));
            case ";" -> Optional.of(PrintScriptTokenFactory.semicolon(lineNumber, columnNumber));
            case "-" -> Optional.of(PrintScriptTokenFactory.substraction(lineNumber, columnNumber));
            case "+" -> Optional.of(PrintScriptTokenFactory.addition(lineNumber, columnNumber));
            case "/" -> Optional.of(PrintScriptTokenFactory.division(lineNumber, columnNumber));
            case "*" -> Optional.of(PrintScriptTokenFactory.multiplication(lineNumber, columnNumber));
            //Si no matchea con ningún token, se fija si esta en el mapa de variables declaradas
            //Si no fué declarada de vuelve el empty, si fué declarada, devuelve el identifier
            default -> identifiersMap.containsKey(token) ? Optional.of(PrintScriptTokenFactory.identifier(token, lineNumber, columnNumber)) : Optional.empty();
        };
    }
}
