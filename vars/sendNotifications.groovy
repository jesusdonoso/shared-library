#!/usr/bin/env groovy

def call(String buildResult) {
  if ( buildResult == "SUCCESS" ) {
    slackSend (color: "good", message: "[Grupo1][${env.JOB_NAME}][${env.BRANCH_NAME}][Resultado: OK]")
  }
  else if( buildResult == "FAILURE" ) { 
    slackSend (color: "danger", message: "[Grupo1][${env.JOB_NAME}][${env.GIT_BRANCH}][Resultado: Failed][${FAILED_STAGE}]")
  }
  else if( buildResult == "UNSTABLE" ) { 
    slackSend (color: "danger", message: "[Grupo1][${env.JOB_NAME}][${env.GIT_BRANCH}][Resultado: Unstable][${FAILED_STAGE}]")
  }
  else {
    slackSend (color: "danger", message: "[Grupo1][${env.JOB_NAME}][${env.GIT_BRANCH}][Resultado: Unclear][${FAILED_STAGE}]")
  }
}