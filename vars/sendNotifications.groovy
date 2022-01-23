#!/usr/bin/env groovy

def call(String buildResult) {
  if ( buildResult == "SUCCESS" ) {
    slackSend (color: "good", message: "[Grupo1][${env.JOB_NAME}][${BRANCH_NAME}][Resultado: OK]")
  }
  else ( buildResult == "FAILURE" ) {
    slackSend (color: "good", message: "[Grupo1][${env.JOB_NAME}][${env.GIT_BRANCH}][Resultado: Failed]")
  }
}



