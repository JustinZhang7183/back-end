package com.justin.backend.springsecuritydemo.test.oop;

import com.justin.backend.springsecuritydemo.test.oop.suboop.DefaultTest;

public class OOPTest extends DefaultTest.InnerClass{

  static {
    class innerclass {
      public String a;
    }
  }

  public final String a;

  public OOPTest() {
    final String b = "";
//    b = "";
    this.a = "";
//    this.a = "";
  }

  public final void visitOuterValue(String a) {
    String suba = "";
    class localInnerClass {
      private String a;
      private static String b;

      public void test() {
        System.out.println(a);
        System.out.println(suba);
      }
    }
  }

  public static void main(String[] args) {
    AbstractPerson person = new ChinesePerson();
    person.showPlanet();
  }
}
