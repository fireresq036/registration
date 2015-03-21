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
  private static final String PRE_ADD_EVENT = "pre_add_event";
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
    doPost(req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    String forwardTo = ADMIN_JSP;
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user == null) {
      log.log(Level.WARNING, "No user defined for application");
    } else {
      log.log(Level.INFO, "user = ", user.getUserId());
    }
    String command = req.getParameter("command");
    log.info("processing: " + command);
    StringBuilder builder = new StringBuilder();
    if (command == null) {
      builder.append("command not found on URL");
    } else {
      if (command.equalsIgnoreCase(GET_EVENTS)) {
        log.log(Level.INFO, GET_EVENTS);
        if (eventProcessor.loadEvents().size() > 0) {
          forwardTo = ADMIN_EVENT_LIST_JSP;
        } else {
          req.setAttribute("edit_event", Event.newBuilder().build());
          req.setAttribute("add_event", true);
          forwardTo = ADMIN_EVENT_EDIT_JSP;
        }
      } else if (command.equalsIgnoreCase(GET_USERS)) {
        log.log(Level.INFO, GET_USERS);
        forwardTo = ADMIN_USER;
      } else if (command.equalsIgnoreCase(PRE_ADD_EVENT)) {
        log.log(Level.INFO, PRE_ADD_EVENT);
        req.setAttribute("add_event", true);
        forwardTo = ADMIN_EVENT_EDIT_JSP;
      } else if (command.equalsIgnoreCase(ADD_EVENT)) {
        log.log(Level.INFO, ADD_EVENT);
        try {
          eventProcessor.saveEvent(eventFromRequest(req), user);
        } catch (ParseException e) {
          req.setAttribute("error_string", builder.toString());
        }
        forwardTo = ADMIN_EVENT_LIST_JSP;
      } else if (command.equalsIgnoreCase(EDIT_EVENT_QUERY)) {
        log.log(Level.INFO, EDIT_EVENT_QUERY);
        if (req.getParameter("editKey") == null) {
          log.log(Level.SEVERE, "Edit Requested but no key supplies");
          req.setAttribute("error_string", "No Key specified on edit");
        } else {
          Long event_id = new Long(req.getParameter("editKey"));
          req.setAttribute("edit_event", eventProcessor.eventFromId(event_id));
          req.setAttribute("add_event", false);
        }
        forwardTo = ADMIN_EVENT_EDIT_JSP;
      } else if (command.equalsIgnoreCase(EDIT_EVENT)){
        log.log(Level.INFO, EDIT_EVENT);
        try {
          eventProcessor.updateEvent(eventFromRequest(req), user);
        } catch (ParseException e) {
          e.printStackTrace();
        }
        forwardTo = ADMIN_EVENT_LIST_JSP;
      } else {
        builder.append("command ")
            .append(command)
            .append(" is not known");
        log.log(Level.WARNING, builder.toString());
        req.setAttribute("error_string", builder.toString());
        forwardTo = ADMIN_JSP;
      }
    }
    List<Event> events = eventProcessor.loadEvents();
    req.setAttribute("known_events", events);
    ServletConfig config = getServletConfig();
    ServletContext context = config.getServletContext();
    log.log(Level.INFO, "Dispatching to {0}", new String[]{forwardTo});
    RequestDispatcher reqDispatcher =
        context.getRequestDispatcher(forwardTo);
    reqDispatcher.forward(req, resp);
  }

  protected Event eventFromRequest(HttpServletRequest req) throws ParseException {
    Long id = null;
    String id_s = req.getParameter("eventId");
    if (id_s != null && id_s.length() > 0) {
      id = new Long(id_s);
    }
    log.log(Level.INFO, "event From request id = {0}", id);
    log.log(Level.INFO, "event From request id_s = {0}", id_s);
    return Event.newBuilder()
        .setId(id)
        .setName(req.getParameter("eventName"))
        .setDescription(req.getParameter("eventDescription"))
        .setLocation(req.getParameter("eventLocation"))
        .setStartDate(req.getParameter("eventStartDate"))
        .setEndDate(req.getParameter("eventEndDate"))
        .setTeamSize(req.getParameter("eventTeamSize"))
        .build();
  }

}
