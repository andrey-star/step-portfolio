package com.google.sps.servlets;

import static com.google.sps.servlets.RequestUtils.getParameter;
import static com.google.sps.servlets.RequestUtils.getRequestInfo;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/username")
public class UsernameServlet extends HttpServlet {

  private static final Logger logger = Logger.getLogger(UsernameServlet.class.getName());
  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    logger.info(getRequestInfo(request));
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect(userService.createLoginURL("/username.html"));
      return;
    }

    String username = getParameter(request, "username", "");
    if (!username.isEmpty()) {
      String id = userService.getCurrentUser().getUserId();
      Entity entity = new Entity("UserInfo", id);
      entity.setProperty("id", id);
      entity.setProperty("username", username);
      datastore.put(entity);
    }
    response.sendRedirect("/");
  }


}
