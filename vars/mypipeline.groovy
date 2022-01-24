// mypipeline.groovy
def call(String) {
environment {
        GIT_USER         = credentials('user-github')
        GIT_PASSWORD     = credentials('pass-github')
    }
pipeline {
    agent any
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
            sh "git checkout -b release1"
            sh("git push origin release/test3")
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