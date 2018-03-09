package io.almariah.jenkins.plugins.autoconfig;

import hudson.Plugin;
import java.util.logging.Logger;
import java.io.IOException;
import jenkins.YesNoMaybe;
import hudson.Extension;
import hudson.init.InitMilestone;
import hudson.init.Initializer;

/**
 * @author Abdullah Almariah
 */

@Extension(dynamicLoadable=YesNoMaybe.NO)
public class Autoconfig extends Plugin {

  private final static Logger LOG = Logger.getLogger(Autoconfig.class.getName());

  @Override public void start() throws Exception, IOException {
    LOG.info("Starting Autoconfig...");
  }

  @Initializer(after = InitMilestone.JOB_LOADED)
  public static void poststart() throws Exception {
    ConfigAction.configure();
  }
}
