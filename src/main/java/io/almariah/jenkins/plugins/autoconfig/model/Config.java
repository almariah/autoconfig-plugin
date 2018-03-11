package io.almariah.jenkins.plugins.autoconfig.model;

import jenkins.model.Jenkins;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Logger;
import java.io.IOException;

import jenkins.model.JenkinsLocationConfiguration;
import hudson.security.GlobalMatrixAuthorizationStrategy;
import java.util.concurrent.ExecutionException;
import hudson.lifecycle.RestartNotSupportedException;
import java.lang.InterruptedException;


public class Config {

  private String url;
  private String adminAddress;
  private int executors;
  private Crowd2 crowd;
  private List<JenkinsUser> jenkinsUsers;
  private boolean pluginsRestart = true;
  private List<String> plugins;
  private List<JenkinsCredentials> credentials;
  private List<SeedJob> seedJobs;
  //  = new ArrayList<SeedJob>()
  private List<Kubernetes> kubernetes;

  public String getUrl() {
        return url;
  }

  public String getAdminAddress() {
        return adminAddress;
  }

  public int getExecutors() {
        return executors;
  }

  public Crowd2 getCrowd() {
        return crowd;
  }

  public List<JenkinsUser> getJenkinsUsers() {
        return jenkinsUsers;
  }

  public boolean isPluginsRestart() {
        return pluginsRestart;
  }

  public List<String> getPlugins() {
        return plugins;
  }

  public List<JenkinsCredentials> getCredentials() {
        return credentials;
  }

  public List<SeedJob> getSeedJobs() {
        return seedJobs;
  }

  public List<Kubernetes> getKubernetes() {
        return kubernetes;
  }

  public void setUrl(String url) {
        this.url = url;
  }

  public void setAdminAddress(String adminAddress) {
        this.adminAddress = adminAddress;
  }

  public void setExecutors(int executors) {
        this.executors = executors;
  }

  public void setCrowd(Crowd2 crowd) {
        this.crowd = crowd;
  }

  public void setJenkinsUsers(List<JenkinsUser> jenkinsUsers)  {
        this.jenkinsUsers = jenkinsUsers;
  }

  public void setPluginsRestart(boolean pluginsRestart) {
        this.pluginsRestart = pluginsRestart;
  }

  public void setPlugins(List<String> plugins) {
        this.plugins = plugins;
  }

  public void setCredentials(List<JenkinsCredentials> credentials) {
        this.credentials = credentials;
  }

  public void setSeedJobs(List<SeedJob> seedJobs) {
        this.seedJobs = seedJobs;
  }

  public void setKubernetes(List<Kubernetes> kubernetes) {
        this.kubernetes = kubernetes;
  }
}
