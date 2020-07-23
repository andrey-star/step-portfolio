package com.google.sps.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Key;

import java.util.logging.Logger;

import static com.google.sps.servlets.RequestUtils.getParameter;
import static com.google.sps.servlets.RequestUtils.getRequestInfo;

@WebServlet("/delete-data")
public class DeleteServlet extends HttpServlet {

  private static final Logger logger = Logger.getLogger(DeleteServlet.class.getName());
  private final Query COMMENTS_QUERY = new Query("Comment");
  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    logger.info(getRequestInfo(request));
    String commentKey = getParameter(request, "comment-key", "");
    if (commentKey.isEmpty()) {
      deleteAllComments();
    } else {
      deleteComment(KeyFactory.stringToKey(commentKey));
    }
  }

  private void deleteAllComments() {
    logger.info("Deleting all comments");
    PreparedQuery results = datastore.prepare(COMMENTS_QUERY);
    for (Entity comment : results.asIterable()) {
      deleteComment(comment.getKey());
    }
  }

  private void deleteComment(Key key) {
    logger.info("Deleting comment: " + KeyFactory.keyToString(key));
    datastore.delete(key);
  }
}
