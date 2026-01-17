// Multi-Branch Pipeline
// Demonstrates branch-specific behavior and environment-based deployments

pipeline {
    agent any
    
    environment {
        APP_NAME = 'myapp'
        BUILD_VERSION = "${env.BRANCH_NAME}-${env.BUILD_NUMBER}"
    }
    
    stages {
        stage('Environment Setup') {
            steps {
                script {
                    // Set environment-specific variables based on branch
                    if (env.BRANCH_NAME == 'master' || env.BRANCH_NAME == 'main') {
                        env.DEPLOY_ENV = 'production'
                        env.RUN_TESTS = 'true'
                        env.DEPLOY_URL = 'https://prod.example.com'
                    } else if (env.BRANCH_NAME == 'develop') {
                        env.DEPLOY_ENV = 'staging'
                        env.RUN_TESTS = 'true'
                        env.DEPLOY_URL = 'https://staging.example.com'
                    } else if (env.BRANCH_NAME.startsWith('feature/')) {
                        env.DEPLOY_ENV = 'dev'
                        env.RUN_TESTS = 'true'
                        env.DEPLOY_URL = 'https://dev.example.com'
                    } else if (env.BRANCH_NAME.startsWith('hotfix/')) {
                        env.DEPLOY_ENV = 'hotfix'
                        env.RUN_TESTS = 'true'
                        env.DEPLOY_URL = 'https://hotfix.example.com'
                    } else {
                        env.DEPLOY_ENV = 'test'
                        env.RUN_TESTS = 'false'
                        env.DEPLOY_URL = 'https://test.example.com'
                    }
                    
                    echo "Branch: ${env.BRANCH_NAME}"
                    echo "Environment: ${env.DEPLOY_ENV}"
                    echo "Build Version: ${BUILD_VERSION}"
                }
            }
        }
        
        stage('Checkout') {
            steps {
                echo "Checking out ${env.BRANCH_NAME} branch..."
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo "Building ${APP_NAME} version ${BUILD_VERSION}"
                // Add your build commands here
                sh 'echo "Build completed"'
            }
        }
        
        stage('Unit Tests') {
            when {
                expression { env.RUN_TESTS == 'true' }
            }
            steps {
                echo 'Running unit tests...'
                // Add your test commands
                sh 'echo "Tests passed"'
            }
        }
        
        stage('Code Quality') {
            when {
                anyOf {
                    branch 'master'
                    branch 'main'
                    branch 'develop'
                }
            }
            steps {
                echo 'Running code quality analysis...'
                // Add SonarQube or other quality tools
            }
        }
        
        stage('Security Scan') {
            when {
                anyOf {
                    branch 'master'
                    branch 'main'
                }
            }
            steps {
                echo 'Running security scan...'
                // Add security scanning tools
            }
        }
        
        stage('Deploy to Dev') {
            when {
                branch pattern: 'feature/.*', comparator: 'REGEXP'
            }
            steps {
                echo "Deploying to development environment..."
                echo "URL: ${env.DEPLOY_URL}"
            }
        }
        
        stage('Deploy to Staging') {
            when {
                branch 'develop'
            }
            steps {
                echo "Deploying to staging environment..."
                echo "URL: ${env.DEPLOY_URL}"
            }
        }
        
        stage('Deploy to Production') {
            when {
                anyOf {
                    branch 'master'
                    branch 'main'
                }
            }
            steps {
                script {
                    // Require manual approval for production
                    input message: 'Deploy to production?', 
                          ok: 'Deploy',
                          submitter: 'admin,release-manager'
                    
                    echo "Deploying to production environment..."
                    echo "URL: ${env.DEPLOY_URL}"
                }
            }
        }
        
        stage('Hotfix Deploy') {
            when {
                branch pattern: 'hotfix/.*', comparator: 'REGEXP'
            }
            steps {
                script {
                    input message: 'Deploy hotfix?', ok: 'Deploy'
                    echo "Deploying hotfix to production..."
                }
            }
        }
        
        stage('Smoke Tests') {
            when {
                anyOf {
                    branch 'master'
                    branch 'main'
                    branch 'develop'
                }
            }
            steps {
                echo 'Running smoke tests...'
                retry(3) {
                    sh "curl -f ${env.DEPLOY_URL}/health || exit 1"
                }
            }
        }
        
        stage('Tag Release') {
            when {
                anyOf {
                    branch 'master'
                    branch 'main'
                }
            }
            steps {
                script {
                    echo "Tagging release: v${BUILD_VERSION}"
                    sh """
                        git tag -a v${BUILD_VERSION} -m "Release version ${BUILD_VERSION}"
                        git push origin v${BUILD_VERSION}
                    """
                }
            }
        }
    }
    
    post {
        success {
            script {
                echo "Pipeline succeeded for branch: ${env.BRANCH_NAME}"
                echo "Deployed to: ${env.DEPLOY_ENV}"
                
                // Send notifications based on branch
                if (env.BRANCH_NAME == 'master' || env.BRANCH_NAME == 'main') {
                    echo 'Sending production deployment notification...'
                }
            }
        }
        failure {
            echo "Pipeline failed for branch: ${env.BRANCH_NAME}"
            // Send failure notifications
        }
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
        }
    }
}

// Made with Bob
