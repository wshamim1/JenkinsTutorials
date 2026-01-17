# Using Scripts in Pipeline Steps

Learn how to define and use Groovy scripts, variables, and methods within Jenkins pipeline steps.

## Overview

Jenkins pipelines allow you to execute Groovy scripts within the `script{}` block in your steps. This enables complex logic, variable manipulation, and method calls.

## Defining Variables and Methods

You can define variables and methods before the pipeline block and use them throughout your pipeline.

### Basic Example

```groovy
// Define variables outside pipeline
def aa = 100
def n = "SHAMIM"
def x 

// Define methods outside pipeline
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
                script {
                    // Use script block for Groovy code
                    x = n.toLowerCase()
                }
                
                pwd()
                deleteDir()
                bat "git clone https://github.com/wilsonshamim/MyMavenProject1.git ."
                
                // Call custom method
                sums()
                
                // Use variables in commands
                bat "echo ${aa}+${a}+${b}"
                bat "echo ${x}"
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

## Script Block Features

### Variable Manipulation

```groovy
pipeline {
    agent any
    
    stages {
        stage('Process Data') {
            steps {
                script {
                    def name = "jenkins"
                    def upperName = name.toUpperCase()
                    def length = name.length()
                    
                    println "Original: ${name}"
                    println "Uppercase: ${upperName}"
                    println "Length: ${length}"
                }
            }
        }
    }
}
```

### Conditional Logic

```groovy
pipeline {
    agent any
    
    stages {
        stage('Conditional Execution') {
            steps {
                script {
                    def environment = "production"
                    
                    if (environment == "production") {
                        println "Running production deployment"
                    } else {
                        println "Running development deployment"
                    }
                }
            }
        }
    }
}
```

### Loops

```groovy
pipeline {
    agent any
    
    stages {
        stage('Loop Example') {
            steps {
                script {
                    def servers = ['server1', 'server2', 'server3']
                    
                    servers.each { server ->
                        println "Deploying to ${server}"
                    }
                }
            }
        }
    }
}
```

## File Operations

### Check if File Exists

```groovy
pipeline {
    agent any
    
    stages {
        stage('Check File') {
            steps {
                script {
                    def fileExists = fileExists 'Test'
                    println "File exists: ${fileExists}"
                    
                    if (fileExists) {
                        println "File found, proceeding..."
                    } else {
                        println "File not found!"
                    }
                }
            }
        }
    }
}
```

### Read Maven POM File

```groovy
pipeline {
    agent any
    
    stages {
        stage('Read POM') {
            steps {
                script {
                    def pom = readMavenPom file: 'pom.xml'
                    
                    println "GroupId: ${pom.groupId}"
                    println "ArtifactId: ${pom.artifactId}"
                    println "Version: ${pom.version}"
                    println "Encoding: ${pom.properties['project.build.sourceEncoding']}"
                }
            }
        }
    }
}
```

### Read POM with Full Path

```groovy
script {
    def po = readMavenPom file: 'C:\\Users\\shamim\\eclipse-workspace\\MyMavenProj\\pom.xml'
    println "Some properties: " + po.properties['project.build.sourceEncoding']
    println "GroupId: " + po.groupId
}
```

## Advanced Examples

### Complete Pipeline with Multiple Features

```groovy
def aa = 100

def sums() {
    println("Custom method called")
}

def n = "SHAMIM"
def x 

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
                script {
                    // String manipulation
                    x = n.toLowerCase()
                }
                
                // Directory operations
                pwd()
                deleteDir()
                
                // Git clone
                bat "git clone https://github.com/wilsonshamim/MyMavenProject1.git ."
                
                // Call custom method
                sums()
                
                // Use variables
                bat "echo ${aa}+${a}+${b}"
                bat "echo ${x}"
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

### Error Handling

```groovy
pipeline {
    agent any
    
    stages {
        stage('Safe Execution') {
            steps {
                script {
                    try {
                        def result = someRiskyOperation()
                        println "Success: ${result}"
                    } catch (Exception e) {
                        println "Error occurred: ${e.message}"
                        currentBuild.result = 'FAILURE'
                    }
                }
            }
        }
    }
}
```

### Working with Collections

```groovy
pipeline {
    agent any
    
    stages {
        stage('Collections') {
            steps {
                script {
                    // Lists
                    def items = ['item1', 'item2', 'item3']
                    items.each { item ->
                        println "Processing: ${item}"
                    }
                    
                    // Maps
                    def config = [
                        env: 'production',
                        version: '1.0.0',
                        debug: false
                    ]
                    
                    println "Environment: ${config.env}"
                    println "Version: ${config.version}"
                }
            }
        }
    }
}
```

## Best Practices

1. **Keep scripts simple** - Complex logic should be in shared libraries
2. **Use meaningful variable names** - Make code self-documenting
3. **Handle errors gracefully** - Use try-catch blocks
4. **Avoid global state** - Minimize side effects
5. **Comment complex logic** - Help others understand your code
6. **Test scripts locally** - Use Groovy console for testing
7. **Use environment variables** - For configuration values
8. **Validate inputs** - Check parameters and file existence

## Common Pitfalls

- **Serialization issues** - Not all objects can be serialized in pipelines
- **CPS transformation** - Some Groovy features don't work in pipelines
- **Variable scope** - Be aware of where variables are defined
- **Performance** - Heavy computation in scripts can slow builds

## Tips

- Use `@NonCPS` annotation for methods that don't need to be serialized
- Leverage Jenkins built-in steps when possible
- Use shared libraries for reusable code
- Keep pipeline scripts in version control
- Use the Pipeline Syntax Generator for complex steps

## Resources

- [Pipeline Groovy Documentation](https://www.jenkins.io/doc/book/pipeline/syntax/)
- [Pipeline Steps Reference](https://www.jenkins.io/doc/pipeline/steps/)
- [Groovy Language Documentation](https://groovy-lang.org/documentation.html)