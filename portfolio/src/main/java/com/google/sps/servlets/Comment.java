package com.google.sps.servlets;

public class Comment {

  private final String key;
  private final String text;
  private final String email;
  private final String username;
  private final long timestamp;

  public Comment(String key, String text, String email, String username, long timestamp) {
    this.key = key;
    this.text = text;
    this.email = email;
    this.username = username;
    this.timestamp = timestamp;
  }

  public String getKey() {
    return key;
  }

  public String getText() {
    return text;
  }

  public String getEmail() {
    return email;
  }

  public String getUsername() {
    return username;
  }

  public long getTimestamp() {
    return timestamp;
  }
}
