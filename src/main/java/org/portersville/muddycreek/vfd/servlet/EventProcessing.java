package org.portersville.muddycreek.vfd.servlet;

import com.google.appengine.api.users.User;
import org.portersville.muddycreek.vfd.entity.Event;
import org.portersville.muddycreek.vfd.utility.PMF;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by mark on 2/22/15.
 */
public class EventProcessing implements Serializable {
  private static final long serialVersionUID = 101L;
  private static final Logger log = Logger.getLogger(EventProcessing.class.getName());
//  private static final String DS_ID = "id";
//  private static final String DS_NAME = "name";
//  private static final String DS_DESCRIPTION = "description";
//  private static final String DS_LOCATION = "location";
//  private static final String DS_START_DATE = "start_date";
//  private static final String DS_END_DATE = "end_date";
//  private static final String DS_TEAM_SIZE = "team_size";
//  private static final String DS_DATE = "date";
//  private static final String DS_AUTHOR_ID = "author_id";
//  private static final String DS_AUTHOR_EMAIL = "author_email";
//  private static final DateFormat DF = new SimpleDateFormat("MM/dd/yyyy");

  protected Event addEvent(HttpServletRequest req, User user, PersistenceManager pm)
      throws ParseException {
    Event.Builder builder = Event.newBuilder()
        .name(req.getParameter("eventName"))
        .description(req.getParameter("eventDescription"))
        .location(req.getParameter("eventLocation"))
        .startDate(req.getParameter("eventStartDate"))
        .endDate(req.getParameter("eventEndDate"))
        .teamSize(req.getParameter("eventTeamSize"));
    if (user != null) {
      builder.userId(user.getUserId())
          .userEmail(user.getEmail());
    }
    Event event = builder.build();
    try {
      pm.makePersistent(event);
    } finally {
      pm.close();
    }


//    eventData.setProperty(DS_DATE, new Date());
//    eventData.setProperty(DS_ID, event.getId());
//    eventData.setProperty(DS_NAME, event.getName());
//    eventData.setProperty(DS_LOCATION, event.getLocation());
//    eventData.setProperty(DS_START_DATE, event.getStartDate());
//    eventData.setProperty(DS_END_DATE, event.getEndDate());
//    eventData.setProperty(DS_TEAM_SIZE, event.getTeamSize());

//    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//    datastore.put(eventData);
    return event;
  }

  protected List<Event> loadEvents(PersistenceManager pm) {
    Query q = pm.newQuery(Event.class);
    List<Event> events = new ArrayList<Event>();
    try {
      events = (List<Event>) q.execute();
//      for (Event result : events) {
//        Event event = Event.newBuilder()
//            .id((Integer) result.getProperty(DS_ID))
//            .name((String) result.getProperty(DS_NAME))
//            .description((String) result.getProperty(DS_DESCRIPTION))
//            .location((String) result.getProperty(DS_LOCATION))
//            .startDate((Date) result.getProperty(DS_START_DATE))
//            .endDate((Date) result.getProperty(DS_END_DATE))
//            .teamSize((Integer) result.getProperty(DS_TEAM_SIZE))
//            .build();
//        events.add(event);    }
    } finally {
      q.closeAll();
    }
    return events;
  }
}
