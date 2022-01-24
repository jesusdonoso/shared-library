// mypipeline.groovy
def call(String) {

pipeline {
    agent any
    environment {
        JENKINSTOKEN = credentials('github-token')
    }
    stages {
        stage("Paso 1: Download and checkout"){
            steps {
               checkout(
                        [$class: 'GitSCM',
                        branches: [[name: "shared-library" ]],
                        userRemoteConfigs: [[url: 'https://github.com/jesusdonoso/ejemplo-maven.git']]])
            }
        }
        stage("Paso 2: Compliar"){
            steps {
                script {
                sh "echo 'Compile Code!'"
                // Run Maven on a Unix agent.
                sh "mvn clean compile -e"
                }
            }
        }
        stage("Paso 3: Testear"){
            steps {
                script {
                sh "echo 'Test Code!'"
                // Run Maven on a Unix agent.
                sh "mvn clean test -e"
                }
            }
        }
        stage("Paso 4: Build .Jar"){
            steps {
                script {
                sh "echo 'Build .Jar!'"
                // Run Maven on a Unix agent.
                sh "mvn clean package -e"
                }
            }
        }
        stage("SonarQube analysis") {
            steps {
            withSonarQubeEnv('sonarqube') { // You can override the credential to be used
      sh "mvn clean verify sonar:sonar \
      -Dsonar.projectKey=shared-library"
    }
            withSonarQubeEnv('sonarqube') { // This expands the evironment variables SONAR_CONFIG_NAME, SONAR_HOST_URL, SONAR_AUTH_TOKEN that can be used by any script.
        println "${env.SONAR_HOST_URL}"
    }
            }
        }
        stage("create branch") {
            steps {
            SHA=$(curl -s -H "Authorization: token $JENKINSTOKEN" https://api.github.com/repos/jesusdonoso/ejemplo-maven/git/refs/heads/shared-library | jq -r .object.sha)
            sh """curl -s -X POST -H "Authorization: token $JENKINSTOKEN" -H "Content-Type: application/json" -d '{"ref": "refs/heads/test-rama", "sha": "'$SHA'"}'  https://api.github.com/repos/jesusdonoso/ejemplo-maven/git/refs"""
            sh """curl -X POST -d '{"head":"release","base":"shared-library"}' -H "Accept 'application/vnd.github.v3+json'" -H "Authorization: token $JENKINSTOKEN" https://api.github.com/repos/jesusdonoso/ejemplo-maven/merges"""
        }
    }
    }
    post {
        always {
            sh "echo 'fase always executed post'"
            /* Use slackNotifier.groovy from shared library and provide current build result as parameter */
            sendNotifications currentBuild.result
        }
        success {
            sh "echo 'fase success'"
        }
        failure {
            sh "echo 'fase failure'"
        }
    }
}
}