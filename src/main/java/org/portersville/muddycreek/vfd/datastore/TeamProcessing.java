package org.portersville.muddycreek.vfd.datastore;

import com.google.appengine.api.users.User;
import org.portersville.muddycreek.vfd.entity.Person;
import org.portersville.muddycreek.vfd.entity.Team;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by mark on 3/28/15.
 */
public class TeamProcessing {
  private static final long serialVersionUID = 101L;
  private static final Logger log =
      Logger.getLogger(TeamProcessing.class.getName());

  public void saveTeam(Team team, Person captain, List<Person> members,
                       User user) {


  }

}
