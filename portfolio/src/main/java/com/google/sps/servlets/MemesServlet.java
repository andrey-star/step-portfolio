package com.google.sps.servlets;

import static com.google.sps.servlets.RequestUtils.getRequestInfo;
import static com.google.sps.servlets.RequestUtils.toJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/memes-data")
public class MemesServlet extends HttpServlet {

  private static final Logger logger = Logger.getLogger(MemesServlet.class.getName());
  private final List<Object[]> memesData = new ArrayList<>();

  @Override
  public void init() {
    Scanner scanner = new Scanner(getServletContext()
            .getResourceAsStream("/WEB-INF/memes-popularity-by-week.csv"));
    memesData.add(scanner.nextLine().split(","));
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cellsStr = line.split(",");
      Object[] cells = new Object[cellsStr.length];
      cells[0] = cellsStr[0];
      for (int i = 1; i < cellsStr.length; i++) {
        cells[i] = Integer.valueOf(cellsStr[i]);
      }
      memesData.add(cells);
    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    logger.info(getRequestInfo(request));
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String responseBody = toJson(memesData);
    logger.info("Sending response: " + responseBody);
    response.getWriter().println(responseBody);
  }
}
