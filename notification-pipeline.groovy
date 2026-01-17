// Notification Pipeline
// Demonstrates various notification methods (Email, Slack, etc.)

pipeline {
    agent any
    
    environment {
        PROJECT_NAME = 'MyApp'
        BUILD_USER = 'Jenkins'
    }
    
    stages {
        stage('Build') {
            steps {
                echo 'Building application...'
                // Add your build commands
            }
        }
        
        stage('Test') {
            steps {
                echo 'Running tests...'
                // Add your test commands
            }
        }
        
        stage('Deploy') {
            steps {
                echo 'Deploying application...'
                // Add your deployment commands
            }
        }
    }
    
    post {
        success {
            script {
                // Email notification on success
                emailext(
                    subject: "✅ SUCCESS: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                    body: """
                        <h2>Build Successful!</h2>
                        <p><strong>Project:</strong> ${PROJECT_NAME}</p>
                        <p><strong>Build Number:</strong> ${env.BUILD_NUMBER}</p>
                        <p><strong>Build URL:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                        <p><strong>Duration:</strong> ${currentBuild.durationString}</p>
                        <p><strong>Branch:</strong> ${env.BRANCH_NAME ?: 'N/A'}</p>
                        <hr>
                        <p>Check the console output for more details.</p>
                    """,
                    to: 'team@example.com',
                    mimeType: 'text/html'
                )
                
                // Slack notification (requires Slack plugin)
                slackSend(
                    color: 'good',
                    message: "✅ SUCCESS: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}\n" +
                             "Duration: ${currentBuild.durationString}\n" +
                             "URL: ${env.BUILD_URL}",
                    channel: '#builds'
                )
                
                echo 'Success notifications sent!'
            }
        }
        
        failure {
            script {
                // Email notification on failure
                emailext(
                    subject: "❌ FAILURE: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                    body: """
                        <h2 style="color: red;">Build Failed!</h2>
                        <p><strong>Project:</strong> ${PROJECT_NAME}</p>
                        <p><strong>Build Number:</strong> ${env.BUILD_NUMBER}</p>
                        <p><strong>Build URL:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                        <p><strong>Duration:</strong> ${currentBuild.durationString}</p>
                        <p><strong>Branch:</strong> ${env.BRANCH_NAME ?: 'N/A'}</p>
                        <hr>
                        <p><strong>Console Output:</strong> <a href="${env.BUILD_URL}console">${env.BUILD_URL}console</a></p>
                        <p>Please check the logs and fix the issues.</p>
                    """,
                    to: 'team@example.com',
                    mimeType: 'text/html'
                )
                
                // Slack notification for failure
                slackSend(
                    color: 'danger',
                    message: "❌ FAILURE: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}\n" +
                             "Duration: ${currentBuild.durationString}\n" +
                             "URL: ${env.BUILD_URL}\n" +
                             "Please investigate!",
                    channel: '#builds'
                )
                
                echo 'Failure notifications sent!'
            }
        }
        
        unstable {
            script {
                // Email notification for unstable build
                emailext(
                    subject: "⚠️ UNSTABLE: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                    body: """
                        <h2 style="color: orange;">Build Unstable!</h2>
                        <p><strong>Project:</strong> ${PROJECT_NAME}</p>
                        <p><strong>Build Number:</strong> ${env.BUILD_NUMBER}</p>
                        <p><strong>Build URL:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                        <p><strong>Duration:</strong> ${currentBuild.durationString}</p>
                        <hr>
                        <p>The build completed but with warnings or test failures.</p>
                    """,
                    to: 'team@example.com',
                    mimeType: 'text/html'
                )
                
                // Slack notification for unstable
                slackSend(
                    color: 'warning',
                    message: "⚠️ UNSTABLE: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}\n" +
                             "URL: ${env.BUILD_URL}",
                    channel: '#builds'
                )
            }
        }
        
        fixed {
            script {
                // Notification when build is fixed after failure
                emailext(
                    subject: "🎉 FIXED: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                    body: """
                        <h2 style="color: green;">Build Fixed!</h2>
                        <p>The build is now successful after previous failures.</p>
                        <p><strong>Project:</strong> ${PROJECT_NAME}</p>
                        <p><strong>Build Number:</strong> ${env.BUILD_NUMBER}</p>
                        <p><strong>Build URL:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                    """,
                    to: 'team@example.com',
                    mimeType: 'text/html'
                )
                
                slackSend(
                    color: 'good',
                    message: "🎉 FIXED: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}\n" +
                             "Great job fixing the build!",
                    channel: '#builds'
                )
            }
        }
        
        always {
            script {
                echo 'Build completed. Notifications processed.'
                
                // Custom notification logic
                def buildStatus = currentBuild.result ?: 'SUCCESS'
                def buildColor = buildStatus == 'SUCCESS' ? 'green' : 
                                buildStatus == 'FAILURE' ? 'red' : 'orange'
                
                echo "Build Status: ${buildStatus}"
                echo "Notification Color: ${buildColor}"
            }
        }
    }
}

// Alternative: Custom notification function
def sendCustomNotification(status, message) {
    def color = status == 'SUCCESS' ? 'good' : 
                status == 'FAILURE' ? 'danger' : 'warning'
    
    // Send to multiple channels
    slackSend(
        color: color,
        message: message,
        channel: '#builds'
    )
    
    // Send email
    emailext(
        subject: "${status}: ${env.JOB_NAME}",
        body: message,
        to: 'team@example.com'
    )
}

// Made with Bob
