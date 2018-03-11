package io.almariah.jenkins.plugins.autoconfig;

import java.lang.String;
import java.util.List;
import io.almariah.jenkins.plugins.autoconfig.model.*;

public class Validator {

  public static void validate(Config config) throws Exception {
    if (config.getCrowd() != null) {
      validateCrowd(config.getCrowd(), config.getPlugins());
    }
    if (config.getJenkinsUsers() != null) {
      validateJenkinsUsers(config.getJenkinsUsers(), config.getCrowd());
    }
    if (config.getCredentials()!= null) {
      validateJenkinsCreds(config.getCredentials());
    }
    if (config.getKubernetes() != null) {
      validateKubernetesClouds(config.getKubernetes(), config.getPlugins());
    }
    if (config.getSeedJobs() != null) {
      validateSeedJobs(config.getSeedJobs(), config.getPlugins());
    }
  }

  public static void validateCrowd(Crowd2 crowd, List<String> plugins) throws Exception {

    if (!Plugins.pluginWillExist("crowd2", plugins)) {
      throw new Exception("Autoconfig: Crowd 2 plugin (id: crowd2) is missing");
    }

    if (crowd.getUrl() == null) {
      throw new Exception("Autoconfig 'jenkins.yaml', crowd should have url");
    }
    if (crowd.getAppName() == null) {
      throw new Exception("Autoconfig 'jenkins.yaml', crowd should have appName");
    }
    if (crowd.getAppPassword() == null) {
      throw new Exception("Autoconfig 'jenkins.yaml', crowd should have appPassword");
    }
  }

  public static void validateJenkinsUsers(List<JenkinsUser> jenkinsUsers, Crowd2 crowd) throws Exception {
    boolean isAdminExist = false;

    for (JenkinsUser jenkinsUser : jenkinsUsers) {
      if (jenkinsUser.getUsername().equals("admin")) {
        isAdminExist = true;
      }
      validateJenkinsUser(jenkinsUser, crowd);
    }
    if (!isAdminExist) {
      throw new Exception("Autoconfig 'jenkins.yaml', admin user and password does not exist");
    }
  }

  public static void validateJenkinsUser(JenkinsUser jenkinsUser, Crowd2 crowd) throws Exception {
    if (jenkinsUser.getUsername() == null) {
      throw new Exception("Autoconfig 'jenkins.yaml', user should have name");
    }
    if (crowd == null && jenkinsUser.getPassword() == null) {
      throw new Exception("Autoconfig 'jenkins.yaml', user should have password");
    }
  }

  public static void validateJenkinsCreds(List<JenkinsCredentials> jenkinsCreds) throws Exception {
    for (JenkinsCredentials jenkinsCred : jenkinsCreds) {
      validateJenkinsCredentials(jenkinsCred);
    }
  }

  public static void validateJenkinsCredentials(JenkinsCredentials c) throws Exception {

    String type = c.getType();

    if (type == null || c.getId() == null) {
      throw new Exception("Autoconfig 'jenkins.yaml', credentials should have type and id");
    }

    if (type.equals("SecretFile") && c.getSecretFile() == null) {
      throw new Exception("Autoconfig 'jenkins.yaml', credentials of type 'SecretFile' should have secretFile");
    }

    if (type.equals("SecretText") && c.getSecretText() == null) {
      throw new Exception("Autoconfig 'jenkins.yaml', credentials of type 'SecretText' should have secretText");
    }

    if (type.equals("UsernamePassword") && (c.getUsername() == null || c.getPassword() == null)) {
      throw new Exception("Autoconfig 'jenkins.yaml', credentials of type 'UsernamePassword' should have username and password");
    }

    if (type.equals("SSHKey") && c.getUsername() == null) {
      throw new Exception("Autoconfig 'jenkins.yaml', credentials of type 'SSHKey' should have username");
    }

    if (type.equals("SSHKey") && c.getSshKeyFile() != null && c.getSshKey() != null) {
      throw new Exception("Autoconfig 'jenkins.yaml', credentials of type 'SSHKey' can't have have both sshKeyFile and sshKey");
    }

    if (type.equals("SSHKey") && c.getSshKeyFile() == null && c.getSshKey() == null) {
      throw new Exception("Autoconfig 'jenkins.yaml', credentials of type 'SSHKey' should have sshKeyFile or sshKey");
    }

    // throw new Exception(String.format("Autoconfig 'jenkins.yaml', %s is invalid type", type));
  }

  public static void validateKubernetesClouds(List<Kubernetes> clouds, List<String> plugins) throws Exception {

    if (!Plugins.pluginWillExist("kubernetes", plugins)) {
      throw new Exception("Autoconfig: Kubernetes plugin (id: kubernetes) is missing");
    }

    for (Kubernetes cloud : clouds) {
      validateKubernetesCloud(cloud);
    }
  }

  public static void validateKubernetesCloud(Kubernetes cloud) throws Exception {
    if (cloud.getName() == null || cloud.getNamespace() == null) {
      throw new Exception("Autoconfig 'jenkins.yaml', kubernetes cloud should have name and namespace");
    }
  }

  public static void validateSeedJobs(List<SeedJob> jobs, List<String> plugins) throws Exception {

    if (!Plugins.pluginWillExist("job-dsl", plugins)) {
      throw new Exception("Autoconfig, Job DSL plugin (id: job-dsl) is missing");
    }

    for (SeedJob job : jobs) {
      validateSeedJob(job);
    }
  }

  public static void validateSeedJob(SeedJob job) throws Exception {

    if (job.getRepository() == null) {
      throw new Exception("Autoconfig 'jenkins.yaml', seedJob should have repository");
    }

    if (job.getBranch() == null) {
      throw new Exception("Autoconfig 'jenkins.yaml', seedJob should have branch");
    }

    if (job.getFileName() == null) {
      throw new Exception("Autoconfig 'jenkins.yaml', seedJob should have fileName");
    }

    if (job.getJobName() == null) {
      throw new Exception("Autoconfig 'jenkins.yaml', seedJob should have is jobName");
    }
  }
}
