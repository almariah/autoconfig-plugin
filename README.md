# autoconfig-plugin

## Overview

A plugin that configure Jenkins form YAML file

## Example
```yaml
url: http://jenkins.example.net/
adminAddress: "Abdullah Almariah <almariah@example.com>"
executors: 6
#crowd:
#  url: https://jira.example.com
#  appName: jenkins
#  appPassword: "..."
#  restrictGroups: jenkins-users
jenkinsUsers:
  - username: admin
    password: admin
    fullName: "Admin Admin"
    email: admin@example.com
pluginsRestart: true
plugins:
  - github-branch-source
  - pam-auth
  - timestamper
  - ...
#  - crowd2
seedJobs:
  - credentialsId: 7117f9a6-8808-40db-b2f5-f44ce3d7a180
    branch: master
    repository: ssh://git@git.example.com/jenkins.git
    fileName: Jenkinsjobs
    jobName: create-dsl-jobs
kubernetes:
  - name: kubernetes
    namespace: ci-cd
credentials:
  - type: "UsernamePassword"
    username: "admin"
    password: "4vx6wrw6618902cd8ge8267e2ef"
    id: 7117f9a6-8808-40db-b2f5-f44ce3d7a180
    description: "Access of service foo"
```

## Dependancies:

* git v3.7.0
* script-security v1.40
* workflow-cps v2.45
* mailer v1.20
* credentials v2.1.16
* structs v1.14
* job-dsl v1.67
* pipeline-utility-steps v2.0.1
* durable-task v1.16
* variant v1.0
* matrix-auth v2.2
* ssh-credentials v1.13

## To do:
* fine grained permissions
* credentials scopes
