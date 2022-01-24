#!/usr/bin/env groovy
def call(String buildResult) {
  if ( buildResult == "SUCCESS" ) {
    slackSend (color: "good", message: "[Grupo1][${env.JOB_NAME}][${env.BRANCH_NAME}][Resultado: OK]")
  }
  if( buildResult == "FAILURE" ) {
    slackSend (color: "danger", message: "[Grupo1][${env.JOB_NAME}][${env.GIT_BRANCH}][Resultado: Failed][${env.LAST_STAGE_NAME}]")
  }
}