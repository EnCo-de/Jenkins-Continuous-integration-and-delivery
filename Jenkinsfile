pipeline {
    agent any

    tools {
        nodejs 'NodeJS-7.8.0' // Make sure this NodeJS version is configured in Jenkins
    }

    environment {
        IMAGE_NAME = ""
        PORT = ""
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

        stage('Set Branch Environment') {
            steps {
                script {
                    if (env.BRANCH_NAME == "main") {
                        IMAGE_NAME = "nodemain:v1.0"
                        PORT = "3000"
                    } else {
                        IMAGE_NAME = "nodedev:v1.0"
                        PORT = "3001"
                    }
                    echo "Branch: ${env.BRANCH_NAME}, Image: ${IMAGE_NAME}, Port: ${PORT}"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker image ${IMAGE_NAME}"
                    export PATH=$HOME/bin/docker:$PATH
                    docker.build(IMAGE_NAME)
                }
            }
        }

        stage('Deploy Container') {
            steps {
                script {
                    echo "Deploying container ${IMAGE_NAME} on port ${PORT}"
                    export PATH=$HOME/bin/docker:$PATH

                    // Use Docker Pipeline plugin to run container detached
                    docker.image(IMAGE_NAME).run("-d -p ${PORT}:3000 --name nodeapp-${env.BRANCH_NAME}")

                    echo "Container ${IMAGE_NAME} is running at http://localhost:${PORT}"
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline for branch ${env.BRANCH_NAME} finished."
        }
    }
}
