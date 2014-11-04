package fr.minepod.utils;

public class UtilsFunctions {

  /**
   * Random between min and max.
   * 
   * @param min the minimal value
   * @param max the maximal value
   * @return the int
   */
  public static int randomMinMax(int min, int max) {
    return min + (int) (Math.random() * ((max - min) + 1));
  }

  /**
   * Checks if is integer.
   * 
   * @param s the string to check
   * @return true, if is integer
   */
  public static boolean isInteger(String s) {
    try {
      Integer.parseInt(s);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Checks if is double.
   * 
   * @param s the string to check
   * @return true, if is double
   */
  public static boolean isDouble(String s) {
    try {
      Double.parseDouble(s);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Checks if is long.
   * 
   * @param s the string to check
   * @return true, if is long
   */
  public static boolean isLong(String s) {
    try {
      Long.parseLong(s);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Checks if is float.
   * 
   * @param s the string to check
   * @return true, if is float
   */
  public static boolean isFloat(String s) {
    try {
      Float.parseFloat(s);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
