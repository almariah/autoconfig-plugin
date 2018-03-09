package io.almariah.jenkins.plugins.autoconfig;

import jenkins.model.Jenkins;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import de.theit.jenkins.crowd.CrowdSecurityRealm;
import jenkins.model.Jenkins;
import java.io.IOException;
import io.almariah.jenkins.plugins.autoconfig.model.Crowd2;

public class CrowdAction {

  public static void enable(Crowd2 crowd, Jenkins instance) throws IOException{

    CrowdSecurityRealm crowd2Realm = new CrowdSecurityRealm(
      crowd.getUrl(),
      crowd.getAppName(),
      crowd.getAppPassword(),
      crowd.getRestrictGroups(),
      false, 0, false, "", "", false, "", "", "", "", "", "", "");

    instance.setSecurityRealm(crowd2Realm);
    instance.save();
  }
}
