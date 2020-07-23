package com.google.sps.servlets;

public class Comment {

  private final String key;
  private final String text;
  private final long timestamp;

  public Comment(String key, String text, long timestamp) {
    this.key = key;
    this.text = text;
    this.timestamp = timestamp;
  }

}
