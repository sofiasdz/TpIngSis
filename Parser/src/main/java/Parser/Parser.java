package Parser;

import java.util.List;

public interface Parser {
    String validate(String path);

    String validate(List<String> list);

    List<String> execute(String path);

    List<String> execute(List<String> list);

    DebugResult debug(String path);

    DebugResult debug(List<String> list);
}
