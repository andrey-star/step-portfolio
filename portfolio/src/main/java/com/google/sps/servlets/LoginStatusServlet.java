package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.google.sps.servlets.RequestUtils.getRequestInfo;
import static com.google.sps.servlets.RequestUtils.getCurrentUsername;
import static com.google.sps.servlets.RequestUtils.toJson;

@WebServlet("/login-status")
public class LoginStatusServlet extends HttpServlet {

  private static final Logger logger = Logger.getLogger(LoginStatusServlet.class.getName());
  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    logger.info(getRequestInfo(request));
    UserService userService = UserServiceFactory.getUserService();
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    boolean isLoggedIn = false;
    String authUrl = userService.createLoginURL("/username.html");
    boolean didSetUsername = false;
    if (userService.isUserLoggedIn()) {
      isLoggedIn = true;
      authUrl = userService.createLogoutURL("/");
      String username = getCurrentUsername(userService.getCurrentUser().getUserId(), datastore);
      didSetUsername = !username.isEmpty();
    }
    String responseBody = toJson(new LoginStatus(isLoggedIn, authUrl, didSetUsername));
    logger.info("Sending response: " + responseBody);
    response.getWriter().println(responseBody);
  }
}
