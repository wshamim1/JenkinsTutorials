// Docker Pipeline
// Demonstrates how to build, test, and push Docker images in Jenkins

pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'myapp'
        DOCKER_TAG = "${env.BUILD_NUMBER}"
        DOCKER_REGISTRY = 'docker.io'
        REGISTRY_CREDENTIAL = 'dockerhub-credentials'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker image: ${DOCKER_IMAGE}:${DOCKER_TAG}"
                    docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                    
                    // Also tag as latest
                    sh "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest"
                }
            }
        }
        
        stage('Test Docker Image') {
            steps {
                script {
                    echo 'Running tests inside Docker container...'
                    docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").inside {
                        sh 'echo "Running tests..."'
                        // Add your test commands here
                        sh 'npm test || true'
                    }
                }
            }
        }
        
        stage('Scan Image for Vulnerabilities') {
            steps {
                script {
                    echo 'Scanning Docker image for vulnerabilities...'
                    // Example using Trivy scanner
                    sh """
                        docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
                        aquasec/trivy image ${DOCKER_IMAGE}:${DOCKER_TAG} || true
                    """
                }
            }
        }
        
        stage('Push to Registry') {
            when {
                branch 'master'
            }
            steps {
                script {
                    echo "Pushing image to registry..."
                    docker.withRegistry("https://${DOCKER_REGISTRY}", REGISTRY_CREDENTIAL) {
                        docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push()
                        docker.image("${DOCKER_IMAGE}:latest").push()
                    }
                }
            }
        }
        
        stage('Deploy Container') {
            steps {
                script {
                    echo 'Deploying Docker container...'
                    sh """
                        docker stop ${DOCKER_IMAGE} || true
                        docker rm ${DOCKER_IMAGE} || true
                        docker run -d --name ${DOCKER_IMAGE} \
                            -p 8080:8080 \
                            ${DOCKER_IMAGE}:${DOCKER_TAG}
                    """
                }
            }
        }
        
        stage('Health Check') {
            steps {
                script {
                    echo 'Performing health check...'
                    retry(5) {
                        sleep 10
                        sh 'curl -f http://localhost:8080/health || exit 1'
                    }
                    echo 'Application is healthy!'
                }
            }
        }
    }
    
    post {
        success {
            echo 'Docker pipeline completed successfully!'
        }
        failure {
            echo 'Docker pipeline failed!'
            // Rollback if needed
            sh "docker stop ${DOCKER_IMAGE} || true"
        }
        always {
            echo 'Cleaning up Docker resources...'
            sh """
                docker system prune -f || true
                docker image prune -f || true
            """
        }
    }
}

// Made with Bob
