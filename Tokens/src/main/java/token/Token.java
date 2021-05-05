package token;

import java.util.Objects;

public class Token {
  TokenType type;
  String value;
  int startingColumn;
  int endingColumn;
  int startingLine;
  int endingLine;

  Token(TokenType type, String value, int endingColumn, int startingLine, int endingLine) {
    this.type = type;
    this.value = value;
    this.startingColumn = endingColumn - value.length() + 1;
    this.endingColumn = endingColumn;
    this.startingLine = startingLine;
    this.endingLine = endingLine;
  }

  public String getValue() {
    return value;
  }

  public TokenType getType() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Token token = (Token) o;
    return startingColumn == token.startingColumn && endingColumn == token.endingColumn
        && startingLine == token.startingLine && endingLine == token.endingLine
        && type == token.type && value.equals(token.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, value, startingColumn, endingColumn, startingLine, endingLine);
  }

  @Override
  public String toString() {
    return value;
  }
}
