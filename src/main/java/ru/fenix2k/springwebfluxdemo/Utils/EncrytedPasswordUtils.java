package ru.fenix2k.springwebfluxdemo.Utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Program that encrypt specified string
 */
public class EncrytedPasswordUtils {

  // Encryte Password with BCryptPasswordEncoder
  public static String encrytePassword(String password) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.encode(password);
  }

  public static void main(String[] args) {
    String password = "user";
    String encrytedPassword = encrytePassword(password);

    System.out.println("Encryted Password: " + encrytedPassword);
  }

}
