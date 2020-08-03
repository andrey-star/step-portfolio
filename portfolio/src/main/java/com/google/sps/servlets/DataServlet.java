package com.google.sps.servlets;

import static com.google.sps.servlets.RequestUtils.getCurrentUsername;
import static com.google.sps.servlets.RequestUtils.getParameter;
import static com.google.sps.servlets.RequestUtils.getRequestInfo;
import static com.google.sps.servlets.RequestUtils.parseLongOrDefault;
import static com.google.sps.servlets.RequestUtils.toJson;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private static final Logger logger = Logger.getLogger(DataServlet.class.getName());
  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    logger.info(getRequestInfo(request));
    String commentLimitParameter = getParameter(request, "comment-limit", "");
    long commentLimit = parseLongOrDefault(commentLimitParameter, 10);
    String commentOrder = getParameter(request, "comment-order", "asc");

    Query.SortDirection order = commentOrder.equals("dec")
            ? Query.SortDirection.DESCENDING
            : Query.SortDirection.ASCENDING;
    Query commentsQuery = new Query("Comment").addSort("timestamp", order);
    PreparedQuery results = datastore.prepare(commentsQuery);
    List<Comment> comments = new ArrayList<>();
    Iterator<Entity> commentIterable = results.asIterable().iterator();
    while (commentIterable.hasNext() && commentLimit > 0) {
      Entity commentEntity = commentIterable.next();
      comments.add(getCommentFromEntity(commentEntity));
      commentLimit--;
    }

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String responseBody = toJson(comments);
    logger.info("Sending response: " + responseBody);
    response.getWriter().println(responseBody);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    logger.info(getRequestInfo(request));
    String comment = getParameter(request, "user-comment", "");
    if (!comment.isEmpty()) {
      saveComment(comment);
    }
  }

  private Comment getCommentFromEntity(Entity commentEntity) {
    String key = KeyFactory.keyToString(commentEntity.getKey());
    String text = (String) commentEntity.getProperty("text");
    String email = (String) commentEntity.getProperty("email");
    String username = (String) commentEntity.getProperty("username");
    long timestamp = (long) commentEntity.getProperty("timestamp");
    return new Comment(key, text, email, username, timestamp);
  }

  private void saveComment(String comment) {
    logger.info("Saving comment: " + comment);
    User currentUser = UserServiceFactory.getUserService().getCurrentUser();
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("text", comment);
    commentEntity.setProperty("email", currentUser.getEmail());
    commentEntity.setProperty("username", getCurrentUsername(currentUser.getUserId(), datastore));
    commentEntity.setProperty("timestamp", System.currentTimeMillis());
    datastore.put(commentEntity);
  }
}
