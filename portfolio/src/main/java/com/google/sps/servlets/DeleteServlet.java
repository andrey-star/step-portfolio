package com.google.sps.servlets;

import com.google.appengine.api.datastore.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/delete-data")
public class DeleteServlet extends HttpServlet {

    private final Query COMMENTS_QUERY = new Query("Comment");

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(COMMENTS_QUERY);
        for (Entity comment : results.asIterable()) {
            datastore.delete(comment.getKey());
        }
    }
}
