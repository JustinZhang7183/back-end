package com.justin.backend.springsecuritydemo.test.oop;

public interface IAction {
  public static final String actionVersion = "v1.0";

  public abstract void play();

  default void showVersion() {
    System.out.println(actionVersion);
  }

}
