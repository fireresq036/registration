package org.portersville.muddycreek.vfd.datastore;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.cmd.Query;
import org.portersville.muddycreek.vfd.entity.Event;
import org.portersville.muddycreek.vfd.entity.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mark on 2/22/15.
 */
public class EventProcessing extends ProcessingBase implements Serializable {
  private static final long serialVersionUID = 101L;
  private static final Logger log =
      Logger.getLogger(EventProcessing.class.getName());

  public void saveEvent(Event event, User user) {
    if (event.getId() != null) {
      log.log(Level.WARNING,
          "Trying to Save Event named {0} but an ID already exists",
          event.getName());
    } else {
      ofy().save().entity(event).now();
      if (user != null) {
        createLogEntry(event, user, "Save Event");
      }
      log.log(Level.INFO, "saved event {0}({1})",
          new Object[]{event.getName(), event.getId()});
    }
  }

  public void updateEvent(Event event, User user) {
    if (event.getId() == null) {
      log.log(Level.WARNING,
          "Trying to update Event named {0} but there is no ID",
          event.getName());
    } else {
      Event found = eventFromId(event.getId());
      found.update(event);
      ofy().save().entity(found).now();
      if (user != null) {
        createLogEntry(found, user, "Update Event");
      }
      log.log(Level.INFO, "updated event {0}({1})",
          new Object[]{ found.getName(), found.getId() });
    }
  }

  public List<Event> loadEvents() {
    List<Event> events = new ArrayList<Event>();
    Query<Event> q = ofy().load().type(Event.class);
    for (Event result : q) {
      events.add(result);
    }

    return events;
  }

  public Event eventFromId(Long event_id)  {
    return ofy().load().type(Event.class).id(event_id).now();
  }
}
