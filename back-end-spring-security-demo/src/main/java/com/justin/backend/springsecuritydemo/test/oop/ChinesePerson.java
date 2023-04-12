package com.justin.backend.springsecuritydemo.test.oop;

public class ChinesePerson extends AbstractNormalPerson implements IAction{
  String defaultValue = "default";

  protected String protectedValue = "protected";

  @Override
  public void showName() {
    System.out.println("chinese person");
  }

  @Override
  public void play() {
    System.out.println("playing");
  }

  /**
   * final method can't override
   */
//  public void showPlanet() {
//    System.out.println("custom planet");
//  }

  public void showPlanetBySelf() {
    System.out.println(PLANET);
  }
}
