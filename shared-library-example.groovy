// Shared Library Example Pipeline
// Demonstrates how to use shared libraries for reusable pipeline code

@Library('my-shared-library') _

// Import classes from shared library
import com.example.BuildHelper
import com.example.DeployHelper
import com.example.NotificationHelper

pipeline {
    agent any
    
    parameters {
        choice(name: 'ENVIRONMENT', choices: ['dev', 'staging', 'production'], description: 'Deployment environment')
        booleanParam(name: 'SKIP_TESTS', defaultValue: false, description: 'Skip tests?')
    }
    
    environment {
        APP_NAME = 'myapp'
        VERSION = '1.0.0'
    }
    
    stages {
        stage('Checkout') {
            steps {
                script {
                    // Use shared library function
                    checkout scm
                    echo "Checked out branch: ${env.BRANCH_NAME}"
                }
            }
        }
        
        stage('Build') {
            steps {
                script {
                    // Use shared library class
                    def builder = new BuildHelper(this)
                    builder.buildApplication(APP_NAME, VERSION)
                }
            }
        }
        
        stage('Test') {
            when {
                expression { !params.SKIP_TESTS }
            }
            steps {
                script {
                    // Use shared library function from vars/
                    runTests()
                }
            }
        }
        
        stage('Code Quality') {
            steps {
                script {
                    // Use shared library for code quality
                    sonarQubeAnalysis(
                        projectKey: APP_NAME,
                        projectName: APP_NAME
                    )
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    // Use shared library for Docker operations
                    dockerBuild(
                        imageName: APP_NAME,
                        tag: VERSION,
                        dockerfile: 'Dockerfile'
                    )
                }
            }
        }
        
        stage('Deploy') {
            steps {
                script {
                    // Use shared library class for deployment
                    def deployer = new DeployHelper(this)
                    deployer.deploy(
                        environment: params.ENVIRONMENT,
                        appName: APP_NAME,
                        version: VERSION
                    )
                }
            }
        }
        
        stage('Smoke Tests') {
            steps {
                script {
                    // Use shared library for smoke tests
                    runSmokeTests(params.ENVIRONMENT)
                }
            }
        }
    }
    
    post {
        success {
            script {
                // Use shared library for notifications
                def notifier = new NotificationHelper(this)
                notifier.sendSuccess(
                    jobName: env.JOB_NAME,
                    buildNumber: env.BUILD_NUMBER,
                    environment: params.ENVIRONMENT
                )
            }
        }
        
        failure {
            script {
                def notifier = new NotificationHelper(this)
                notifier.sendFailure(
                    jobName: env.JOB_NAME,
                    buildNumber: env.BUILD_NUMBER,
                    environment: params.ENVIRONMENT
                )
            }
        }
        
        always {
            script {
                // Use shared library for cleanup
                cleanupWorkspace()
            }
        }
    }
}

// Example of calling shared library functions directly
// These would be defined in vars/ directory of your shared library

// vars/runTests.groovy
/*
def call() {
    echo 'Running tests...'
    sh 'npm test'
}
*/

// vars/sonarQubeAnalysis.groovy
/*
def call(Map config) {
    echo "Running SonarQube analysis for ${config.projectName}"
    withSonarQubeEnv('SonarQube') {
        sh """
            sonar-scanner \
            -Dsonar.projectKey=${config.projectKey} \
            -Dsonar.projectName=${config.projectName}
        """
    }
}
*/

// vars/dockerBuild.groovy
/*
def call(Map config) {
    echo "Building Docker image: ${config.imageName}:${config.tag}"
    sh """
        docker build -t ${config.imageName}:${config.tag} -f ${config.dockerfile} .
        docker tag ${config.imageName}:${config.tag} ${config.imageName}:latest
    """
}
*/

// vars/runSmokeTests.groovy
/*
def call(String environment) {
    echo "Running smoke tests for ${environment}"
    retry(3) {
        sh "curl -f https://${environment}.example.com/health"
    }
}
*/

// vars/cleanupWorkspace.groovy
/*
def call() {
    echo 'Cleaning up workspace...'
    cleanWs()
    sh 'docker system prune -f || true'
}
*/

// Example shared library structure:
/*
my-shared-library/
├── src/
│   └── com/
│       └── example/
│           ├── BuildHelper.groovy
│           ├── DeployHelper.groovy
│           └── NotificationHelper.groovy
├── vars/
│   ├── runTests.groovy
│   ├── sonarQubeAnalysis.groovy
│   ├── dockerBuild.groovy
│   ├── runSmokeTests.groovy
│   └── cleanupWorkspace.groovy
└── resources/
    └── scripts/
        └── deploy.sh
*/

// Made with Bob
