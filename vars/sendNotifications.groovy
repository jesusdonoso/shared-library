#!/usr/bin/env groovy

def call(String buildResult) {
  if ( buildResult == "SUCCESS" ) {
    slackSend (color: "good", message: "[Grupo1][${env.JOB_NAME}][Resultado: OK]")
  }
  else if( buildResult == "FAILURE" ) {
    slackSend (color: "danger", message: "Job: ${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} was failed")
  }
  else if( buildResult == "UNSTABLE" ) {
    slackSend (color: "warning", message: "Job: ${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} was unstable")
  }
  else {
    slackSend (color: "danger", message: "Job: ${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} its resulat was unclear")
  }
}



