package org.portersville.muddycreek.fvd;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.portersville.muddycreek.vfd.registration.Registration.Address;
import org.portersville.muddycreek.vfd.registration.Registration.Person;
import org.portersville.muddycreek.vfd.registration.Registration.Team;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mark on 2/15/15.
 */
public class RegisterServlet extends HttpServlet {
  private static final Logger log = Logger.getLogger(RegisterServlet.class.getName());
  private static final String CONFIRM_JSP = "/confirm.jsp";

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();

    String teamName = req.getParameter("teamName");
    log.log(Level.INFO, "processing teamName: {0}", new String[]{teamName});
    Key teamKey = KeyFactory.createKey("Team", teamName);
    String captainName = req.getParameter("captainName");
    String captainEmail = req.getParameter("captainEmail");
    String captainPhone = req.getParameter("captainPhone");
    String captainAddress1 = req.getParameter("captainAddress1");
    String captainAddress2 = req.getParameter("captainAddress2");
    String captainCity = req.getParameter("captainCity");
    String captainState = req.getParameter("captainState");
    String captainZip = req.getParameter("captainZip");

    Date date = new Date();
    Entity teamData = new Entity("TeamInformation", teamKey);
    if (user != null) {
      teamData.setProperty("author_id", user.getUserId());
      teamData.setProperty("author_email", user.getEmail());
    }
    teamData.setProperty("date", date);
    teamData.setProperty("captain_name", captainName);
    teamData.setProperty("captain_email", captainEmail);
    teamData.setProperty("captain_phone", captainPhone);
    teamData.setProperty("captain_address1", captainAddress1);
    teamData.setProperty("captain_address2", captainAddress2);
    teamData.setProperty("captain_city", captainCity);
    teamData.setProperty("captain_state", captainState);
    teamData.setProperty("captain_Zip", captainZip);
    teamData.setProperty("team_name", teamName);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(teamData);
    Address address = Address.newBuilder()
        .setStreet1(captainAddress1)
        .setStreet2(captainAddress2)
        .setCity(captainCity)
        .setState(captainState)
        .setZip(captainZip)
        .build();
    Person captain = Person.newBuilder()
        .setName(captainName)
        .addAddress(address)
        .addEmail(captainEmail)
        .addEmail("test")
        .addPhone(captainPhone)
        .build();
    Team team = Team.newBuilder()
        .setName(teamName)
        .setCaptain(captain)
        .build();
    req.setAttribute("team_data", team);
    log.log(Level.INFO, "forwarding to {0}", CONFIRM_JSP);
    RequestDispatcher reqDispatcher =
        getServletConfig().getServletContext().
            getRequestDispatcher(CONFIRM_JSP);
    reqDispatcher.forward(req, resp);
  }
}
