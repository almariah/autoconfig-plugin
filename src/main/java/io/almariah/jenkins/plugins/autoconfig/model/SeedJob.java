package io.almariah.jenkins.plugins.autoconfig.model;


import java.lang.String;
import hudson.model.FreeStyleProject;
import hudson.plugins.git.GitSCM;
import hudson.plugins.git.BranchSpec;

import javaposse.jobdsl.plugin.LookupStrategy;
import javaposse.jobdsl.plugin.ExecuteDslScripts;
import javaposse.jobdsl.plugin.RemovedJobAction;
import javaposse.jobdsl.plugin.RemovedViewAction;
import jenkins.model.Jenkins;

import java.util.Arrays;
import hudson.plugins.git.UserRemoteConfig;

import hudson.plugins.git.SubmoduleConfig;
import java.util.Collections;
import hudson.plugins.git.extensions.GitSCMExtension;
import java.io.IOException;
import java.lang.InterruptedException;
import java.util.concurrent.ExecutionException;

public class SeedJob {
  
  private String credentialsId;
  private String branch;
  private String repository;
  private String fileName;
  private String jobName;

  public String getCredentialsId() {
        return credentialsId;
  }

  public String getRepository() {
        return repository;
  }

  public String getBranch() {
        return branch;
  }

  public String getFileName() {
        return fileName;
  }

  public String getJobName() {
        return jobName;
  }

  public void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId;
  }

  public void setRepository(String repository) {
        this.repository = repository;
  }

  public void setBranch(String branch) {
        this.branch = branch;
  }

  public void setFileName(String fileName) {
        this.fileName = fileName;
  }

  public void setJobName(String jobName) {
        this.jobName = jobName;
  }
}
