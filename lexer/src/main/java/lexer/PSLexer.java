package lexer;

import token.Token;

import java.util.*;

public class PSLexer implements Lexer {
    HashMap<String,Token> identifiersMap;

    @Override
    public List<Token> identifyTokens(List<String> text) {
        List<Token> tokens = new ArrayList<>();
        identifiersMap = new HashMap<>();
        for (int i = 0; i < text.size(); i++) {
            //line to char
            List<String> currentLine = lineToChars(text.get(i));
            tokens.addAll(lineToTokenRefactored(currentLine, i));
        }
        return tokens;
    }


    private List<String> lineToChars(String line) {
        List<String> chars = Arrays.asList(line.split("(?!^)"));
        return chars;
    }

    private List<Token> lineToToken(List<String> line, int lineNumber) {
        String currentWord = "";
        List<Token> list = new ArrayList<>();
        for (int i = 0; i <= line.size(); i++) {
            Optional<Token> token;
            if (currentWord.matches("\\d+")) {
                //el regex es de  combinaciones de integer
                String intNumber = "";
                for (int j = i; j < line.size(); j++) {
                    if (line.get(j).equals(";")) {
                        currentWord = intNumber;
                        i = j-1; //Lo mismo que en los de abajo.
                        break;
                    } else if (line.get(j).equals(" ")){
                        currentWord = intNumber;
                        i=j;
                        break;
                    }
                    intNumber += line.get(j);
                    i = j;
                }
                if (currentWord.matches("\\d+")) {
                    //regex de float
                    token = Optional.of(Token.integer(lineNumber, i, currentWord));
                } else  {
                    token = Optional.of(Token.floatingPoint(lineNumber, i, currentWord));
                }

            } else if (!list.isEmpty() && list.get(list.size() - 1).getValue().equals("let")) {
                String variableName = "";
                for (int j = i; j < line.size(); j++) {
                    if (line.get(j).equals(";") | line.get(j).equals(":")) {
                        currentWord = variableName;
                        i = j-1; //Si hay un ; o : pegado queremos contemplarlo, por eso el -1 para no pisarlo y perderlo
                        break;
                    }
                    else if(line.get(j).equals(" ")){
                        currentWord = variableName;
                        i = j; //Si hay un espacio no nos importa, y lo pisamos con el j para no cagar ningun token
                        break;
                    }
                    variableName += line.get(j);
                    i = j;
                }
                token = Optional.of(Token.identifier(currentWord, lineNumber, i));
                identifiersMap.put(currentWord,token.get());
            } else if (currentWord.length() > 1 && currentWord.charAt(0) == '"' && currentWord.charAt(currentWord.length() - 1) == '"') {
                token = Optional.of(Token.string(lineNumber,i, currentWord));
                //No sabemos porque, pero si después del "String" no hay un espacio antes del ;, se pierde el ;
                //Por ejemplo "Sofi cute"; se guarda solo el "Sofi cute" y no el Semicolon
                //Si pones "Sofi cute" ; se guardan ambos.
            } else {
                if(i==line.size()) token = tokenIdentifier(currentWord, lineNumber, i-1);
                //La vuelta extra que le dabamos hacía que si el último (Osea, i<=Size)
                //token no tenía un espacio onda "number = 2.04;" en vez de "2.04 ;"
                //ese ; token tenía su columnNumber desfazado en 1, por eso el -1.
                else token = tokenIdentifier(currentWord, lineNumber, i);
            }
            if (token.isPresent()) {
                list.add(token.get());
                currentWord = "";
            }else if(i!=line.size())currentWord += line.get(i);
            //Después hay que fixear el orden de la ronda, porque si no perdemos un caracter y por eso hicimos esta negrada
        }

        return list;
    }

    private List<Token> lineToTokenRefactored(List<String> line, int lineNumber) {
        String currentWord = "";
        List<Token> list = new ArrayList<>();
        for (int i = 0; i < line.size(); i++) {
            currentWord+= line.get(i);
            if(currentWord.equals(" ")){
                currentWord ="";
                continue;
            }
            Optional<Token> token = Optional.empty();
            //si detecta un número
            if(currentWord.matches("\\d+")){
                String number = currentWord;
                for (int j = i+1; j <line.size() ; j++) {
                    if(line.get(j).equals(" ") | line.get(j).equals(";")){
                        currentWord = number;
                        i = j-1;
                        break;
                    }
                   number += line.get(j);
                }
                //Si es un integer se devuelve, sino, es que parseó un float y lo devuelve.
                if(integerParse(currentWord)) token = Optional.of(Token.integer(lineNumber, i,currentWord));
                else token = Optional.of(Token.floatingPoint(lineNumber,i,currentWord));
            }
            //si lo anterior fue un let registra un identifier
            else if (variableWasDeclared(list)) {
                String variableName = "";
                for (int j = i; j < line.size(); j++) {
                    //if (line.get(j).equals(";") | line.get(j).equals(":") | line.get(j).equals(" ")) {
                    if (line.get(j).matches("[:; ]")){
                        currentWord = variableName;
                        i = j-1;
                        break;
                    }
                    variableName += line.get(j);
                    i = j;
                }
                token = Optional.of(Token.identifier(currentWord, lineNumber, i));
                //Agrega el identifier al Map para futuras referencias.
                identifiersMap.put(currentWord,token.get());
            }
            //si currentWord empieza y termina con "
            else if (isString(currentWord)) {
                token = Optional.of(Token.string(lineNumber,i, currentWord));
                //Si ponés "hola"; o "hola" ; es lo mismo, ya no se pierde el ; si no hay un espacio.
            }
            //si no cumple nada va al genérico
            else {
                token = tokenIdentifier(currentWord, lineNumber, i);
            }
            //si el token existe y es válido, se agrega y reinicia el string
            if (token.isPresent()) {
                list.add(token.get());
                currentWord = "";
            }
        }
        return list;
    }

    private boolean isString(String currentWord) {
        return currentWord.length() > 1 && currentWord.charAt(0) == '"' && currentWord.charAt(currentWord.length() - 1) == '"';
    }

    private boolean variableWasDeclared(List<Token> list) {
        return !list.isEmpty() && list.get(list.size() - 1).getValue().equals("let");
    }

    private boolean integerParse(String string){
        try{
            Integer.parseInt(string);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    private Optional<Token> tokenIdentifier(String token, int lineNumber, int columnNumber) {
        return switch (token) {
            case "let" -> Optional.of(Token.let(lineNumber, columnNumber));
            case "string" -> Optional.of(Token.stringType(lineNumber, columnNumber));
            case "number" -> Optional.of(Token.numberType(lineNumber, columnNumber));
            case "=" -> Optional.of(Token.assignation(lineNumber, columnNumber));
            case ":" -> Optional.of(Token.colon(lineNumber, columnNumber));
            case ";" -> Optional.of(Token.semicolon(lineNumber, columnNumber));
            case "-" -> Optional.of(Token.substraction(lineNumber, columnNumber));
            case "+" -> Optional.of(Token.addition(lineNumber, columnNumber));
            case "/" -> Optional.of(Token.division(lineNumber, columnNumber));
            case "*" -> Optional.of(Token.multiplication(lineNumber, columnNumber));
            //Si no matchea con ningún token, se fija si esta en el mapa de variables declaradas
            //Si no fué declarada de vuelve el empty, si fué declarada, devuelve el identifier
            default -> identifiersMap.containsKey(token)? Optional.of(Token.identifier(token, lineNumber, columnNumber)) : Optional.empty();
        };
    }
}
