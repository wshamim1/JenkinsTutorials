// Get Git Projects Pipeline
// This pipeline demonstrates cloning a Git repository and building a Maven project

pipeline {
    agent any
    
    stages {
        stage('git clone') {
            steps {
                // Print current working directory
                pwd()
                
                // Clean workspace before cloning
                deleteDir()
                
                // Clone the Maven project repository
                bat "git clone https://github.com/wilsonshamim/MyMavenProject1.git"
            }
        }
        
        stage('mvn build') {
            steps {
                // Change to project directory
                bat "cd MyMavenProject1"
                
                // Build the Maven project
                bat "mvn package"
            }
        }
    }
}

// Made with Bob
