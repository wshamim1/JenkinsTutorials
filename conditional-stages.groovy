// Conditional Stages Pipeline
// Demonstrates how to execute stages based on conditions

pipeline {
    agent any
    
    parameters {
        choice(name: 'ENVIRONMENT', choices: ['dev', 'staging', 'production'], description: 'Select deployment environment')
        booleanParam(name: 'RUN_TESTS', defaultValue: true, description: 'Run tests?')
        booleanParam(name: 'DEPLOY', defaultValue: false, description: 'Deploy application?')
    }
    
    environment {
        APP_VERSION = '1.0.0'
    }
    
    stages {
        stage('Build') {
            steps {
                echo "Building application version ${APP_VERSION}"
                echo "Target environment: ${params.ENVIRONMENT}"
            }
        }
        
        stage('Unit Tests') {
            when {
                expression { params.RUN_TESTS == true }
            }
            steps {
                echo 'Running unit tests...'
                // Add your test commands here
            }
        }
        
        stage('Integration Tests') {
            when {
                allOf {
                    expression { params.RUN_TESTS == true }
                    expression { params.ENVIRONMENT != 'dev' }
                }
            }
            steps {
                echo 'Running integration tests...'
                // Add your integration test commands here
            }
        }
        
        stage('Security Scan') {
            when {
                expression { params.ENVIRONMENT == 'production' }
            }
            steps {
                echo 'Running security scan for production...'
                // Add security scan commands here
            }
        }
        
        stage('Deploy to Dev') {
            when {
                allOf {
                    expression { params.DEPLOY == true }
                    expression { params.ENVIRONMENT == 'dev' }
                }
            }
            steps {
                echo 'Deploying to development environment...'
                // Add dev deployment commands here
            }
        }
        
        stage('Deploy to Staging') {
            when {
                allOf {
                    expression { params.DEPLOY == true }
                    expression { params.ENVIRONMENT == 'staging' }
                }
            }
            steps {
                echo 'Deploying to staging environment...'
                // Add staging deployment commands here
            }
        }
        
        stage('Deploy to Production') {
            when {
                allOf {
                    expression { params.DEPLOY == true }
                    expression { params.ENVIRONMENT == 'production' }
                }
            }
            steps {
                input message: 'Deploy to production?', ok: 'Deploy'
                echo 'Deploying to production environment...'
                // Add production deployment commands here
            }
        }
    }
    
    post {
        success {
            echo "Pipeline completed successfully for ${params.ENVIRONMENT} environment"
        }
        failure {
            echo 'Pipeline failed!'
        }
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
        }
    }
}

// Made with Bob
