pipeline {
    agent any

    tools {
        nodejs "node-25.8.0"
    }

    environment {
        IMAGE_NAME = ""
        PORT = ""
    }

    stages {

        stage('Checkout') {
            steps {
                git credentialsId: 'github-ssh', url: 'git@github.com:EnCo-de/Jenkins-Continuous-integration-and-delivery.git'
            }
        }

        stage('Set Environment Variables') {
            steps {
                script {

                    if (env.BRANCH_NAME == "main") {
                        IMAGE_NAME = "nodemain:v1.0"
                        PORT = "3000"
                    }

                    if (env.BRANCH_NAME == "dev") {
                        IMAGE_NAME = "nodedev:v1.0"
                        PORT = "3001"
                    }

                }
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

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Deploy') {
            steps {
                script {

                    sh '''
                    docker stop nodeapp || true
                    docker rm nodeapp || true
                    '''

                    if (env.BRANCH_NAME == "main") {

                        sh '''
                        docker run -d \
                        --name nodeapp \
                        -p 3000:3000 \
                        nodemain:v1.0
                        '''

                    }

                    if (env.BRANCH_NAME == "dev") {

                        sh '''
                        docker run -d \
                        --name nodeapp \
                        -p 3001:3000 \
                        nodedev:v1.0
                        '''

                    }

                }
            }
        }
    }
}
