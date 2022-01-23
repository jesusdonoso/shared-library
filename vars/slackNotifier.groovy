#!/usr/bin/env groovy

def call(String buildResult) {
  if ( buildResult == "SUCCESS" ) {
    slackSend message "paso la wea"
  }
  else ( buildResult == "FAILURE" ) {
    slackSend message "falla"
  }
}