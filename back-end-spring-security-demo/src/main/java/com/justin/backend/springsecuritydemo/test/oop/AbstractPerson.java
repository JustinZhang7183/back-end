package com.justin.backend.springsecuritydemo.test.oop;

public abstract class AbstractPerson {
  public final String PLANET = "Earth";

  protected String name;

  static {
    System.out.println("abstract class can execute static method block");
  }

  public abstract void showName();

  public void showPlanet() {
    System.out.println(PLANET);
  }
}
