package io.almariah.jenkins.plugins.autoconfig;

import java.lang.String;
import jenkins.model.Jenkins;
import org.csanchez.jenkins.plugins.kubernetes.KubernetesCloud;
import java.io.IOException;

public class KubernetesAction {

  public static void createCloud(Kubernetes cloud, Jenkins instance, String jenkinsUrl) throws IOException {

    KubernetesCloud k8s = new KubernetesCloud(
      cloud.getName(),
      null,
      cloud.getServerUrl(),
      cloud.getNamespace(),
      jenkinsUrl,
      String.valueOf(cloud.getContainerCap()),
      5, 15, 5);

    k8s.setMaxRequestsPerHostStr(String.valueOf(cloud.getMaxRequests()));
    k8s.setSkipTlsVerify(cloud.isSkipTLS());
    k8s.setCredentialsId(cloud.getCredentialsId());

    instance.clouds.replace(k8s);
    instance.save();
  }
}
