package token;

public class PrintScriptTokenFactory {

  public static Token let(int ln, int cn) {
    return new Token(TokenType.LET, "let", cn, ln, ln);
  }

  public static Token stringType(int ln, int cn) {
    return new Token(TokenType.STRING_TYPE, "string", cn, ln, ln);
  }

  public static Token numberType(int ln, int cn) {
    return new Token(TokenType.NUMBER_TYPE, "number", cn, ln, ln);
  }

  public static Token integer(int ln, int cn, String value) {
    return new Token(TokenType.INTEGER, value, cn, ln, ln);
  }

  public static Token floatingPoint(int ln, int cn, String value) {
    return new Token(TokenType.FLOATING_POINT, value, cn, ln, ln);
  }

  public static Token assignation(int ln, int cn) {
    return new Token(TokenType.ASSIGNATION, "=", cn, ln, ln);
  }

  public static Token colon(int ln, int cn) {
    return new Token(TokenType.COLON, ":", cn, ln, ln);
  }

  public static Token string(int ln, int cn, String value) {
    return new Token(TokenType.STRING, value, cn, ln, ln);
  }

  public static Token semicolon(int ln, int cn) {
    return new Token(TokenType.SEMICOLON, ";", cn, ln, ln);
  }

  public static Token identifier(String value, int ln, int cn) {
    return new Token(TokenType.IDENTIFIER, value, cn, ln, ln);
  }

  public static Token addition(int ln, int cn) {
    return new Token(TokenType.ADDITION, "+", cn, ln, ln);
  }

  public static Token substraction(int ln, int cn) {
    return new Token(TokenType.SUBSTRACTION, "-", cn, ln, ln);
  }

  public static Token multiplication(int ln, int cn) {
    return new Token(TokenType.MULTIPLICATION, "*", cn, ln, ln);
  }

  public static Token division(int ln, int cn) {
    return new Token(TokenType.DIVISION, "/", cn, ln, ln);
  }

  public static Token println(String value, int ln, int cn) {
    return new Token(TokenType.PRINTLN, value, cn, ln, ln);
  }

  // /PRINTSCRIPT 1.1

  public static Token constType(int ln, int cn) {
    return new Token(TokenType.CONST_TYPE, "const", cn, ln, ln);
  }

  public static Token booleanType(int ln, int cn) {
    return new Token(TokenType.BOOLEAN_TYPE, "boolean", cn, ln, ln);
  }

  public static Token trueValue(int ln, int cn) {
    return new Token(TokenType.TRUE, "true", cn, ln, ln);
  }

  public static Token falseValue(int ln, int cn) {
    return new Token(TokenType.FALSE, "False", cn, ln, ln);
  }

  public static Token greater(int ln, int cn) {
    return new Token(TokenType.GREATER, ">", cn, ln, ln);
  }

  public static Token smaller(int ln, int cn) {
    return new Token(TokenType.SMALLER, "<", cn, ln, ln);
  }

  public static Token equalOrGreater(int ln, int cn) {
    return new Token(TokenType.EQUAL_OR_G, ">=", cn, ln, ln);
  }

  public static Token equalOrSmaller(int ln, int cn) {
    return new Token(TokenType.EQUAL_OR_S, "=<", cn, ln, ln);
  }

  public static Token ifKeyword(int ln, int cn) {
    return new Token(TokenType.IF, "if", cn, ln, ln);
  }

  public static Token elseKeyword(int ln, int cn) {
    return new Token(TokenType.ELSE, "else", cn, ln, ln);
  }


}
