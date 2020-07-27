package com.google.sps.servlets;

public class LoginStatus {

  private final boolean isLoggedIn;
  private final String authUrl;
  private final boolean didSetUsername;

  public LoginStatus(boolean status, String url, boolean didSetUsername) {
    this.isLoggedIn = status;
    this.authUrl = url;
    this.didSetUsername = didSetUsername;
  }

  public boolean isLoggedIn() {
    return isLoggedIn;
  }

  public String getAuthUrl() {
    return authUrl;
  }

  public boolean didSetUsername() {
    return didSetUsername;
  }
}
