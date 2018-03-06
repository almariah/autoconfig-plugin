package io.almariah.jenkins.plugins.autoconfig;

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
  private List<JenkinsUser> jenkinsUsers = new ArrayList<JenkinsUser>();
  private boolean pluginsRestart = true;
  private List<String> plugins = new ArrayList<String>();
  private List<JenkinsCredentials> credentials = new ArrayList<JenkinsCredentials>();
  private List<SeedJob> seedJobs = new ArrayList<SeedJob>();
  private List<Kubernetes> kubernetes = new ArrayList<Kubernetes>();

  private Jenkins instance = Jenkins.getInstance();
  private JenkinsLocationConfiguration jenkinsModel = new JenkinsLocationConfiguration();
  private GlobalMatrixAuthorizationStrategy strategy = new GlobalMatrixAuthorizationStrategy();

  private final static Logger LOG = Logger.getLogger(Config.class.getName());

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

  public static void configure() throws Exception, IOException, InterruptedException, ExecutionException, RestartNotSupportedException {

    CustomClassLoaderConstructor constr = new CustomClassLoaderConstructor(Config.class.getClassLoader());

    Yaml yaml = new Yaml(constr);
    String homeDir = System.getProperty("user.home");
    // fix try
    // if yaml does not exist
    try (InputStream in = new FileInputStream(new File(homeDir + "/jenkins.yaml"))) {
      Config config = null;
      try {
        config =  yaml.loadAs(in, Config.class);
        Validator.validate(config);
      } catch (Exception e) {
        LOG.warning(e.getMessage());
      }

      config.jenkinsModel.setUrl(config.getUrl());
      config.jenkinsModel.setAdminAddress(config.getAdminAddress());
      config.instance.setNumExecutors(config.getExecutors());
      config.instance.save();

      for(JenkinsCredentials cred : config.getCredentials()) {
        cred.createJenkinsCredentials();
      }

      Utils.installPlugins(config.getPlugins(), config.isPluginsRestart());

      boolean crowdEnabled = false;
      if (config.getCrowd() != null) {
        if (Utils.pluginExist("crowd2")) {
          CrowdAction.enable(config.getCrowd(), config.instance);
          crowdEnabled = true;
        } else {
          LOG.warning("Autoconfig, Crowd 2 plugin (id: crowd2) is missing");
        }
      }

      for(JenkinsUser jenkinsUser : config.getJenkinsUsers()) {
        jenkinsUser.createJenkinsUser(config.instance, config.strategy, crowdEnabled);
      }

      config.instance.setAuthorizationStrategy(config.strategy);
      config.instance.save();

      if (Utils.pluginExist("kubernetes")) {
        for(Kubernetes cloud : config.getKubernetes()) {
          KubernetesAction.createCloud(cloud, config.instance, config.jenkinsModel.getUrl());
        }
      } else {
        LOG.warning("Autoconfig, Kubernetes plugin (id: kubernetes) is missing");
      }



      for(SeedJob seedJob : config.getSeedJobs()) {
        seedJob.createSeedJob();
      }
    }
  }
}
