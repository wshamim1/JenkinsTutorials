# Checkout Groovy Script from SCM

Learn how to configure Jenkins to load pipeline scripts directly from Source Control Management (SCM) like Git.

## Overview

Instead of writing pipeline scripts directly in Jenkins, you can store them in your Git repository and have Jenkins checkout and execute them. This approach provides version control and easier collaboration.

## Method 1: Pipeline Script from SCM

### Configuration Steps

1. Create a pipeline project in Jenkins
2. In the **Pipeline** section, select **"Pipeline script from SCM"** from the dropdown
3. Configure the following:
   - **SCM**: Select "Git"
   - **Repository URL**: Enter your Git repository URL
     ```
     https://github.com/wilsonshamim/JenkinsTutorials.git
     ```
   - **Branches to build**: Specify the branch (e.g., `*/master`)
   - **Script Path**: Provide the Jenkinsfile name
     ```
     jenkinsfile.groovy
     ```
4. Click **Save**
5. Click **Build Now** to run the pipeline

### Benefits

- Version control for your pipeline scripts
- Easy collaboration with team members
- Track changes and rollback if needed
- Reuse pipelines across multiple projects

## Method 2: Generate Checkout Script

You can also generate the checkout script using Jenkins' Pipeline Syntax Generator.

### Steps

1. In your pipeline configuration, click **Pipeline Syntax**
2. Select **checkout: Check out from version control**
3. Configure your Git repository settings
4. Click **Generate Pipeline Script**

### Generated Script Example

```groovy
checkout([
    $class: 'GitSCM', 
    branches: [[name: '*/master']], 
    doGenerateSubmoduleConfigurations: false, 
    extensions: [], 
    submoduleCfg: [], 
    userRemoteConfigs: [[url: 'https://github.com/wilsonshamim/JenkinsTutorials.git']]
])
```

## Using Checkout in Pipeline

You can use the generated checkout script within your pipeline:

```groovy
pipeline {
    agent any
    
    stages {
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM', 
                    branches: [[name: '*/master']], 
                    userRemoteConfigs: [[url: 'https://github.com/wilsonshamim/JenkinsTutorials.git']]
                ])
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building...'
            }
        }
    }
}
```

## Advanced Options

### Checkout Specific Branch

```groovy
checkout([
    $class: 'GitSCM',
    branches: [[name: '*/develop']],
    userRemoteConfigs: [[url: 'https://github.com/wilsonshamim/JenkinsTutorials.git']]
])
```

### Checkout with Credentials

```groovy
checkout([
    $class: 'GitSCM',
    branches: [[name: '*/master']],
    userRemoteConfigs: [[
        url: 'https://github.com/wilsonshamim/JenkinsTutorials.git',
        credentialsId: 'github-credentials'
    ]]
])
```

### Shallow Clone

```groovy
checkout([
    $class: 'GitSCM',
    branches: [[name: '*/master']],
    extensions: [[$class: 'CloneOption', depth: 1, shallow: true]],
    userRemoteConfigs: [[url: 'https://github.com/wilsonshamim/JenkinsTutorials.git']]
])
```

## Best Practices

- Store Jenkinsfiles in the root of your repository
- Use meaningful names for your Jenkinsfiles
- Keep pipeline scripts under version control
- Use branches for testing pipeline changes
- Document your pipeline configuration
- Use credentials for private repositories

## Troubleshooting

- Ensure Git plugin is installed in Jenkins
- Verify repository URL is correct and accessible
- Check branch name matches your repository
- Verify script path points to the correct file
- Ensure Jenkins has proper credentials for private repos