package io.almariah.jenkins.plugins.autoconfig.model;

import com.cloudbees.plugins.credentials.CredentialsStore;
import com.cloudbees.plugins.credentials.SystemCredentialsProvider;

import com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey;
import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl;
import com.cloudbees.plugins.credentials.domains.Domain;
import jenkins.model.Jenkins;

import java.io.IOException;
import hudson.ExtensionList;

import com.cloudbees.plugins.credentials.CredentialsProvider;

import org.jenkinsci.plugins.plaincredentials.impl.FileCredentialsImpl;
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl;
import hudson.util.Secret;
import com.cloudbees.plugins.credentials.SecretBytes;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

import javax.validation.constraints.NotNull;

public class JenkinsCredentials {

  private String type;
  private String id;
  private String description = "";
  private String username;
  private String password;
  private String sshKey;
  private String sshKeyFile;
  private String passphrase = "";
  private String secretFile;
  private String secretText;

  public JenkinsCredentials() {
  }

  public String getType() {
        return type;
  }

  public String getId() {
        return id;
  }

  public String getDescription() {
        return description;
  }

  public String getUsername() {
        return username;
  }

  public String getPassword() {
        return password;
  }

  public String getSshKey() {
        return sshKey;
  }

  public String getSshKeyFile() {
        return sshKeyFile;
  }

  public String getPassphrase() {
        return passphrase;
  }

  public String getSecretFile() {
        return secretFile;
  }

  public String getSecretText() {
        return secretText;
  }

  public void setType(String type) {
        this.type = type;
  }

  public void setId(String id) {
        this.id = id;
  }

  public void setDescription(String description) {
        this.description = description;
  }

  public void setUsername(String username) {
        this.username = username;
  }

  public void setPassword(String password) {
        this.password = password;
  }

  public void setSshKey(String sshKey) {
        this.sshKey = sshKey;
  }

  public void setSshKeyFile(String sshKeyFile) {
        this.sshKeyFile = sshKeyFile;
  }

  public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
  }

  public void setSecretFile(String secretFile) {
        this.secretFile = secretFile;
  }

  public void setSecretText(String secretText) {
        this.secretText = secretText;
  }

  public void createJenkinsCredentials() throws IOException {

    final SystemCredentialsProvider.ProviderImpl systemProvider = ExtensionList.lookup(CredentialsProvider.class)
                .get(SystemCredentialsProvider.ProviderImpl.class);
    if (systemProvider == null) return;

    final CredentialsStore credentialsStore = systemProvider.getStore(Jenkins.getInstance());
    if (credentialsStore == null) return;

    switch (this.type) {
      case "SecretFile":
          Path fileLocation = Paths.get(getSecretFile());
          credentialsStore.addCredentials(Domain.global(), new FileCredentialsImpl(
              CredentialsScope.GLOBAL,
              getId(),
              getDescription(),
              getSecretFile(),
              SecretBytes.fromBytes(Files.readAllBytes(fileLocation))));
      case "SecretText":
          credentialsStore.addCredentials(Domain.global(), new StringCredentialsImpl(
             CredentialsScope.GLOBAL,
             getId(),
             getDescription(),
             Secret.fromString(getSecretText())));
      case "UsernamePassword":
          credentialsStore.addCredentials(Domain.global(), new UsernamePasswordCredentialsImpl(
              CredentialsScope.GLOBAL,
              getId(),
              getDescription(),
              getUsername(),
              getPassword()));
      case "SSHKey":
          if (getSshKeyFile() != null) {
            credentialsStore.addCredentials(Domain.global(), new BasicSSHUserPrivateKey(
                CredentialsScope.GLOBAL,
                getId(),
                getUsername(),
                new BasicSSHUserPrivateKey.FileOnMasterPrivateKeySource(getSshKeyFile()),
                getPassphrase(),
                getDescription()));
          } else if (getSshKey() != null) {
            credentialsStore.addCredentials(Domain.global(), new BasicSSHUserPrivateKey(
                CredentialsScope.GLOBAL,
                getId(),
                getUsername(),
                new BasicSSHUserPrivateKey.DirectEntryPrivateKeySource(getSshKey()),
                getPassphrase(),
                getDescription()));
          }
    }
  }
}
