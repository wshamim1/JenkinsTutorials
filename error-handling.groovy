// Error Handling Pipeline
// Demonstrates various error handling techniques in Jenkins pipelines

pipeline {
    agent any
    
    stages {
        stage('Try-Catch Example') {
            steps {
                script {
                    try {
                        echo 'Attempting risky operation...'
                        // Simulate an operation that might fail
                        def result = someRiskyOperation()
                        echo "Operation succeeded: ${result}"
                    } catch (Exception e) {
                        echo "Error caught: ${e.message}"
                        // Handle the error gracefully
                        currentBuild.result = 'UNSTABLE'
                    }
                }
            }
        }
        
        stage('Retry on Failure') {
            steps {
                retry(3) {
                    echo 'Attempting operation (will retry up to 3 times)...'
                    // Add your command that might fail
                    sh 'echo "Attempt successful"'
                }
            }
        }
        
        stage('Timeout Example') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    echo 'This stage will timeout after 5 minutes'
                    // Add your long-running command here
                    sleep 10
                }
            }
        }
        
        stage('Multiple Error Handlers') {
            steps {
                script {
                    try {
                        timeout(time: 2, unit: 'MINUTES') {
                            retry(2) {
                                echo 'Complex operation with timeout and retry'
                                // Your command here
                            }
                        }
                    } catch (org.jenkinsci.plugins.workflow.steps.FlowInterruptedException e) {
                        echo 'Operation timed out!'
                        currentBuild.result = 'UNSTABLE'
                    } catch (Exception e) {
                        echo "Unexpected error: ${e.message}"
                        currentBuild.result = 'FAILURE'
                    }
                }
            }
        }
        
        stage('Conditional Error Handling') {
            steps {
                script {
                    def exitCode = bat(returnStatus: true, script: 'exit 0')
                    
                    if (exitCode != 0) {
                        echo "Command failed with exit code: ${exitCode}"
                        // Decide whether to fail the build or continue
                        currentBuild.result = 'UNSTABLE'
                    } else {
                        echo 'Command succeeded'
                    }
                }
            }
        }
        
        stage('Capture Output') {
            steps {
                script {
                    try {
                        def output = bat(returnStdout: true, script: 'echo Hello World')
                        echo "Command output: ${output}"
                    } catch (Exception e) {
                        echo "Failed to capture output: ${e.message}"
                    }
                }
            }
        }
        
        stage('Custom Error Messages') {
            steps {
                script {
                    try {
                        // Validate some condition
                        def isValid = true
                        
                        if (!isValid) {
                            error('Validation failed: Custom error message')
                        }
                        
                        echo 'Validation passed'
                    } catch (Exception e) {
                        echo "Validation error: ${e.message}"
                        throw e  // Re-throw to fail the build
                    }
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        unstable {
            echo 'Pipeline completed with warnings'
        }
        failure {
            echo 'Pipeline failed!'
            // Send notifications, cleanup, etc.
        }
        always {
            echo 'This runs regardless of pipeline result'
            // Cleanup actions
        }
        cleanup {
            echo 'Final cleanup actions'
        }
    }
}

// Helper method for demonstration
def someRiskyOperation() {
    // Simulate a risky operation
    def random = new Random()
    if (random.nextBoolean()) {
        return "Success"
    } else {
        throw new Exception("Operation failed randomly")
    }
}

// Made with Bob
