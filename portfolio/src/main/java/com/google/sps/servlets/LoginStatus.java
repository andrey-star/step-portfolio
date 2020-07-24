package com.google.sps.servlets;

public class LoginStatus {

  private final boolean status;
  private final String url;

  public LoginStatus(boolean status, String url) {
    this.status = status;
    this.url = url;
  }

  public boolean isStatus() {
    return status;
  }

  public String getUrl() {
    return url;
  }
}
