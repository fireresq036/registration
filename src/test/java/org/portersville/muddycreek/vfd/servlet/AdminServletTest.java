package org.portersville.muddycreek.vfd.servlet;

import org.junit.Test;
import org.mockito.internal.matchers.Null;
import org.portersville.muddycreek.vfd.entity.Event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by mark on 3/20/15.
 */
public class AdminServletTest {
  private static final Logger log =
      Logger.getLogger(AdminServletTest.class.getName());

  private static final String NAME = "name";
  private static final String DESCRIPTION = "description";
  private static final String LOCATION = "location";
  private static final String START_DATE_STRING = "6/22/2015";
  private static final String END_DATE_STRING = "6/22/2015";
  private static final String TEAM_SIZE = "2";
  private static final SimpleDateFormat SDF =
      new SimpleDateFormat("MM/dd/yyyy");

  @Test
  public void CreateEvent() throws ParseException {
    final HttpServletRequest req = mock(HttpServletRequest.class);
    given(req.getParameter("eventId")).willReturn(null);
    given(req.getParameter("eventName")).willReturn(NAME);
    given(req.getParameter("eventDescription")).willReturn(DESCRIPTION);
    given(req.getParameter("eventLocation")).willReturn(LOCATION);
    given(req.getParameter("eventStartDate")).willReturn(START_DATE_STRING);
    given(req.getParameter("eventEndDate")).willReturn(END_DATE_STRING);
    given(req.getParameter("eventTeamSize")).willReturn(TEAM_SIZE);

    AdminServlet servlet = new AdminServlet();
    Event event = servlet.eventFromRequest(req);
    assertNull(event.getId());
    assertEquals(NAME, event.getName());
    assertEquals(LOCATION, event.getLocation());
    assertEquals(DESCRIPTION, event.getDescription());
    assertEquals(SDF.parse(START_DATE_STRING), event.getStartDate());
    assertEquals(SDF.parse(END_DATE_STRING), event.getEndDate());
    assertEquals(new Integer(TEAM_SIZE), new Integer(event.getTeamSize()));
  }

}
