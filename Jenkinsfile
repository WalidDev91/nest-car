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

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh './mvnw verify sonar:sonar -Dsonar.projectKey=fleet-management-backend -Dsonar.projectName=fleet-management-backend'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Package') {
            steps {
                sh './mvnw package -DskipTests'
            }
        }

        stage('Publish to Nexus') {
            steps {
                configFileProvider([
                    configFile(
                        fileId: 'nexus-maven-settings',
                        variable: 'MAVEN_SETTINGS'
                    )
                ]) {
                    sh './mvnw deploy -DskipTests -s "$MAVEN_SETTINGS"'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t fleet-management-backend:latest .'
                sh "docker tag fleet-management-backend:latest ghcr.io/waliddev91/fleet-management-backend:build-${env.BUILD_NUMBER}"
                sh 'docker tag fleet-management-backend:latest ghcr.io/waliddev91/fleet-management-backend:latest'
            }
        }

        stage('Push to GHCR') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'github-ghcr',
                    usernameVariable: 'GHCR_USERNAME',
                    passwordVariable: 'GHCR_TOKEN'
                )]) {
                    retry(3) {
                        sh '''
                            echo "$GHCR_TOKEN" | docker login ghcr.io -u "$GHCR_USERNAME" --password-stdin
                            docker push ghcr.io/waliddev91/fleet-management-backend:build-${BUILD_NUMBER}
                        '''
                    }
                    retry(3) {
                        sh '''
                            echo "$GHCR_TOKEN" | docker login ghcr.io -u "$GHCR_USERNAME" --password-stdin
                            docker push ghcr.io/waliddev91/fleet-management-backend:latest
                        '''
                    }
                    sh 'docker logout ghcr.io'
                }
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }

        success {
            echo 'Backend CI completed successfully and Docker images were pushed to GHCR.'
        }

        failure {
            echo 'Backend CI failed.'
        }
    }
}