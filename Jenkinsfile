pipeline {

    agent any

    options {
        timeout(time: 60, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    tools {
        nodejs "node-25.8.0"
    }

    environment {
        IMAGE_NAME = ""
        PORT = ""
        CONTAINER_NAME = "nodeapp"
    }

    stages {

        stage('Checkout') {
             options {
                timeout(time: 2, unit: 'MINUTES')
            }
            steps {
                git credentialsId: 'github-ssh', url: 'git@github.com:EnCo-de/Jenkins-Continuous-integration-and-delivery.git'
            }
        }

        stage('Set Environment Variables') {
            steps {
                script {

                    switch(env.BRANCH_NAME) {

                        case "main":
                            IMAGE_NAME = "nodemain:v1.0"
                            PORT = "3000"
                            break

                        case "dev":
                            IMAGE_NAME = "nodedev:v1.0"
                            PORT = "3001"
                            break

                        default:
                            error("Unsupported branch: ${env.BRANCH_NAME}")
                    }
                }
            }
        }

        stage('Install Dependencies') {
            steps {
                sh 'npm install'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'npm test'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Deploy') {
            steps {
                sh """
                docker stop ${CONTAINER_NAME} || true
                docker rm ${CONTAINER_NAME} || true

                docker run -d \
                --name ${CONTAINER_NAME} \
                -p ${PORT}:3000 \
                ${IMAGE_NAME}
                """
            }
        }
    }

    post {

        success {
            echo "Deployment successful 🚀"
        }

        failure {
            echo "Pipeline failed ❌"
        }

        always {
            cleanWs()
        }
    }
}
