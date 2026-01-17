// Parallel Stages Pipeline
// Demonstrates how to run multiple stages in parallel for faster builds

pipeline {
    agent any
    
    stages {
        stage('Preparation') {
            steps {
                echo 'Preparing workspace...'
                deleteDir()
            }
        }
        
        stage('Parallel Execution') {
            parallel {
                stage('Unit Tests') {
                    steps {
                        echo 'Running unit tests...'
                        sleep 3
                        echo 'Unit tests completed'
                    }
                }
                
                stage('Integration Tests') {
                    steps {
                        echo 'Running integration tests...'
                        sleep 3
                        echo 'Integration tests completed'
                    }
                }
                
                stage('Code Quality Analysis') {
                    steps {
                        echo 'Running code quality checks...'
                        sleep 3
                        echo 'Code quality analysis completed'
                    }
                }
                
                stage('Security Scan') {
                    steps {
                        echo 'Running security scan...'
                        sleep 3
                        echo 'Security scan completed'
                    }
                }
            }
        }
        
        stage('Deploy') {
            steps {
                echo 'All parallel stages completed successfully'
                echo 'Deploying application...'
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}

// Made with Bob
