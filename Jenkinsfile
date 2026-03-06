pipeline {
    agent any

    tools {
        nodejs "NodeJS-7.8.0"
    }

    environment {
        IMAGE_NAME = ""
        PORT = ""
        PATH = "${env.HOME}/bin/docker:${env.PATH}"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'npm install'
            }
        }

        stage('Test') {
            steps {
                sh 'npm test'
            }
        }

        stage('Set Environment') {
            steps {
                script {
                    if (env.BRANCH_NAME == "main") {
                        IMAGE_NAME = "nodemain:v1.0"
                        PORT = "3000"
                    } else {
                        IMAGE_NAME = "nodedev:v1.0"
                        PORT = "3001"
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Stop Old Container') {
            steps {
                sh '''
                docker stop nodeapp || true
                docker rm nodeapp || true
                '''
            }
        }

        stage('Deploy') {
            steps {
                script {
                    if (env.BRANCH_NAME == "main") {
                        sh "docker run -d --name nodeapp --expose 3000 -p 3000:3000 nodemain:v1.0"
                    } else {
                        sh "docker run -d --name nodeapp --expose 3001 -p 3001:3000 nodedev:v1.0"
                    }
                }
            }
        }
    }
}
