package com.google.sps.servlets;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {

  public static String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  public static long parseLongOrDefault(String number, long defaultValue) {
    try {
      return Long.parseLong(number);
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  public static String toJson(Object o) {
    return new Gson().toJson(o);
  }
}
