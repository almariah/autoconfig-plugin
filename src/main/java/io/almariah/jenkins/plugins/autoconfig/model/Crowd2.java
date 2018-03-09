package io.almariah.jenkins.plugins.autoconfig.model;

import jenkins.model.Jenkins;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import jenkins.model.Jenkins;
import java.io.IOException;

public class Crowd2 {
  private String url;
  private String appName;
  private String appPassword;
  private String restrictGroups;

  public String getUrl() {
    return url;
  }

  public String getAppName() {
    return appName;
  }

  public String getAppPassword() {
    return appPassword;
  }

  public String getRestrictGroups() {
    return restrictGroups;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public void setAppPassword(String appPassword) {
    this.appPassword = appPassword;
  }

  public void setRestrictGroups(String restrictGroups) {
    this.restrictGroups = restrictGroups;
  }
}
