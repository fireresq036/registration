package org.portersville.muddycreek.vfd.datastore;

import com.google.appengine.api.users.User;
import org.portersville.muddycreek.vfd.entity.Person;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by mark on 3/28/15.
 */
public class PersonProcessing extends ProcessingBase implements Serializable {
  private static final long serialVersionUID = 101L;
  private static final Logger log =
      Logger.getLogger(PersonProcessing.class.getName());

  public void savePerson(Person person, User user) {
    if (person.getId() != null) {
      log.log(Level.WARNING,
          "Trying to Save Person named {0} ({1}) but an ID already exists",
          new String[] {person.getName(), person.getEmail()});
    } else {
      ofy().save().entity(person).now();
      if (user != null) {
        createLogEntry(person, user, "Save Event");
      }
      log.log(Level.INFO, "saved event {0}({1})",
          new Object[]{person.getName(), person.getId()});
    }
  }
}
