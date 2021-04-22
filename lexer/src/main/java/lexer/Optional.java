package lexer;

import java.util.NoSuchElementException;
import java.util.Objects;

public class Optional<T> {
  private T value;

  private Optional(T value) {
    this.value = value;
  }

  public static <T> Optional<T> of(T value) {
    return new Optional<>(Objects.requireNonNull(value));
  }

  public static <T> Optional<T> empty() {
    return new Optional<>(null);
  }

  public T get() {
    if (value == null) {
      throw new NoSuchElementException("No value present");
    }
    return value;
  }

  public boolean isPresent() {
    return value != null;
  }

  public void set(T value) {
    this.value = value;
  }
}
