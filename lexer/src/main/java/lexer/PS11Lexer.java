package lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import token.PrintScriptTokenFactory;
import token.Token;
import token.TokenType;

public class PS11Lexer implements Lexer {
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
      token = tokenIdentifier(currentWord, line.getLineNumber(), i);
      if (isNegativeNumber(list)) i = negativeNumberVerification(currentWord, line, i, token, list);
      else if (isGrEqualSmaEqual(currentWord, line, i))
        i = grEqualsmEqualVerification(currentWord, line, i, token);
      else if (isNumber(currentWord)) i = numberVerification(currentWord, line, i, token);
      else if (variableWasDeclared(list)) i = identifierVerification(currentWord, line, i, token);
      else if (isString(currentWord))
        token = Optional.of(PrintScriptTokenFactory.string(line.getLineNumber(), i, currentWord));
      //      else if (isPrint(currentWord)) i = printVerification(currentWord, line, i, token);
      else token = tokenIdentifier(currentWord, line.getLineNumber(), i);
      if (token.isPresent() && i != line.size() - 1) {
        if (line.get(i).matches("[a-zA-Z]") && line.get(i + 1).matches("[a-zA-Z]")) continue;
      }

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

  private boolean isGrEqualSmaEqual(String currentWord, Line line, int i) {
    return (currentWord.equals("<") | currentWord.equals(">") && line.get(i + 1).equals("="));
  }

  private int grEqualsmEqualVerification(
      String currentWord, Line line, int i, Optional<Token> token) {
    currentWord = currentWord + line.get(i + 1);
    token.set(tokenIdentifier(currentWord, line.getLineNumber(), i + 1).get());
    return i + 1;
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
        if (line.get(j).matches("[ ;)*/+-]")) {
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
    token.set(PrintScriptTokenFactory.printlnWithValue(currentWord, line.getLineNumber(), i));
    return i;
  }

  private boolean isString(String currentWord) {
    return currentWord.length() > 1
        && currentWord.charAt(0) == '"'
        && currentWord.charAt(currentWord.length() - 1) == '"';
  }

  private boolean variableWasDeclared(List<Token> list) {
    return !list.isEmpty()
        && (list.get(list.size() - 1).getValue().equals("let")
            | list.get(list.size() - 1).getValue().equals("const"));
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
    // Si no matchea con ning??n token, se fija si esta en el mapa de variables declaradas
    // Si no fu?? declarada de vuelve el empty, si fu?? declarada, devuelve el identifier
    switch (token) {
      case "let":
        return Optional.of(PrintScriptTokenFactory.let(lineNumber, columnNumber));
      case "string":
        return Optional.of(PrintScriptTokenFactory.stringType(lineNumber, columnNumber));
      case "number":
        return Optional.of(PrintScriptTokenFactory.numberType(lineNumber, columnNumber));
      case ">=":
        return Optional.of(PrintScriptTokenFactory.equalOrGreater(lineNumber, columnNumber));
      case "<=":
        return Optional.of(PrintScriptTokenFactory.equalOrSmaller(lineNumber, columnNumber));
      case "=":
        return Optional.of(PrintScriptTokenFactory.assignation(lineNumber, columnNumber));
      case ":":
        return Optional.of(PrintScriptTokenFactory.colon(lineNumber, columnNumber));
      case ";":
        return Optional.of(PrintScriptTokenFactory.semicolon(lineNumber, columnNumber));
      case "-":
        return Optional.of(PrintScriptTokenFactory.substraction(lineNumber, columnNumber));
      case "+":
        return Optional.of(PrintScriptTokenFactory.addition(lineNumber, columnNumber));
      case "/":
        return Optional.of(PrintScriptTokenFactory.division(lineNumber, columnNumber));
      case "*":
        return Optional.of(PrintScriptTokenFactory.multiplication(lineNumber, columnNumber));
      case "println":
      case "printLn":
        return Optional.of(PrintScriptTokenFactory.println(lineNumber, columnNumber));
      case "true":
        return Optional.of(PrintScriptTokenFactory.trueValue(lineNumber, columnNumber));
      case "false":
        return Optional.of(PrintScriptTokenFactory.falseValue(lineNumber, columnNumber));
      case "boolean":
        return Optional.of(PrintScriptTokenFactory.booleanType(lineNumber, columnNumber));
      case ">":
        return Optional.of(PrintScriptTokenFactory.greater(lineNumber, columnNumber));
      case "<":
        return Optional.of(PrintScriptTokenFactory.smaller(lineNumber, columnNumber));
      case "const":
        return Optional.of(PrintScriptTokenFactory.constKeyword(lineNumber, columnNumber));
      case "if":
        return Optional.of(PrintScriptTokenFactory.ifKeyword(lineNumber, columnNumber));
      case "else":
        return Optional.of(PrintScriptTokenFactory.elseKeyword(lineNumber, columnNumber));
      case "(":
        return Optional.of(PrintScriptTokenFactory.openingParenthesis(lineNumber, columnNumber));
      case ")":
        return Optional.of(PrintScriptTokenFactory.closingParenthesis(lineNumber, columnNumber));
      case "{":
        return Optional.of(PrintScriptTokenFactory.openingBrackets(lineNumber, columnNumber));
      case "}":
        return Optional.of(PrintScriptTokenFactory.closingBrackets(lineNumber, columnNumber));
      default:
        return identifiersMap.containsKey(token)
            ? Optional.of(PrintScriptTokenFactory.identifier(token, lineNumber, columnNumber))
            : Optional.empty();
    }
  }
}
