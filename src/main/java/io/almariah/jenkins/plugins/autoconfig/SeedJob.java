package io.almariah.jenkins.plugins.autoconfig;


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

  public void createSeedJob() throws IOException, InterruptedException, ExecutionException {

    Jenkins instance = Jenkins.getInstance();

    String seedJobName = "Autoconfig Seed Job - " + getJobName();

    if ( instance.getItem(seedJobName) == null ) {
      FreeStyleProject dslProject = new FreeStyleProject(instance, seedJobName);

      dslProject.setDescription("This job is auto-generated to create Jenkins jobs that is specified in Jenkinsjobs file");

      GitSCM gitSCM = new GitSCM(
        Arrays.asList(new UserRemoteConfig(this.repository, null, null, this.credentialsId)),
        Arrays.asList(new BranchSpec(this.branch)),
        false,
        Collections.<SubmoduleConfig>emptyList(),
        null,
        null,
        Collections.<GitSCMExtension>emptyList());

      dslProject.setScm(gitSCM);

      ExecuteDslScripts jobDslBuildStep = new ExecuteDslScripts();
      jobDslBuildStep.setIgnoreExisting(false);
      jobDslBuildStep.setLookupStrategy(LookupStrategy.JENKINS_ROOT);
      jobDslBuildStep.setRemovedJobAction(RemovedJobAction.DELETE);
      jobDslBuildStep.setRemovedViewAction(RemovedViewAction.DELETE);
      jobDslBuildStep.setTargets(this.fileName);

      dslProject.getBuildersList().add(jobDslBuildStep);

      instance.add(dslProject, seedJobName);
      instance.save();
    }

    FreeStyleProject project = (FreeStyleProject) instance.getItem(seedJobName);
    project.scheduleBuild();
    //while(!buildFuture.isDone()) {
    //  Thread.sleep(3000);
    //}
  }

}

/*
def job = jenkins.getItem(initConf.dslJobs.jobName)
job.scheduleBuild()
sleep(10000)

Logger logger = Logger.getLogger("")

while(job.isBuilding()) {
  sleep(3000)
  logger.info("waiting for dsl jobs")
}
sleep(3000)


ScriptApproval sa = ScriptApproval.get();

for (ScriptApproval.PendingScript pending : sa.getPendingScripts()) {
       	sa.approveScript(pending.getHash());
}

sleep(3000)

job.scheduleBuild()
*/
