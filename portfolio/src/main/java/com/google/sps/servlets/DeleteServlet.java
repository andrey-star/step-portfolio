package com.google.sps.servlets;

import com.google.appengine.api.datastore.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.google.sps.servlets.RequestUtils.*;

@WebServlet("/delete-data")
public class DeleteServlet extends HttpServlet {

  private final Query COMMENTS_QUERY = new Query("Comment");
  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    String commentKey = getParameter(request, "comment-key", "");
    if (commentKey.isEmpty()) {
      deleteAllComments();
    } else {
      deleteComment(KeyFactory.stringToKey(commentKey));
    }
  }

  private void deleteAllComments() {
    PreparedQuery results = datastore.prepare(COMMENTS_QUERY);
    for (Entity comment : results.asIterable()) {
      deleteComment(comment.getKey());
    }
  }

  private void deleteComment(Key key) {
    datastore.delete(key);
  }
}
