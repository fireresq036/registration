package org.portersville.muddycreek.vfd.datastore;

import com.google.appengine.api.users.User;
import org.portersville.muddycreek.vfd.entity.EntityI;
import org.portersville.muddycreek.vfd.entity.Event;
import org.portersville.muddycreek.vfd.entity.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by mark on 3/28/15.
 */
public class ProcessingBase {
  private static final long serialVersionUID = 101L;
  private static final Logger log =
      Logger.getLogger(ProcessingBase.class.getName());

  protected <T extends EntityI> void
      createLogEntry(T entity, User user, String note) {
    log.log(Level.INFO, "adding log record");
    Log log_record = Log.newBuilder()
        .setUserId(user.getUserId())
        .setUserEmail(user.getEmail())
        .setEntityKey(entity.getId())
        .setEntityType(Log.EntityType.EVENT)
        .setNote(note)
        .build();
    ofy().save().entity(log_record).now();
  }
}
