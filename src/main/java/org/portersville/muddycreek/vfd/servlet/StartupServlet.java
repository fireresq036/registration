package org.portersville.muddycreek.vfd.servlet;

import com.googlecode.objectify.ObjectifyService;
import org.portersville.muddycreek.vfd.entity.Address;
import org.portersville.muddycreek.vfd.entity.Event;
import org.portersville.muddycreek.vfd.entity.Log;
import org.portersville.muddycreek.vfd.entity.Person;
import org.portersville.muddycreek.vfd.entity.Team;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mark on 3/11/15.
 */
public class StartupServlet  extends HttpServlet {
  static {
    ObjectifyService.register(Event.class);
    ObjectifyService.register(Log.class);
    ObjectifyService.register(Person.class);
    ObjectifyService.register(Team.class);
  }

  private static final Logger log = Logger.getLogger(StartupServlet.class.getName());

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
  }
}
