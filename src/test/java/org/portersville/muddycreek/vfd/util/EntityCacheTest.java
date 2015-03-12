package org.portersville.muddycreek.vfd.util;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.portersville.muddycreek.vfd.entity.Log;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by mark on 3/12/15.
 */
public class EntityCacheTest {
  private static final String KEY_1 = "1";
  private static final Long KEY_2 = new Long(2);
  private static final Date KEY_3 = new Date();
  private static final String VALUE_1 = "this is a test";
  private static final String VALUE_2 = "this is a test2";
  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
      new LocalMemcacheServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() throws Exception {
    helper.tearDown();
  }

  @Test
  public void testCreate() {
    EntityCache cache = EntityCache.CacheInstance();
    assertNotNull(cache);
  }

  @Test
  public void testIsInCacheAndPut() {
    EntityCache cache = EntityCache.CacheInstance();
    assertNotNull(cache);
    assertFalse(cache.isInCache(KEY_1));
    assertFalse(cache.isInCache(KEY_2));
    cache.put(KEY_1, VALUE_1);
    cache.put(KEY_2, VALUE_2);
    assertTrue(cache.isInCache(KEY_1));
    assertTrue(cache.isInCache(KEY_2));
  }

  @Test
  public void testFind() {
    EntityCache cache = EntityCache.CacheInstance();
    assertNotNull(cache);
    assertFalse(cache.isInCache(KEY_1));
    assertFalse(cache.isInCache(KEY_2));
    cache.put(KEY_1, VALUE_1);
    cache.put(KEY_2, VALUE_2);
    assertEquals(VALUE_1, cache.find(KEY_1));
    assertEquals(VALUE_2, cache.find(KEY_2));
    assertNull(cache.find(KEY_3));
  }

}