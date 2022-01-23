#!/usr/bin/env groovy

def call(String buildResult) {
  if ( buildResult == "SUCCESS" ) {
    slackSend (color: "good", message: "[Grupo1][${env.JOB_NAME}][${env.BRANCH_NAME}][Resultado: OK][${env.STAGE_NAME}]")
  }
  else ( buildResult == "FAILURE" ) {
    slackSend (color: "good", message: "[Grupo1][${env.JOB_NAME}][${env.GIT_BRANCH}][Resultado: Failed]")
  }
}



