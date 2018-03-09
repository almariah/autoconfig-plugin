import jenkins.model.Jenkins;

def instance = Jenkins.getInstance()

def requiredPlugins = ["git",
                       "mailer",
                       "matrix-auth",
                       "credentials",
                       "ssh-credentials",
                       "plain-credentials"]

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
