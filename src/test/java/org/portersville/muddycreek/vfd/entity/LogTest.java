package org.portersville.muddycreek.vfd.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by mark on 3/11/15.
 */
public class LogTest {
  private static final String EMAIL = "me@you.com";
  private static final String ID = "ID1";
  private static final String NOTE = "this is a note";
  private static final Long EKEY = 25L;
  @Test
  public void testCreate() {
    Log log = Log.newBuilder()
        .setUserEmail(EMAIL)
        .setUserId(ID)
        .setNote(NOTE)
        .setEntityKey(EKEY)
        .setEntityType(Log.EntityType.EVENT)
        .build();
    assertNotNull(log);
    assertEquals(EMAIL, log.getUserEmail());
    assertEquals(ID, log.getUserId());
    assertEquals(NOTE, log.getNote());
    assertEquals(EKEY, log.getEntityKey());
    assertEquals(Log.EntityType.EVENT, log.getEntityType());
    assertEquals(NOTE, log.getNote());
  }

  @Test
  public void testCopy() {
    Log log = Log.newBuilder()
        .setUserEmail(EMAIL)
        .setUserId(ID)
        .setNote(NOTE)
        .setEntityKey(EKEY)
        .setEntityType(Log.EntityType.EVENT)
        .build();
    Long new_key = EKEY + 1;
    Log log2 = Log.newBuilder(log)
        .setEntityKey(new_key)
        .build();
    assertNotNull(log2);
    assertEquals(EMAIL, log2.getUserEmail());
    assertEquals(ID, log2.getUserId());
    assertEquals(NOTE, log2.getNote());
    assertEquals(new_key, log2.getEntityKey());
    assertEquals(Log.EntityType.EVENT, log2.getEntityType());
    assertEquals(NOTE, log2.getNote());
  }

}
