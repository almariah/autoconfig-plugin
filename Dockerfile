FROM jenkins/jenkins
COPY --chown=jenkins target/autoconfig.hpi /usr/share/jenkins/ref/plugins/autoconfig.jpi
COPY --chown=jenkins init.groovy /var/jenkins_home/init.groovy
