package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
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

  public static String getRequestInfo(HttpServletRequest request) {
    return String.format("Received %s request from %s:%s", request.getMethod(), request.getServerName(), request.getServerPort());
  }

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
