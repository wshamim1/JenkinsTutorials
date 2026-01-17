# Jenkins Tutorials

A comprehensive collection of Jenkins pipeline examples and tutorials for learning CI/CD automation.

## 📋 Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Tutorials](#tutorials)
- [Pipeline Examples](#pipeline-examples)
- [Contributing](#contributing)

## 🎯 Overview

This repository contains practical examples and tutorials for working with Jenkins pipelines, including:
- Basic pipeline creation
- Maven builds
- Git integration
- Parameterized builds
- Groovy scripting
- Credentials management
- Tool configuration

## 📦 Prerequisites

- Java JDK 8 or higher
- Jenkins WAR file or Jenkins installation
- Maven (for Maven build examples)
- Git

## 🚀 Installation

### Installing Jenkins

1. Download the `jenkins.war` file
2. Run Jenkins on a custom port:
   ```bash
   java -jar jenkins.war --httpPort=9090
   ```
3. Access Jenkins at `http://localhost:9090`
4. Use `admin` as the username
5. Find the initial password in the security file provided during startup

For detailed instructions, see [installJenkins](./installJenkins)

## 📚 Tutorials

### Basic Pipeline Creation
Learn how to create your first Jenkins pipeline:
- Navigate to Jenkins → New Item
- Enter item name and select "Pipeline"
- Configure pipeline with declarative syntax

See [pipeline.md](./pipeline.md) for step-by-step instructions.

### Available Examples

| Tutorial | Description | File |
|----------|-------------|------|
| **Basic Pipeline** | Simple pipeline with multiple stages | [jenkinsfile.groovy](./jenkinsfile.groovy) |
| **Maven Build** | Clone Git repo and build Maven project | [mavenbuild](./mavenbuild) |
| **Parameterized Build** | Build with user-defined parameters | [BuildWithParameter](./BuildWithParameter) |
| **Git Checkout** | Checkout Groovy scripts from SCM | [checkoutgroovyfromSCM](./checkoutgroovyfromSCM) |
| **Get Git Projects** | Retrieve projects from Git repositories | [GetGitProjects](./GetGitProjects) |
| **Install Plugins** | Plugin installation guide | [installPlugins](./installPlugins) |
| **Read JSON** | Parse and use JSON data in pipelines | [readJSON](./readJSON) |
| **Scripts in Steps** | Execute scripts within pipeline steps | [ScriptsInSteps](./ScriptsInSteps) |
| **Use Tools** | Configure and use tools in pipelines | [UseToolsinPipeline](./UseToolsinPipeline) |
| **Variable Methods** | Working with variables and methods | [VariableMethodsinPipeline](./VariableMethodsinPipeline) |
| **With Credentials** | Secure credential management | [withCredentials](./withCredentials) |

## 🔧 Pipeline Examples

### Basic Pipeline Structure
```groovy
pipeline {
    agent any
    stages {
        stage('Stage 1') {
            steps {
                echo "step1"
            }
        }
        stage('Preparation') {
            steps {
                script {
                    def a = 100
                    echo "value is " + a
                }
            }
        }
    }
}
```

### Maven Build Pipeline
```groovy
pipeline {
    agent any
    stages {
        stage('git clone') {
            steps {
                pwd()
                deleteDir()
                bat "git clone https://github.com/wilsonshamim/MyMavenProject1.git"
            }
        }
        stage('mvn build') {
            steps {
                bat "cd MyMavenProject1"
                bat "mvn package"
            }
        }
    }
}
```

### Parameterized Build
```groovy
pipeline {
    agent any
    stages {
        stage('one') {
            steps {
                script {
                    println(intname.toUpperCase())
                }
                bat "echo one"
            }
        }
    }
}
```

## 📁 Repository Structure

```
JenkinsTutorials/
├── README.md
├── pipeline.md                    # Pipeline creation guide
├── jenkinsfile.groovy            # Basic Jenkinsfile example
├── installJenkins                # Jenkins installation guide
├── installPlugins                # Plugin installation
├── mavenbuild                    # Maven build pipeline
├── BuildWithParameter            # Parameterized builds
├── checkoutgroovyfromSCM        # SCM checkout examples
├── GetGitProjects               # Git project retrieval
├── readJSON                     # JSON parsing
├── ScriptsInSteps               # Script execution
├── UseToolsinPipeline           # Tool configuration
├── VariableMethodsinPipeline    # Variables and methods
├── withCredentials              # Credentials management
└── workspace/                   # Jenkins workspace directory
```

## 🤝 Contributing

Contributions are welcome! Feel free to:
- Add new pipeline examples
- Improve existing tutorials
- Fix bugs or typos
- Enhance documentation

## 📝 License

This project is open source and available for educational purposes.

## 👤 Author

**Wilson Shamim**
- GitHub: [@wilsonshamim](https://github.com/wilsonshamim)

## 🔗 Useful Links

- [Jenkins Official Documentation](https://www.jenkins.io/doc/)
- [Pipeline Syntax Reference](https://www.jenkins.io/doc/book/pipeline/syntax/)
- [Jenkins Plugins](https://plugins.jenkins.io/)

---

⭐ If you find this repository helpful, please consider giving it a star!