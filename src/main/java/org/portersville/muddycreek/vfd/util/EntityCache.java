package org.portersville.muddycreek.vfd.util;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mark on 3/12/15.
 */
public class EntityCache {
  public static final Logger log = Logger.getLogger(EntityCache.class.getName());

  private static EntityCache instance;
  private Cache cache;

  public static synchronized EntityCache CacheInstance() {
    if (instance == null) {
      instance = new EntityCache();
    }
    return instance;
  }

  private EntityCache() {
    try {
      CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
      cache = cacheFactory.createCache(Collections.emptyMap());
    }
    catch (CacheException e) {
      log.log(Level.WARNING, "Error in creating the Cache");
    }
  }

  public <K> boolean isInCache(K key) {
    return cache.containsKey(key);
  }

  public <K, T> T find(K key) {
    if (isInCache(key)) {
      return (T)cache.get(key);
    }
    return null;
  }

  public <K, T> void put(K key, T value) {
    cache.put(key, value);
  }
}
