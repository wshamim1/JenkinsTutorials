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

For detailed instructions, see [install-jenkins.md](./install-jenkins.md)

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
| **Install Jenkins** | Complete Jenkins installation guide | [install-jenkins.md](./install-jenkins.md) |
| **Install Plugins** | Plugin installation and management | [install-plugins.md](./install-plugins.md) |
| **Basic Pipeline** | Simple pipeline with multiple stages | [jenkinsfile.groovy](./jenkinsfile.groovy) |
| **Maven Build** | Clone Git repo and build Maven project | [maven-build.groovy](./maven-build.groovy) |
| **Get Git Projects** | Retrieve and build Git projects | [get-git-projects.groovy](./get-git-projects.groovy) |
| **Parameterized Build** | Build with user-defined parameters | [build-with-parameters.md](./build-with-parameters.md) |
| **Git Checkout from SCM** | Checkout Groovy scripts from SCM | [checkout-groovy-from-scm.md](./checkout-groovy-from-scm.md) |
| **Read JSON/CSV** | Parse and use JSON/CSV data in pipelines | [read-json.md](./read-json.md) |
| **Scripts in Steps** | Execute Groovy scripts within pipeline steps | [scripts-in-steps.md](./scripts-in-steps.md) |
| **Use Tools** | Configure and use build tools in pipelines | [use-tools-in-pipeline.md](./use-tools-in-pipeline.md) |
| **Variables & Methods** | Working with variables and custom methods | [variables-methods-in-pipeline.md](./variables-methods-in-pipeline.md) |
| **With Credentials** | Secure credential management | [with-credentials.md](./with-credentials.md) |

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
├── README.md                           # This file
├── pipeline.md                         # Pipeline creation guide
├── jenkinsfile.groovy                  # Basic Jenkinsfile example
├── install-jenkins.md                  # Jenkins installation guide
├── install-plugins.md                  # Plugin installation guide
├── maven-build.groovy                  # Maven build pipeline
├── get-git-projects.groovy             # Git project retrieval pipeline
├── build-with-parameters.md            # Parameterized builds guide
├── checkout-groovy-from-scm.md         # SCM checkout guide
├── read-json.md                        # JSON/CSV parsing guide
├── scripts-in-steps.md                 # Script execution guide
├── use-tools-in-pipeline.md            # Tool configuration guide
├── variables-methods-in-pipeline.md    # Variables and methods guide
├── with-credentials.md                 # Credentials management guide
└── workspace/                          # Jenkins workspace directory
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