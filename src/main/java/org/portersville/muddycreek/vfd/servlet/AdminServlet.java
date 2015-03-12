package org.portersville.muddycreek.vfd.servlet;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.portersville.muddycreek.vfd.entity.Event;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mark on 2/16/15.
 */
public class AdminServlet extends HttpServlet {
  private static final long serialVersionUID = 101L;
  protected static final String DS_EVENT_ENTITY = "events";
  private static final Logger log = Logger.getLogger(AdminServlet.class.getName());
  private static final String ADMIN_EVENT_JSP = "/admin_event.jsp";
  private static final String ADMIN_JSP = "/admin.jsp";
  private static final String ADMIN_USER = "/admin_user.jsp";
  private static final String ADD_EVENT = "add_event";
  private static final String GET_EVENTS = "get_events";
  private static final String GET_USERS = "get_users";
  private EventProcessing eventProcessor = new EventProcessing();

  public AdminServlet() {
    super();

  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    log.log(Level.INFO, "doGet");
    doPost(req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    log.log(Level.INFO, "doPost");
    String forwardTo = ADMIN_JSP;
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    String command = req.getParameter("command");
    log.info("processing: " + command);
    StringBuilder builder = new StringBuilder();
    if (command == null) {
      builder.append("command not found on URL");
    } else {
      if (command.equalsIgnoreCase(GET_EVENTS)) {
          req.setAttribute("known_events", eventProcessor.loadEvents());
        forwardTo = ADMIN_EVENT_JSP;
      } else if (command.equalsIgnoreCase(GET_USERS)) {
        forwardTo = ADMIN_USER;
      } else if (command.equalsIgnoreCase(ADD_EVENT)) {
        List<Event> events =
            (List<Event>) req.getAttribute("known_events");
        if (events == null) {
          events = new ArrayList<Event>();
        }
        try {
          log.log(Level.INFO, "events size: {0}", new Integer[]{events.size()});
          events.add(eventProcessor.addEvent(req, user));
          log.log(Level.INFO, "After events size: {0}", new Integer[]{events.size()});
          req.setAttribute("known_events", events);
        } catch (ParseException e) {
          resp.sendRedirect(
              String.format("%s?errorString=%s", ADMIN_EVENT_JSP,
                  "Could not parse one of the dates."));
        }
        forwardTo = ADMIN_EVENT_JSP;
      } else {
        builder.append("command ")
            .append(command)
            .append(" is not known");
      }
      if (builder.length() > 0) {
        resp.sendRedirect(
            String.format("%s?errorString=%s", ADMIN_JSP, builder.toString()));
      }
    }
    ServletConfig config = getServletConfig();
    ServletContext context = config.getServletContext();
    log.log(Level.INFO, "Dispatching to {0}", new String[]{forwardTo});
    RequestDispatcher reqDispatcher =
        context.getRequestDispatcher(forwardTo);
    reqDispatcher.forward(req, resp);
  }
}
