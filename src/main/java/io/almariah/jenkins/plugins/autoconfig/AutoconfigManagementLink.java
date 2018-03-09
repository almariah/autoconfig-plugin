package io.almariah.jenkins.plugins.autoconfig;

import hudson.Extension;
import hudson.model.ManagementLink;

import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;

import org.apache.commons.lang.StringUtils;
import java.util.logging.Logger;
import net.sf.json.JSONObject;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.ByteArrayInputStream;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.Writer;

import hudson.Extension;
import hudson.model.RootAction;
import jenkins.model.Jenkins;
import hudson.security.Permission;
import java.io.IOException;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.interceptor.RequirePOST;
import javax.servlet.ServletException;
import java.lang.InterruptedException;
import java.util.concurrent.ExecutionException;
import hudson.lifecycle.RestartNotSupportedException;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.kohsuke.stapler.QueryParameter;
import hudson.util.FormValidation;
import org.kohsuke.stapler.Stapler;
import java.nio.charset.StandardCharsets;
import java.lang.ClassNotFoundException;
import io.almariah.jenkins.plugins.autoconfig.model.Config;

@Extension
public class AutoconfigManagementLink extends ManagementLink {

  public static final Permission ADMINISTER = Jenkins.ADMINISTER;

  private static final Logger LOGGER = Logger.getLogger(AutoconfigManagementLink.class.getName());

  private String yamlFile;

  public void setYamlFile(String yamlFile) {
    this.yamlFile = yamlFile;
  }

  public String getYamlFile() {
    return yamlFile;
  }

  public String getFullURL(){
    return Stapler.getCurrentRequest().getOriginalRequestURI().substring(1);
  }

  public String getYamlPath(){
    return System.getProperty("user.home") + "/jenkins.yaml";
  }

  public AutoconfigManagementLink() throws java.io.IOException {
    // if yaml file not exist
    yamlFile = new String(Files.readAllBytes(Paths.get(getYamlPath())));
  }

  @Override
  public String getDescription() {
    return "Update/apply Autoconfig YAML file";
  }

  public String getDisplayName() {
    return "Autoconfig";
  }

  @Override
  public String getIconFileName() {
    return "/plugin/autoconfig/images/48x48/auto_config.png";
  }

  @Override
  public String getUrlName() {
    return "autoconfig";
  }


  @RequirePOST
  @SuppressWarnings("unused")
  public void doConfigure(StaplerRequest req, StaplerResponse rsp) throws Exception, ServletException, IOException, InterruptedException, ExecutionException, RestartNotSupportedException {

    Jenkins.getInstance().checkPermission(Jenkins.ADMINISTER);

    String homeDir = System.getProperty("user.home");

    JSONObject form = req.getSubmittedForm();
    String yaml = form.getString("yamlFile");

    // validator
    // fix try
    try (Writer writer = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(homeDir + "/jenkins.yaml"), "utf-8"))) {
      writer.write(yaml);
    }

    setYamlFile(yaml);
    ConfigAction.configure();
    // need fix
    rsp.forward(this, "_restart", req);
  }

  public FormValidation doValidateConfig(@QueryParameter("yamlFile") String yamlText) throws Exception {
    if (yamlText.isEmpty()) {
      return FormValidation.error("yaml config is required");
    }

    try {

      CustomClassLoaderConstructor constr = new CustomClassLoaderConstructor(Config.class.getClassLoader());
      Yaml yaml = new Yaml(constr);
      InputStream in = new ByteArrayInputStream(yamlText.getBytes(StandardCharsets.UTF_8));
      Config config =  yaml.loadAs(in, Config.class);
      Validator.validate(config);

    } catch (Exception e) {

      LOGGER.log(Level.FINE, "Error validating config", e);
      return FormValidation.error("Error validating config %s", e);

    }

    return FormValidation.ok("Autoconfig: Configuration is okay");
  }
}
