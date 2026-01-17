// Maven Build Pipeline
// This pipeline clones a Git repository and builds a Maven project

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
                // Change to project directory and build
                bat "cd MyMavenProject1"
                bat "mvn package"
            }
        }
    }
}

// Made with Bob
