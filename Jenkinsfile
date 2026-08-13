pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }

        stage('Package') {
            steps {
                sh './mvnw package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t fleet-management-backend:latest .'
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }

        success {
            echo 'Backend CI completed successfully.'
        }

        failure {
            echo 'Backend CI failed.'
        }
    }
}