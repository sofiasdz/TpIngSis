package lexer;

import java.util.List;

public class Line {
    private List<String> line;
    private int lineNumber;

    public Line(List<String> line, int lineNumber) {
        this.line = line;
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int size(){
        return line.size();
    }

    public String get(int n){
        return line.get(n);
    }
}
