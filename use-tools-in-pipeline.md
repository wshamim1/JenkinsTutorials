# Using Tools in Jenkins Pipeline

Learn how to configure and use build tools like Maven, Gradle, JDK, and others in your Jenkins pipelines.

## Overview

Jenkins allows you to configure various tools globally and reference them in your pipelines. This ensures consistent tool versions across all builds.

## Setting Up Maven

### Step 1: Configure Maven in Jenkins

1. Click **Manage Jenkins** → **Global Tool Configuration**
2. Scroll to **Maven Installation**
3. Click **Add Maven**
4. Configure the following:
   - **Name**: `maven-3.5.4` (or your preferred name)
   - **MAVEN_HOME**: Path to your Maven installation
     ```
     C:\Users\shamim\maven\apache-maven-3.5.4-bin\apache-maven-3.5.4\
     ```
   - Or check **Install automatically** to let Jenkins download Maven
5. Click **Save**

### Step 2: Use Maven in Pipeline

Define the tool in the environment section and use it in your stages:

```groovy
pipeline {
    agent any
    
    environment {
        // Reference the configured Maven tool
        def mvn_home = tool name: 'maven-3.5.4', type: 'maven'
    }
    
    stages {
        stage('git clone') {
            steps {
                pwd()
                deleteDir()
                bat "git clone https://github.com/wilsonshamim/MyMavenProject1.git ."
            }
        }
        
        stage('build') {
            steps {
                // Use Maven from the configured tool
                bat "${mvn_home}/bin/mvn package"
            }
        }
    }
}
```

## Using the Tool Directive

### Basic Tool Usage

```groovy
pipeline {
    agent any
    
    tools {
        maven 'maven-3.5.4'
        jdk 'jdk-11'
    }
    
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
    }
}
```

### Multiple Tools

```groovy
pipeline {
    agent any
    
    tools {
        maven 'maven-3.6.3'
        jdk 'jdk-11'
        gradle 'gradle-7.0'
    }
    
    stages {
        stage('Maven Build') {
            steps {
                sh 'mvn --version'
                sh 'mvn clean package'
            }
        }
        
        stage('Gradle Build') {
            steps {
                sh 'gradle --version'
                sh 'gradle build'
            }
        }
    }
}
```

## Configuring Different Tools

### JDK Configuration

1. Go to **Manage Jenkins** → **Global Tool Configuration**
2. Find **JDK installations**
3. Click **Add JDK**
4. Configure:
   - **Name**: `jdk-11`
   - **JAVA_HOME**: Path to JDK installation
   - Or use **Install automatically**

### Using JDK in Pipeline

```groovy
pipeline {
    agent any
    
    tools {
        jdk 'jdk-11'
    }
    
    stages {
        stage('Check Java') {
            steps {
                sh 'java -version'
                sh 'javac -version'
            }
        }
    }
}
```

### Gradle Configuration

1. Go to **Manage Jenkins** → **Global Tool Configuration**
2. Find **Gradle installations**
3. Click **Add Gradle**
4. Configure:
   - **Name**: `gradle-7.0`
   - **Install automatically** or provide **GRADLE_HOME**

### Using Gradle in Pipeline

```groovy
pipeline {
    agent any
    
    tools {
        gradle 'gradle-7.0'
    }
    
    stages {
        stage('Build') {
            steps {
                sh 'gradle clean build'
            }
        }
    }
}
```

### Node.js Configuration

1. Install **NodeJS Plugin**
2. Go to **Manage Jenkins** → **Global Tool Configuration**
3. Find **NodeJS installations**
4. Click **Add NodeJS**
5. Configure version and global packages

### Using Node.js in Pipeline

```groovy
pipeline {
    agent any
    
    tools {
        nodejs 'node-16'
    }
    
    stages {
        stage('Build') {
            steps {
                sh 'node --version'
                sh 'npm --version'
                sh 'npm install'
                sh 'npm run build'
            }
        }
    }
}
```

## Advanced Tool Usage

### Tool in Environment Block

```groovy
pipeline {
    agent any
    
    environment {
        MAVEN_HOME = tool name: 'maven-3.6.3', type: 'maven'
        JAVA_HOME = tool name: 'jdk-11', type: 'jdk'
        PATH = "${MAVEN_HOME}/bin:${JAVA_HOME}/bin:${env.PATH}"
    }
    
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
    }
}
```

### Different Tools per Stage

```groovy
pipeline {
    agent any
    
    stages {
        stage('Build with Maven 3.5') {
            tools {
                maven 'maven-3.5.4'
            }
            steps {
                sh 'mvn --version'
                sh 'mvn clean package'
            }
        }
        
        stage('Build with Maven 3.6') {
            tools {
                maven 'maven-3.6.3'
            }
            steps {
                sh 'mvn --version'
                sh 'mvn clean install'
            }
        }
    }
}
```

### Tool with Custom Installation

```groovy
pipeline {
    agent any
    
    stages {
        stage('Setup') {
            steps {
                script {
                    def mvnHome = tool name: 'maven-3.6.3', type: 'maven'
                    env.PATH = "${mvnHome}/bin:${env.PATH}"
                }
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
    }
}
```

## Platform-Specific Commands

### Windows (bat)

```groovy
pipeline {
    agent any
    
    environment {
        MAVEN_HOME = tool name: 'maven-3.5.4', type: 'maven'
    }
    
    stages {
        stage('Build') {
            steps {
                bat "${MAVEN_HOME}\\bin\\mvn.cmd clean package"
            }
        }
    }
}
```

### Linux/Mac (sh)

```groovy
pipeline {
    agent any
    
    environment {
        MAVEN_HOME = tool name: 'maven-3.5.4', type: 'maven'
    }
    
    stages {
        stage('Build') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn clean package"
            }
        }
    }
}
```

## Best Practices

1. **Use descriptive tool names** - Include version numbers
2. **Configure tools globally** - Avoid hardcoding paths
3. **Use automatic installation** - When possible, let Jenkins manage tools
4. **Version control** - Document required tool versions
5. **Test tool availability** - Verify tools are accessible before use
6. **Use tools directive** - Simpler than environment variables
7. **Keep tools updated** - Regularly update tool versions
8. **Document requirements** - List required tools in README

## Troubleshooting

### Tool Not Found

```groovy
pipeline {
    agent any
    
    stages {
        stage('Verify Tool') {
            steps {
                script {
                    try {
                        def mvnHome = tool name: 'maven-3.5.4', type: 'maven'
                        echo "Maven found at: ${mvnHome}"
                    } catch (Exception e) {
                        error "Maven tool not configured: ${e.message}"
                    }
                }
            }
        }
    }
}
```

### Check Tool Version

```groovy
pipeline {
    agent any
    
    tools {
        maven 'maven-3.5.4'
        jdk 'jdk-11'
    }
    
    stages {
        stage('Verify Tools') {
            steps {
                sh 'mvn --version'
                sh 'java -version'
                sh 'which mvn'
                sh 'which java'
            }
        }
    }
}
```

## Common Tools

| Tool | Type | Configuration Location |
|------|------|------------------------|
| Maven | Build Tool | Global Tool Configuration → Maven |
| Gradle | Build Tool | Global Tool Configuration → Gradle |
| JDK | Runtime | Global Tool Configuration → JDK |
| Node.js | Runtime | Global Tool Configuration → NodeJS |
| Git | SCM | Global Tool Configuration → Git |
| Docker | Container | Requires Docker plugin |
| Ant | Build Tool | Global Tool Configuration → Ant |

## Resources

- [Jenkins Tool Configuration](https://www.jenkins.io/doc/book/managing/tools/)
- [Pipeline Tools Directive](https://www.jenkins.io/doc/book/pipeline/syntax/#tools)
- [Maven Plugin Documentation](https://plugins.jenkins.io/maven-plugin/)