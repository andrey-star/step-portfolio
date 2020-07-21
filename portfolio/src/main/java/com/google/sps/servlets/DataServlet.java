package com.google.sps.servlets;


import com.google.appengine.api.datastore.*;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private static final Logger logger = LogManager.getLogger(com.google.sps.servlets.DataServlet.class.getName());

  private final Query COMMENTS_QUERY = new Query("Comment")
          .addSort("timestamp", Query.SortDirection.ASCENDING);

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    logger.info("Received GET request");
    PreparedQuery results = DatastoreServiceFactory.getDatastoreService().prepare(COMMENTS_QUERY);
    List<Comment> comments = new ArrayList<>();
    for (Entity commentEntity : results.asIterable()) {
      long id = commentEntity.getKey().getId();
      String text = (String) commentEntity.getProperty("text");
      long timestamp = (long) commentEntity.getProperty("timestamp");
      comments.add(new Comment(id, text, timestamp));
    }

    response.setContentType("application/json;");
    String responseBody = toJson(comments);
    logger.info("Sending response:\n" + responseBody);
    response.getWriter().println(responseBody);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    logger.info("Received POST request");
    String comment = getParameter(request, "user-comment", "");
    if (!comment.isEmpty()) {
      handleComment(comment);
    }
    response.sendRedirect("/index.html");
  }

  private void handleComment(String comment) {
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("text", comment);
    commentEntity.setProperty("timestamp", System.currentTimeMillis());
    DatastoreServiceFactory.getDatastoreService().put(commentEntity);
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  private String toJson(Object o) {
    return new Gson().toJson(o);
  }
}
