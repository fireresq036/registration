package org.portersville.muddycreek.vfd.servlet;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.cmd.Query;
import org.portersville.muddycreek.vfd.datastore.EntityProcessing;
import org.portersville.muddycreek.vfd.entity.Address;
import org.portersville.muddycreek.vfd.entity.Person;
import org.portersville.muddycreek.vfd.entity.Team;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mark on 2/15/15.
 */
public class RegisterServlet extends HttpServlet {
  private static final long serialVersionUID = 101L;
  private static final Logger log =
      Logger.getLogger(RegisterServlet.class.getName());
  private static final String CONFIRM_JSP = "/confirm.jsp";
  private static final String REGISTER_JSP = "/register.jsp";

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();

    Person captain = findOrAddCaptain(req, user);
    Team team = createTeam(req, captain);
    String forwardTo = CONFIRM_JSP;
    if (findTeam(team)) {
      forwardTo = REGISTER_JSP;
      req.setAttribute("Error", true);
    } else {
      addTeam(team, user);
    }

    req.setAttribute("team_data", team);
    log.log(Level.INFO, "forwarding to {0}", forwardTo);
    RequestDispatcher reqDispatcher =
        getServletConfig().getServletContext().
            getRequestDispatcher(forwardTo);
    reqDispatcher.forward(req, resp);
  }

  protected Team createTeam(HttpServletRequest req, Person captain) {
    return Team.newBuilder()
        .setName(req.getParameter("teamName"))
            .setCaptain(captain)
            .build();
  }

  protected boolean findTeam(Team team) {
    boolean found = false;
    EntityProcessing<Team> processor = new EntityProcessing<>(Team.class);
    Query<Team> pQuery = processor.entityWithFilter("name", team.getName());
    for(Team person : pQuery) {
      found = true;
    }
    return found;
  }

  protected void addTeam(Team team, User user) {
    EntityProcessing<Team> processor = new EntityProcessing<>(Team.class);
    processor.saveEntity(team, user);
  }

  protected Person findOrAddCaptain(HttpServletRequest req, User user) {
    Address.Builder addrBuilder = Address.newBuilder()
        .setStreet1(req.getParameter("captainAddress1"))
        .setStreet2(req.getParameter("captainAddress2"))
        .setCity(req.getParameter("captainCity"))
        .setState(req.getParameter("captainState"))
        .setZip(req.getParameter("captainZip"));
    Person captain = Person.newBuilder()
        .setName(req.getParameter("captainName"))
        .setEmail(req.getParameter("captainEmail"))
        .setPhone(req.getParameter("captainPhone"))
        .setAddress(addrBuilder.build())
        .build();
    return addIfMising(captain, user);
  }

  protected Person addIfMising(Person captain, User user) {
    boolean found = false;
    Person foundCaptain = null;
    EntityProcessing<Person> processor = new EntityProcessing<>(Person.class);
    Query<Person> pQuery = processor.entityWithFilter("email", captain.getEmail());
    for(Person person : pQuery) {
      found = true;
      foundCaptain = person;
    }
    if (!found) {
      processor.saveEntity(captain, user);
    }
    return found?foundCaptain:captain;
  }
}
