package com.google.sps.servlets;

public class LoginStatus {

  private final boolean isLoggedIn;
  private final String authUrl;

  public LoginStatus(boolean status, String url) {
    this.isLoggedIn = status;
    this.authUrl = url;
  }

  public boolean isLoggedIn() {
    return isLoggedIn;
  }

  public String getAuthUrl() {
    return authUrl;
  }
}
