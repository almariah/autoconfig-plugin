package io.almariah.jenkins.plugins.autoconfig;

import java.lang.String;
import jenkins.model.Jenkins;

import java.util.List;
import java.io.IOException;
import hudson.PluginManager;
import hudson.model.UpdateCenter;

import hudson.model.UpdateSite;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import hudson.lifecycle.RestartNotSupportedException;


public class Utils {

  private static Jenkins instance = Jenkins.getInstance();

  public static boolean pluginExist(String plugin) {
    PluginManager pm = instance.getPluginManager();
    if (pm.getPlugin(plugin) == null) {
      return false;
    }
    return true;
  }

  public static void installPlugins(List<String> plugins, boolean pluginsRestart) throws IOException, InterruptedException, ExecutionException, RestartNotSupportedException {


    UpdateCenter uc = instance.getUpdateCenter();
    boolean installed = false;
    boolean initialized = false;

    for (String pluginName : plugins) {
      if (!pluginExist(pluginName)) {
        if (!initialized) {
          uc.updateAllSites();
          initialized = true;
        }
        UpdateSite.Plugin plugin = uc.getPlugin(pluginName);
        if (plugin != null) {
          Future<UpdateCenter.UpdateCenterJob> installFuture = plugin.deploy();
          while(!installFuture.isDone()) {
            Thread.sleep(3000);
          }
          installed = true;
        }
      }
    }

    if (installed) {
      instance.save();
      if (pluginsRestart) {
        instance.safeRestart();
      }
    }
  }
}
