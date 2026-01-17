# Build with Parameters

Learn how to create parameterized Jenkins builds that accept user input at runtime.

## Overview

Parameterized builds allow you to pass dynamic values to your pipeline, making it more flexible and reusable.

## Configuration Steps

### 1. Enable Parameters

1. Open your Jenkins project
2. Click **Configure**
3. Check the box **"This project is parameterized"**

### 2. Add Parameters

You can add various types of parameters:
- String Parameter
- Boolean Parameter
- Choice Parameter
- Password Parameter
- File Parameter

For this example, we'll add a String parameter named `intname`.

### 3. Access Parameters in Pipeline

Once configured, you can access the parameter in your pipeline script using the parameter name.

## Example Pipeline

```groovy
pipeline {
    agent any
 
    stages {
        stage('one') {
            steps {
                script {
                    // Access the parameter and convert to uppercase
                    println(intname.toUpperCase())
                }
                bat "echo one"
            }
        }
    }
}
```

## Parameter Types

### String Parameter
```groovy
parameters {
    string(name: 'USERNAME', defaultValue: 'admin', description: 'Enter username')
}
```

### Choice Parameter
```groovy
parameters {
    choice(name: 'ENVIRONMENT', choices: ['dev', 'staging', 'prod'], description: 'Select environment')
}
```

### Boolean Parameter
```groovy
parameters {
    booleanParam(name: 'DEPLOY', defaultValue: false, description: 'Deploy to server?')
}
```

## Using Parameters

Access parameters in your pipeline:

```groovy
pipeline {
    agent any
    
    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'master', description: 'Git branch to build')
        choice(name: 'BUILD_TYPE', choices: ['debug', 'release'], description: 'Build type')
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo "Building branch: ${params.BRANCH_NAME}"
                echo "Build type: ${params.BUILD_TYPE}"
            }
        }
    }
}
```

## Best Practices

- Provide meaningful parameter names and descriptions
- Set sensible default values
- Validate parameter values in your pipeline
- Use choice parameters to limit user input options
- Document required parameters in your pipeline or README

## Running Parameterized Builds

1. Click **Build with Parameters** instead of **Build Now**
2. Enter or select parameter values
3. Click **Build** to start the job

## Tips

- Parameters are available as environment variables
- Use `${params.PARAMETER_NAME}` to access parameter values
- Parameters persist between builds
- You can override parameters programmatically using the Jenkins API