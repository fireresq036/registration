package org.portersville.muddycreek.fvd;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import org.portersville.muddycreek.vfd.registration.Registration.Event;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mark on 2/22/15.
 */
public class EventProcessing {
  private static final Logger log = Logger.getLogger(EventProcessing.class.getName());
  private static final String DS_ID = "id";
  private static final String DS_NAME = "name";
  private static final String DS_DESCRIPTION = "description";
  private static final String DS_LOCATION = "location";
  private static final String DS_START_DATE = "start_date";
  private static final String DS_END_DATE = "end_date";
  private static final String DS_TEAM_SIZE = "team_size";
  private static final String DS_DATE = "date";
  private static final String DS_AUTHOR_ID = "author_id";
  private static final String DS_AUTHOR_EMAIL = "author_email";
  private static final AtomicInteger idGenerator =
      new AtomicInteger(100);
  private static final DateFormat DF = new SimpleDateFormat("MM/dd/yyyy");

  protected Event AddEvent(HttpServletRequest req, User user)
      throws ParseException {
    int id = idGenerator.getAndIncrement();
    Key eventKey = KeyFactory.createKey("Event", id);

    Event event = Event.newBuilder()
        .setId(id)
        .setName(req.getParameter("eventName"))
        .setDescritpion(req.getParameter("eventDescription"))
        .setLocation(req.getParameter("eventLocation"))
        .setStartDate(ConvertDateToSecSinceEpoch(
            req.getParameter("eventStartDate")))
        .setEndDate(ConvertDateToSecSinceEpoch(
            req.getParameter("eventEndDate")))
        .setTeamSize(Integer.valueOf(req.getParameter("eventTeamSize")))
        .build();
    Entity eventData = new Entity(AdminServlet.DS_EVENT_ENTITY, eventKey);
    if (user != null) {
      eventData.setProperty(DS_AUTHOR_ID, user.getUserId());
      eventData.setProperty(DS_AUTHOR_EMAIL, user.getEmail());
    }
    eventData.setProperty(DS_DATE, new Date());
    eventData.setProperty(DS_ID, event.getId());
    eventData.setProperty(DS_NAME, event.getName());
    eventData.setProperty(DS_LOCATION, event.getLocation());
    eventData.setProperty(DS_START_DATE, event.getStartDate());
    eventData.setProperty(DS_END_DATE, event.getEndDate());
    eventData.setProperty(DS_TEAM_SIZE, event.getTeamSize());

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(eventData);
    return event;
  }

  protected List<Event> LoadEvents() {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query q = new Query(AdminServlet.DS_EVENT_ENTITY);
    PreparedQuery pq = datastore.prepare(q);
    List<Event> events = new ArrayList<>();
    for (Entity result : pq.asIterable()) {
      Event event = Event.newBuilder()
          .setId((Integer) result.getProperty(DS_ID))
          .setName((String) result.getProperty(DS_NAME))
          .setDescritpion((String) result.getProperty(DS_DESCRIPTION))
          .setLocation((String) result.getProperty(DS_LOCATION))
          .setStartDate((Long) result.getProperty(DS_START_DATE))
          .setEndDate((Long) result.getProperty(DS_END_DATE))
          .setTeamSize((Integer) result.getProperty(DS_TEAM_SIZE))
          .build();
      events.add(event);
    }
    return events;
  }

  static protected long ConvertDateToSecSinceEpoch(String date_in) throws ParseException {
    log.log(Level.INFO, "ConvertDateToSecSinceEpoch: {0}", new String[]{date_in});
    Date date = DF.parse(date_in);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.getTimeInMillis();
  }
}
