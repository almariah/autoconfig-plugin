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
import io.almariah.jenkins.plugins.autoconfig.model.Config;
import io.almariah.jenkins.plugins.autoconfig.model.JenkinsCredentials;
import io.almariah.jenkins.plugins.autoconfig.model.JenkinsUser;
import io.almariah.jenkins.plugins.autoconfig.model.SeedJob;
import io.almariah.jenkins.plugins.autoconfig.model.Kubernetes;

public class ConfigAction {

  private final static Logger LOG = Logger.getLogger(ConfigAction.class.getName());

  private static Jenkins instance = Jenkins.getInstance();
  private static JenkinsLocationConfiguration jenkinsModel = new JenkinsLocationConfiguration();
  private static GlobalMatrixAuthorizationStrategy strategy = new GlobalMatrixAuthorizationStrategy();

  public static void configure() throws Exception, IOException, InterruptedException, ExecutionException, RestartNotSupportedException {

    CustomClassLoaderConstructor constr = new CustomClassLoaderConstructor(Config.class.getClassLoader());

    Yaml yaml = new Yaml(constr);
    String homeDir = System.getProperty("user.home");
    // fix try
    // if yaml does not exist
    File yamlFile = new File(homeDir + "/jenkins.yaml");
    if (yamlFile.isFile() && yamlFile.canRead()) {
      try (InputStream in = new FileInputStream(yamlFile)) {
        Config config = null;
        try {
          config =  yaml.loadAs(in, Config.class);
          Validator.validate(config);
        } catch (Exception e) {
          LOG.warning(e.getMessage());
        }

        jenkinsModel.setUrl(config.getUrl());
        jenkinsModel.setAdminAddress(config.getAdminAddress());
        instance.setNumExecutors(config.getExecutors());
        instance.save();

        if (config.getCredentials() != null) {
          for(JenkinsCredentials cred : config.getCredentials()) {
            cred.createJenkinsCredentials();
          }
        }

        if (config.getPlugins() != null) {
          Plugins.installPlugins(config.getPlugins(), config.isPluginsRestart());
        }

        boolean crowdEnabled = false;
        if (config.getCrowd() != null) {
          if (Plugins.pluginExist("crowd2")) {
            CrowdAction.enable(config.getCrowd(), instance);
            crowdEnabled = true;
          } else {
            LOG.warning("Autoconfig, Crowd 2 plugin (id: crowd2) is missing");
          }
        }

        // check if getJenkinsUsers() is null
        for(JenkinsUser jenkinsUser : config.getJenkinsUsers()) {
          jenkinsUser.createJenkinsUser(instance, strategy, crowdEnabled);
        }

        instance.setAuthorizationStrategy(strategy);
        instance.save();

        if (config.getKubernetes() != null) {
          if (Plugins.pluginExist("kubernetes")) {
            KubernetesAction.createClouds(config.getKubernetes(), instance, jenkinsModel.getUrl());
          } else {
            LOG.warning("Autoconfig, Kubernetes plugin (id: kubernetes) is missing");
          }
        }

        if (config.getSeedJobs() != null) {
          if (Plugins.pluginExist("job-dsl")) {
            SeedJobAction.createSeedJobs(config.getSeedJobs());
          } else {
            LOG.warning("Autoconfig, Job DSL plugin (id: job-dsl) is missing");
          }
        }
      }
    } else {
      LOG.warning(String.format("Autoconfig, '%s' does not exist", homeDir + "/jenkins.yaml"));
    }
  }
}
