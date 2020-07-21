package com.google.sps.servlets;

import com.google.appengine.api.datastore.*;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private static final Logger logger = LogManager.getLogger(com.google.sps.servlets.DataServlet.class.getName());

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    logger.info("Received GET request");
    String commentLimitParameter = getParameter(request, "comment-limit", "");
    long commentLimit = parseLongOrDefault(commentLimitParameter, 10);
    String commentOrder = getParameter(request, "comment-order", "asc");

    Query.SortDirection order = commentOrder.equals("dec") ? Query.SortDirection.DESCENDING : Query.SortDirection.ASCENDING;
    Query commentsQuery = new Query("Comment").addSort("timestamp", order);
    PreparedQuery results = DatastoreServiceFactory.getDatastoreService().prepare(commentsQuery);
    List<Comment> comments = new ArrayList<>();
    Iterator<Entity> commentIterable = results.asIterable().iterator();
    while (commentIterable.hasNext() && commentLimit > 0) {
      Entity commentEntity = commentIterable.next();
      long id = commentEntity.getKey().getId();
      String text = (String) commentEntity.getProperty("text");
      long timestamp = (long) commentEntity.getProperty("timestamp");
      comments.add(new Comment(id, text, timestamp));
      commentLimit--;
    }

    response.setContentType("application/json;");
    String responseBody = toJson(comments);
    logger.info("Sending response:\n" + responseBody);
    response.getWriter().println(responseBody);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    logger.info("Received POST request");
    String comment = getParameter(request, "user-comment", "");
    if (!comment.isEmpty()) {
      handleComment(comment);
    }
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

  private long parseLongOrDefault(String number, long defaultValue) {
    try {
      return Long.parseLong(number);
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  private String toJson(Object o) {
    return new Gson().toJson(o);
  }
}
