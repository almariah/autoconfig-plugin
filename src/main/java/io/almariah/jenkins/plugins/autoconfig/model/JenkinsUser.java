package io.almariah.jenkins.plugins.autoconfig.model;

import java.lang.String;
import hudson.model.User;
import jenkins.model.Jenkins;
import hudson.security.HudsonPrivateSecurityRealm;
import hudson.security.GlobalMatrixAuthorizationStrategy;
import java.io.IOException;



public class JenkinsUser {
  private String username;
  private String password;
  private String fullName;
  private String email;

  public JenkinsUser() {
  }

  public String getUsername() {
        return username;
  }

  public String getPassword() {
        return password;
  }

  public String getFullName() {
        return fullName;
  }
  public String getEmail() {
        return email;
  }

  public void setUsername(String username) {
        this.username = username;
  }

  public void setPassword(String password) {
        this.password = password;
  }

  public void setFullName(String fullName) {
        this.fullName = fullName;
  }
  public void setEmail(String email) {
        this.email = email;
  }

  public void createJenkinsUser(Jenkins instance, GlobalMatrixAuthorizationStrategy strategy, boolean crowdEnabled) throws IOException {

    if (!crowdEnabled) {
      HudsonPrivateSecurityRealm hudsonRealm = new HudsonPrivateSecurityRealm(false);
      hudsonRealm.createAccount(this.username, this.password);
      instance.setSecurityRealm(hudsonRealm);
      instance.save();
      User user = hudson.model.User.getById(this.username, false);
      user.setFullName(this.fullName);
      user.addProperty(new hudson.tasks.Mailer.UserProperty(this.email));
      user.save();
    }

    strategy.add(Jenkins.ADMINISTER, getUsername());

  }
}
