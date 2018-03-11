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
import io.almariah.jenkins.plugins.autoconfig.model.SeedJob;
import java.util.List;

public class SeedJobAction {

  public static void createSeedJobs(List<SeedJob> seedJobs) throws IOException, InterruptedException, ExecutionException {
    for(SeedJob seedJob : seedJobs) {
      createSeedJob(seedJob);
    }
  }

  public static void createSeedJob(SeedJob seedJob) throws IOException, InterruptedException, ExecutionException {

    Jenkins instance = Jenkins.getInstance();

    String seedJobName = "Autoconfig Seed Job - " + seedJob.getJobName();

    if ( instance.getItem(seedJobName) == null ) {
      FreeStyleProject dslProject = new FreeStyleProject(instance, seedJobName);

      dslProject.setDescription("This job is auto-generated to create Jenkins jobs that is specified in Jenkinsjobs file");

      GitSCM gitSCM = new GitSCM(
        Arrays.asList(new UserRemoteConfig(seedJob.getRepository(), null, null, seedJob.getCredentialsId())),
        Arrays.asList(new BranchSpec(seedJob.getBranch())),
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
      jobDslBuildStep.setTargets(seedJob.getFileName());

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
