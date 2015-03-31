package org.portersville.muddycreek.vfd.datastore;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.cmd.Query;
import org.portersville.muddycreek.vfd.entity.EntityI;
import org.portersville.muddycreek.vfd.entity.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by mark on 3/28/15.
 */
public class EntityProcessing<T> extends ProcessingBase {
  private static final long serialVersionUID = 101L;
  private static final Logger log =
      Logger.getLogger(EntityProcessing.class.getName());
  private Class<T> clazz;

  public EntityProcessing (Class<T> entityClass) {
    clazz = entityClass;
  }

  public <T extends EntityI> void saveEntity(T entity, User user) {
    if (entity.getId() != null) {
      log.log(Level.WARNING,
          "Trying to Save Entity named {0} but an ID already exists",
          entity.getName());
    } else {
      ofy().save().entity(entity).now();
      if (user != null) {
        createLogEntry(entity, user, "Save Entity");
      }
      log.log(Level.INFO, "saved entity {0}({1})",
          new Object[]{entity.getName(), entity.getId()});
    }
  }

  public <T extends EntityI> void updateEntity(T entity, User user) {
    if (entity.getId() == null) {
      log.log(Level.WARNING,
          "Trying to update Entity named {0} but there is no ID",
          entity.getName());
    } else {
      T found = (T)entityFromId(entity.getId());
      found.update(entity);
      ofy().save().entity(found).now();
      if (user != null) {
        createLogEntry(found, user, "Update Entity");
      }
      log.log(Level.INFO, "updated entity {0}({1})",
          new Object[]{ found.getName(), found.getId() });
    }
  }

  public <T extends EntityI> List<T> loadEntities() {
    List<T> entities = new ArrayList<>();
    Query<T> q = (Query<T>) ofy().load().type(clazz);
    for (T result : q) {
      entities.add(result);
    }

    return entities;
  }

  public <T extends EntityI> T entityFromId(Long entity_id)  {
    return (T) ofy().load().type(this.clazz).id(entity_id).now();
  }

  public <T extends EntityI> Query<T> entityWithFilter(String field, String target)  {
    return (Query<T>) ofy().load().type(this.clazz).filter(field, target);
  }
}
