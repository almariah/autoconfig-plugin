package io.almariah.jenkins.plugins.autoconfig;

import java.lang.String;
import jenkins.model.Jenkins;
import java.io.IOException;

public class Kubernetes {

  private String name;
  private String serverUrl;
  private String namespace;
  private String credentialsId;
  private int containerCap = 10;
  private int maxRequests = 32;
  private boolean skipTLS = false;

  public String getName() {
        return name;
  }

  public String getServerUrl() {
        return serverUrl;
  }

  public String getNamespace() {
        return namespace;
  }

  public String getCredentialsId() {
        return credentialsId;
  }

  public int getContainerCap() {
        return containerCap;
  }

  public int getMaxRequests() {
        return maxRequests;
  }

  public boolean isSkipTLS() {
        return skipTLS;
  }

  public void setName(String name) {
        this.name = name;
  }

  public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
  }

  public void setNamespace(String namespace) {
        this.namespace = namespace;
  }

  public void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId;
  }

  public void setContainerCap(int containerCap) {
        this.containerCap = containerCap;
  }

  public void setMaxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
  }

  public void setSkipTLS(boolean skipTLS) {
        this.skipTLS = skipTLS;
  }
}
