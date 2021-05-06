package ASTNode;

public class testclass {
  int test() {
    if (true) return 1;
    return 0;
  }

  int test2() {
    return 4;
  }

  int test3() {
    if (false) return 0;
    else return 1;
  }

  int test5() {
    if (true) return 9;
    return 10;
  }
}
