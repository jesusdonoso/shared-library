// cipipeline.groovy
def call(String) {

pipeline {
    agent any
    stages {
        stage("Paso 1: Download and checkout"){
            steps {
               checkout(
                        [$class: 'GitSCM',
                        branches: [[name: "develop" ]],
                        userRemoteConfigs: [[url: 'https://github.com/jesusdonoso/ms-iclab.git']]])
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
        stage("Paso 5: SonarQube analysis") {
            steps {
            withSonarQubeEnv('sonarqube') { // You can override the credential to be used
      sh "mvn clean verify sonar:sonar \
      -Dsonar.projectKey=feature-mundial"
    }
            withSonarQubeEnv('sonarqube') { // This expands the evironment variables SONAR_CONFIG_NAME, SONAR_HOST_URL, SONAR_AUTH_TOKEN that can be used by any script.
        println "${env.SONAR_HOST_URL}"
    }
            }
        }
        stage("Paso 6: Levantar Springboot APP"){
            steps {
                sh 'mvn spring-boot:run &'
            }
        }
        stage("Paso 7: Dormir(Esperar 60sg) "){
            steps {
                sh 'sleep 60'
            }
        }
        stage("Paso 8: Test Alive Service - Testing Application!"){
            steps {
                sh 'curl -X GET "http://localhost:8081/rest/mscovid/test?msg=testing"'
            }
        }
    }
    post {
        always {
            sh "echo 'fase always executed post'"
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