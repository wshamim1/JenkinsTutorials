# Variables and Methods in Jenkins Pipeline

Learn how to define and use variables and custom methods in Jenkins pipelines to create more maintainable and reusable code.

## Overview

Jenkins pipelines support defining variables and methods outside the pipeline block, which can then be called throughout your pipeline stages. This promotes code reusability and cleaner pipeline scripts.

## Defining Variables

### Global Variables

Variables defined outside the pipeline block are accessible throughout the entire pipeline:

```groovy
// Define variables before pipeline
def aa = 100

pipeline {
    agent any
    
    stages {
        stage('Use Variable') {
            steps {
                echo "Value of aa: ${aa}"
            }
        }
    }
}
```

### Environment Variables

Variables defined in the environment block are available as environment variables:

```groovy
pipeline {
    agent any
    
    environment {
        def mvn_home = tool name: 'maven-3.5.4', type: 'maven'
        def a = 10
        def b = 10
    }
    
    stages {
        stage('Use Environment Variables') {
            steps {
                bat "echo ${a} + ${b}"
            }
        }
    }
}
```

## Defining Methods

### Custom Methods

Define reusable methods before the pipeline block:

```groovy
// Define custom method
def sums() {
    println("Custom method called")
}

pipeline {
    agent any
    
    stages {
        stage('Call Method') {
            steps {
                // Call the custom method
                sums()
            }
        }
    }
}
```

### Methods with Parameters

```groovy
def greet(name) {
    println("Hello, ${name}!")
}

def add(a, b) {
    return a + b
}

pipeline {
    agent any
    
    stages {
        stage('Use Methods') {
            steps {
                script {
                    greet("Jenkins")
                    def result = add(10, 20)
                    println("Sum: ${result}")
                }
            }
        }
    }
}
```

## Complete Example

Here's a comprehensive example combining variables and methods:

```groovy
// Global variables
def aa = 100

// Custom method
def sums() {
    println("sssss")
}

pipeline {
    agent any
    
    environment {
        def mvn_home = tool name: 'maven-3.5.4', type: 'maven'
        def a = 10
        def b = 10
    }
    
    stages {
        stage('git clone') {
            steps {
                pwd()
                deleteDir()
                bat "git clone https://github.com/wilsonshamim/MyMavenProject1.git ."
                
                // Call custom method
                sums()
                
                // Use variables
                bat "echo ${aa}"
            }
        }
        
        stage('build') {
            steps {
                bat "${mvn_home}/bin/mvn package"
            }
        }
    }
}
```

## Advanced Variable Usage

### String Manipulation

```groovy
def name = "JENKINS"

pipeline {
    agent any
    
    stages {
        stage('String Operations') {
            steps {
                script {
                    def lowercase = name.toLowerCase()
                    def uppercase = name.toUpperCase()
                    def length = name.length()
                    
                    println "Original: ${name}"
                    println "Lowercase: ${lowercase}"
                    println "Uppercase: ${uppercase}"
                    println "Length: ${length}"
                }
            }
        }
    }
}
```

### Collections

```groovy
def servers = ['server1', 'server2', 'server3']
def config = [env: 'production', version: '1.0.0']

pipeline {
    agent any
    
    stages {
        stage('Process Collections') {
            steps {
                script {
                    // Iterate through list
                    servers.each { server ->
                        println "Deploying to ${server}"
                    }
                    
                    // Access map values
                    println "Environment: ${config.env}"
                    println "Version: ${config.version}"
                }
            }
        }
    }
}
```

## Method Examples

### Validation Method

```groovy
def validateInput(value) {
    if (value == null || value.isEmpty()) {
        error "Invalid input: value cannot be empty"
    }
    return true
}

pipeline {
    agent any
    
    stages {
        stage('Validate') {
            steps {
                script {
                    validateInput("test")
                    println "Validation passed"
                }
            }
        }
    }
}
```

### Calculation Method

```groovy
def calculateTotal(items) {
    def total = 0
    items.each { item ->
        total += item
    }
    return total
}

pipeline {
    agent any
    
    stages {
        stage('Calculate') {
            steps {
                script {
                    def numbers = [10, 20, 30, 40]
                    def sum = calculateTotal(numbers)
                    println "Total: ${sum}"
                }
            }
        }
    }
}
```

### File Processing Method

```groovy
def processFile(filename) {
    if (fileExists(filename)) {
        println "Processing ${filename}"
        return true
    } else {
        println "File ${filename} not found"
        return false
    }
}

pipeline {
    agent any
    
    stages {
        stage('Process Files') {
            steps {
                script {
                    processFile('pom.xml')
                    processFile('build.gradle')
                }
            }
        }
    }
}
```

## Scope and Visibility

### Local vs Global Variables

```groovy
// Global variable
def globalVar = "I'm global"

pipeline {
    agent any
    
    stages {
        stage('Scope Demo') {
            steps {
                script {
                    // Local variable
                    def localVar = "I'm local"
                    
                    println globalVar  // Accessible
                    println localVar   // Accessible here
                }
                
                // localVar not accessible here
                echo globalVar  // Still accessible
            }
        }
    }
}
```

### Modifying Variables

```groovy
def counter = 0

def increment() {
    counter++
    println "Counter: ${counter}"
}

pipeline {
    agent any
    
    stages {
        stage('Count') {
            steps {
                script {
                    increment()
                    increment()
                    increment()
                }
            }
        }
    }
}
```

## Best Practices

### 1. Use Meaningful Names

```groovy
// Good
def buildVersion = "1.0.0"
def deploymentEnvironment = "production"

// Avoid
def v = "1.0.0"
def e = "production"
```

### 2. Document Complex Methods

```groovy
/**
 * Deploys application to specified environment
 * @param env Target environment (dev, staging, prod)
 * @param version Application version to deploy
 * @return Deployment status
 */
def deployApplication(env, version) {
    println "Deploying version ${version} to ${env}"
    // Deployment logic here
    return "SUCCESS"
}
```

### 3. Handle Errors Gracefully

```groovy
def safeExecute(closure) {
    try {
        closure()
        return true
    } catch (Exception e) {
        println "Error: ${e.message}"
        return false
    }
}

pipeline {
    agent any
    
    stages {
        stage('Safe Execution') {
            steps {
                script {
                    safeExecute {
                        // Your code here
                        println "Executing safely"
                    }
                }
            }
        }
    }
}
```

### 4. Keep Methods Small and Focused

```groovy
// Good - Single responsibility
def cloneRepository(url) {
    bat "git clone ${url}"
}

def buildProject() {
    bat "mvn clean package"
}

// Use them separately
pipeline {
    agent any
    
    stages {
        stage('Setup') {
            steps {
                script {
                    cloneRepository("https://github.com/user/repo.git")
                }
            }
        }
        
        stage('Build') {
            steps {
                script {
                    buildProject()
                }
            }
        }
    }
}
```

## Common Patterns

### Configuration Object

```groovy
def config = [
    gitUrl: 'https://github.com/user/repo.git',
    branch: 'master',
    buildTool: 'maven',
    deployEnv: 'production'
]

def getBuildCommand(config) {
    switch(config.buildTool) {
        case 'maven':
            return 'mvn clean package'
        case 'gradle':
            return 'gradle build'
        default:
            return 'make'
    }
}

pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                script {
                    def cmd = getBuildCommand(config)
                    bat cmd
                }
            }
        }
    }
}
```

### Factory Pattern

```groovy
def createBuilder(type) {
    switch(type) {
        case 'maven':
            return { bat 'mvn clean package' }
        case 'gradle':
            return { bat 'gradle build' }
        case 'npm':
            return { bat 'npm run build' }
        default:
            return { echo 'Unknown build type' }
    }
}

pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                script {
                    def builder = createBuilder('maven')
                    builder()
                }
            }
        }
    }
}
```

## Tips and Tricks

1. **Use @NonCPS for complex methods** - Improves performance
2. **Avoid heavy computation** - Keep pipelines fast
3. **Use shared libraries** - For organization-wide reusable code
4. **Test methods independently** - Use Groovy console
5. **Keep state minimal** - Reduce serialization overhead
6. **Use parameters** - Make methods flexible
7. **Return values** - Make methods composable
8. **Handle null values** - Prevent unexpected errors

## Resources

- [Pipeline Groovy Syntax](https://www.jenkins.io/doc/book/pipeline/syntax/)
- [Shared Libraries](https://www.jenkins.io/doc/book/pipeline/shared-libraries/)
- [Groovy Documentation](https://groovy-lang.org/documentation.html)