package com.google.sps.servlets;

public class LoginStatus {

  private final boolean isLoggedIn;
  private final String authUrl;
  private final boolean didSetUsername;

  /**
   * Constructs a new {@code LoginStatus} object with the provided login status, login/logout url
   * and {@code didSetUsername} flag.
   *
   * @param status {@code true}, if the the status is 'logged in', {@code false} otherwise
   * @param url the login url, if the user is not logged in, a logout url otherwise
   * @param didSetUsername true, if this user has laready associated a username with their account
   */
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
