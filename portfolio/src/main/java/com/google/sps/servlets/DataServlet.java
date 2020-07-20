package com.google.sps.servlets;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private final List<String> comments = new ArrayList<>();
  private static final Logger logger = LogManager.getLogger(com.google.sps.servlets.DataServlet.class.getName());

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    logger.info("Received GET request");
    response.setContentType("application/json;");
    String responseBody = convertToJsonUsingGson(comments);
    logger.info("Sending response:\n" + responseBody);
    response.getWriter().println(responseBody);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    logger.info("Received POST request");
    String comment = getParameter(request, "user-comment", "");
    if (!comment.isEmpty()) {
      comments.add(comment);
    }
    response.sendRedirect("/index.html");
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  private String convertToJsonUsingGson(Object o) {
    return new Gson().toJson(o);
  }
}
