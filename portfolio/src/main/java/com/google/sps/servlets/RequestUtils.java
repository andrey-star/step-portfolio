package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
import javax.servlet.http.HttpServletRequest;

public class RequestUtils {

  /**
   * Retrieves the specified parameter from the provided request.
   *
   * @param request      the request
   * @param name         the name of the parameter to be retrieved
   * @param defaultValue the value to return, if the parameter with provided {@code name} is not
   *                     present
   * @return the value of the parameter, or {@code defaultValue} if no parameter with provided
   *         name is present.
   */
  public static String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  /**
   * Parses a long number form the provided string.
   *
   * @param number       a string representing the number
   * @param defaultValue the value to return if parsing fails
   * @return the parsed long number, or {@code defaultValue} if {@link NumberFormatException} is
   *         thrown during parsing.
   */
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

  /**
   * Returns a string with information about the provided request.
   * The string contains request's method ana host.
   *
   * @param request the request
   * @return a string with information about the request.
   */
  public static String getRequestInfo(HttpServletRequest request) {
    return String.format("Received %s request from %s:%s",
            request.getMethod(),
            request.getServerName(),
            request.getServerPort());
  }

  /**
   * Retrieves the username of the user with the provided id.
   *
   * @param id the id of the user
   * @param datastore the user database
   * @return the username or an empty string, if a user with th provided id was not found.
   */
  public static String getCurrentUsername(String id, DatastoreService datastore) {
    Query query = new Query("UserInfo")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      return "";
    }
    return (String) entity.getProperty("username");
  }
}
