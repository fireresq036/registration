package org.portersville.muddycreek.vfd.entity;

/**
 * Created by mark on 3/28/15.
 */
public interface EntityI {
  public Long getId();
  public <T> void update(T entity);
  public String getName();
}
