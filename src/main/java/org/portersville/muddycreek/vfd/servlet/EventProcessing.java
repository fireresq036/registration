package org.portersville.muddycreek.vfd.servlet;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.cmd.Query;
import org.portersville.muddycreek.vfd.entity.Event;
import org.portersville.muddycreek.vfd.entity.Log;
import org.portersville.muddycreek.vfd.util.EntityCache;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mark on 2/22/15.
 */
public class EventProcessing implements Serializable {
  private static final long serialVersionUID = 101L;
  private static final Logger log = Logger.getLogger(EventProcessing.class.getName());
  private static final String COUNT_KEY = "Event-count";
  private static final String EVENT_KEY = "Event-";
  protected Event addEvent(HttpServletRequest req, User user)
      throws ParseException {
    Log.Builder log_builder = null;
    Event event = Event.newBuilder()
        .setName(req.getParameter("eventName"))
        .setDescription(req.getParameter("eventDescription"))
        .setLocation(req.getParameter("eventLocation"))
        .setStartDate(req.getParameter("eventStartDate"))
        .setEndDate(req.getParameter("eventEndDate"))
        .setTeamSize(req.getParameter("eventTeamSize"))
        .build();
    saveEvent(event, user);
    return event;
  }

  protected void saveEvent(Event event, User user) {
    EntityCache cache = EntityCache.CacheInstance();
    ofy().save().entity(event).now();
    if (user != null) {
      Log log = Log.newBuilder()
          .setUserId(user.getUserId())
          .setUserEmail(user.getEmail())
          .setEntityKey(event.getId())
          .setEntityType(Log.EntityType.EVENT)
          .build();
      ofy().save().entity(log).now();
    }
    log.log(Level.INFO, "saved event {}", event.getName());
    if (cache.find(COUNT_KEY) == null) {
      List<Event> events = new ArrayList<Event>();
      ReloadCache(new ArrayList<Event>(), cache);
      log.log(Level.INFO, "Reloaded cache");
    } else {
      Integer count = cache.find(COUNT_KEY);
      cache.put(EVENT_KEY + count, event);
      log.log(Level.INFO, "added event {} to cache", event.getName());
      count++;
      cache.put(COUNT_KEY, count);
      log.log(Level.INFO, "set event cachc count to {}", count);
    }
  }

  protected List<Event> loadEvents() {
    boolean reload_cache = false;
    List<Event> events = new ArrayList<Event>();
    EntityCache cache = EntityCache.CacheInstance();
    if (cache.isInCache(COUNT_KEY)) {
      int event_count = cache.find(COUNT_KEY);
      for (int i=0; i < event_count; i++) {
        Event event = cache.find(EVENT_KEY + i);
        if (event == null) {
          reload_cache = true;
          break;
        }
      }
    }
    if (reload_cache) {
      ReloadCache(events, cache);
    }

    log.log(Level.INFO,"retrieved {0} events from datastore", events.size());
    return events;
  }

  private void ReloadCache(List<Event> events, EntityCache cache) {
    Query<Event> q = ofy().load().type(Event.class);
    int count = 0;
    for (Event result : q) {
      events.add(result);
      log.log(Level.INFO, "inserting event names {0} into cache",
          result.getName());
      cache.put(EVENT_KEY + count, result);
      count++;
    }
    cache.put(COUNT_KEY, count);
  }
}
