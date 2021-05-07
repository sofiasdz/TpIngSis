package lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import token.PrintScriptTokenFactory;
import token.Token;
import token.TokenType;

public class PSLexer implements Lexer {
  HashMap<String, Token> identifiersMap;

  @Override
  public List<Token> identifyTokens(List<String> text) {
    List<Token> tokens = new ArrayList<>();
    identifiersMap = new HashMap<>();
    for (int i = 0; i < text.size(); i++) {
      Line currentLine = lineToChars(text.get(i), i);
      tokens.addAll(lineToToken(currentLine));
    }
    return tokens;
  }

  private Line lineToChars(String line, int i) {
    List<String> chars = Arrays.asList(line.split("(?!^)"));
    return new Line(chars, i);
  }

  private List<Token> lineToToken(Line line) {
    String currentWord = "";
    List<Token> list = new ArrayList<>();
    for (int i = 0; i < line.size(); i++) {
      currentWord += line.get(i);
      Optional<Token> token = Optional.empty();
      if (currentWord.equals(" ")) {
        currentWord = "";
        continue;
      }
      if (isNegativeNumber(list)) i = negativeNumberVerification(currentWord, line, i, token, list);
      else if (isNumber(currentWord)) i = numberVerification(currentWord, line, i, token);
      else if (variableWasDeclared(list)) i = identifierVerification(currentWord, line, i, token);
      else if (isString(currentWord))
        token = Optional.of(PrintScriptTokenFactory.string(line.getLineNumber(), i, currentWord));
      else if (isPrint(currentWord)) i = printVerification(currentWord, line, i, token);
      else token = tokenIdentifier(currentWord, line.getLineNumber(), i);

      if (token.isPresent()) {
        list.add(token.get());
        currentWord = "";
      }
    }
    if (!currentWord.isEmpty())
      throw new RuntimeException(
          "Error at line "
              + line.getLineNumber()
              + ": I couldn't proccess that one. Did you correctly declare all variables?");
    return list;
  }

  private int negativeNumberVerification(
      String currentWord, Line line, int i, Optional<Token> token, List<Token> list) {
    if (currentWord.matches("\\d+")) {
      list.remove(list.size() - 1);
      StringBuilder number = new StringBuilder(currentWord);
      for (int j = i + 1; j < line.size(); j++) {
        if (line.get(j).equals(" ") | line.get(j).equals(";")) {
          currentWord = number.toString();
          i = j - 1;
          break;
        }
        number.append(line.get(j));
      }
      if (isNumber(currentWord))
        token.set(PrintScriptTokenFactory.integer(line.getLineNumber(), i, "-" + currentWord));
      else
        token.set(
            PrintScriptTokenFactory.floatingPoint(line.getLineNumber(), i, "-" + currentWord));
    }
    return i;
  }

  private boolean isNegativeNumber(List<Token> list) {
    return (list.size() > 1
        && list.get(list.size() - 1).getType().equals(TokenType.SUBSTRACTION)
        && list.get(list.size() - 2).getType().equals(TokenType.ASSIGNATION));
  }

  private int numberVerification(String currentWord, Line line, int i, Optional<Token> token) {
    if (currentWord.matches("\\d+")) {
      StringBuilder number = new StringBuilder(currentWord);
      for (int j = i + 1; j < line.size(); j++) {
        // if (line.get(j).equals(" ") | line.get(j).equals(";")) {
        String regex = "[+\\-/* ;]";
        if (line.get(j).matches(regex)) {
          currentWord = number.toString();
          i = j - 1;
          break;
        }
        number.append(line.get(j));
      }
      if (isNumber(currentWord))
        token.set(PrintScriptTokenFactory.integer(line.getLineNumber(), i, currentWord));
      else token.set(PrintScriptTokenFactory.floatingPoint(line.getLineNumber(), i, currentWord));
    }
    return i;
  }

  private int identifierVerification(String currentWord, Line line, int i, Optional<Token> token) {
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
    // Agrega el identifier al Map para futuras referencias.
    identifiersMap.put(currentWord, token.get());
    return i;
  }

  private boolean isPrint(String currentWord) {
    return currentWord.equals("printLn(") || currentWord.equals("println(");
  }

  private int printVerification(String currentWord, Line line, int i, Optional<Token> token) {
    StringBuilder variableName = new StringBuilder();
    for (int j = i + 1; j < line.size(); j++) {
      if (line.get(j).matches("[)]")) {
        currentWord = variableName.toString();
        i = j;
        break;
      }
      variableName.append(line.get(j));
      i = j;
    }
    token.set(PrintScriptTokenFactory.println(currentWord, line.getLineNumber(), i));
    return i;
  }

  private boolean isString(String currentWord) {
    return currentWord.length() > 1
        && currentWord.charAt(0) == '"'
        && currentWord.charAt(currentWord.length() - 1) == '"';
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
        // Si no matchea con ningún token, se fija si esta en el mapa de variables declaradas
        // Si no fué declarada de vuelve el empty, si fué declarada, devuelve el identifier
      default -> identifiersMap.containsKey(token)
          ? Optional.of(PrintScriptTokenFactory.identifier(token, lineNumber, columnNumber))
          : Optional.empty();
    };
  }
}
