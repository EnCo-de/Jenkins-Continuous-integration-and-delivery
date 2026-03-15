pipeline {
    agent any
    
    tools {
        dockerTool "docker"
    }
    
    environment {
        IMAGE_NAME = ""
        PORT = ""
        CONTAINER_NAME = "nodeapp"
    }

    parameters {
        choice(name: 'environment', choices: ['main', 'dev'], description: 'Target environment')
        string(name: 'image_tag', defaultValue: 'v1.0', description: 'Docker image tag')
    }

    stages {
        stage('Set Environment Variables') {
            steps {
                script {
                    switch(params.environment) {

                        case "main":
                            IMAGE_NAME = "nodemain"
                            PORT = "3000"
                            break

                        case "dev":
                            IMAGE_NAME = "nodedev"
                            PORT = "3001"
                            break

                    }
                }
            }
        }
        
        stage('Deploy') {
            steps {
                script {
                    sh """
                    docker stop "${CONTAINER_NAME}-${params.environment}" || true
                    docker rm "${CONTAINER_NAME}-${params.environment}" || true
    
                    docker run -d \
                    --name "${CONTAINER_NAME}-${params.environment}" \
                    -p ${PORT}:3000 \
                    ${IMAGE_NAME}:${params.image_tag}
                    """
                }
            }
        }
    }
    
    post {
        success {
            echo "You deployed to ${params.environment} successfully 🚀"
        }
        always {
            cleanWs()
        }
    }

}
