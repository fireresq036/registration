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
  private static final String ADMIN_EVENT_LIST_JSP = "/admin_event_list.jsp";
  private static final String ADMIN_EVENT_EDIT_JSP = "/admin_event_edit.jsp";
  private static final String ADMIN_JSP = "/admin.jsp";
  private static final String ADMIN_USER = "/admin_user.jsp";
  private static final String ADD_EVENT = "add_event";
  private static final String GET_EVENTS = "get_events";
  private static final String GET_USERS = "get_users";
  private static final String EDIT_EVENT_QUERY = "edit_event_query";
  private static final String EDIT_EVENT = "edit_event";
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
        List<Event> events = eventProcessor.loadEvents();
        if (events.size() > 0) {
          req.setAttribute("known_events", events);
          forwardTo = ADMIN_EVENT_LIST_JSP;
        } else {
          req.setAttribute("edit_event", Event.newBuilder().build());
          req.setAttribute("add_event", true);
          forwardTo = ADMIN_EVENT_EDIT_JSP;
        }
      } else if (command.equalsIgnoreCase(GET_USERS)) {
        forwardTo = ADMIN_USER;
      } else if (command.equalsIgnoreCase(ADD_EVENT)) {
        try {
          eventProcessor.addEvent(req, user);
          req.setAttribute("known_events", eventProcessor.loadEvents());
        } catch (ParseException e) {
          req.setAttribute("error_string", builder.toString());
          forwardTo = ADMIN_JSP;
          resp.sendRedirect(
              String.format("%s?errorString=%s", ADMIN_EVENT_LIST_JSP,
                  "Could not parse one of the dates."));
        }
        forwardTo = ADMIN_EVENT_LIST_JSP;
      } else if (command.equalsIgnoreCase(EDIT_EVENT_QUERY)) {
        Long event_id = new Long(req.getParameter("editKey"));
        req.setAttribute("edit_event", eventProcessor.eventFromId(event_id));
        req.setAttribute("add_event", false);
        forwardTo = ADMIN_EVENT_EDIT_JSP;
      } else if (command.equalsIgnoreCase(EDIT_EVENT)){
        try {
          eventProcessor.UpdateEvent(new Long(req.getParameter("editKey")),
              req, user);
        } catch (ParseException e) {
          e.printStackTrace();
        }

      } else {
        builder.append("command ")
            .append(command)
            .append(" is not known");
        log.log(Level.WARNING, builder.toString());
        req.setAttribute("error_string", builder.toString());
        forwardTo = ADMIN_JSP;
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
