package com.justin.backend.springsecuritydemo.test.oop.suboop;

import com.justin.backend.springsecuritydemo.test.oop.ChinesePerson;

public class DefaultTest extends ChinesePerson{

  public static String outerValue;

  private static String privateOuterValue;

  private static void privateMethod() {

  }

  public void visitInnerValue() {
    System.out.println(new InnerClass().innerValue);
  }

  public void showProtectedValue() {
    System.out.println(protectedValue);
  }

  public void showDefaultValue() {
//    System.out.println(defaultValue);
  }

  public static void main(String[] args) {
    ChinesePerson person = new ChinesePerson();
//    String defaultValue = person.defaultValue;
  }

  public static class InnerClass{
    public String innerValue;

    public final void visitOuterValue() {
      System.out.println(outerValue);
      System.out.println(privateOuterValue);
      privateMethod();
    }
  }
}
