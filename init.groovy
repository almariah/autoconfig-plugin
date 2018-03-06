import jenkins.model.Jenkins;

def instance = Jenkins.getInstance()

def requiredPlugins = ["pipeline-utility-steps",
                       "bouncycastle-api",
                       "structs",
                       "credentials",
                       "plain-credentials",
                       "kubernetes-credentials",
                       "variant",
                       "workflow-step-api",
                       "durable-task",
                       "kubernetes",
                       "ace-editor",
                       "jquery-detached",
                       "workflow-scm-step",
                       "scm-api",
                       "script-security",
                       "workflow-api",
                       "workflow-support",
                       "workflow-cps",
                       "display-url-api",
                       "mailer",
                       "matrix-auth",
                       "apache-httpcomponents-client-4-api",
                       "ssh-credentials",
                       "jsch",
                       "git-client",
                       "junit",
                       "matrix-project",
                       "git",
                       "job-dsl"]

def installed = false
def initialized = false
def pm = instance.getPluginManager()
def uc = instance.getUpdateCenter()
requiredPlugins.each {
  if (!pm.getPlugin(it)) {
    if (!initialized) {
      uc.updateAllSites()
      initialized = true
    }
    def plugin = uc.getPlugin(it)
    if (plugin) {
    	def installFuture = plugin.deploy()
      while(!installFuture.isDone()) {
        sleep(3000)
      }
      installed = true
    }
  }
}

if (installed) {
  instance.save()
  instance.doSafeRestart()
}
