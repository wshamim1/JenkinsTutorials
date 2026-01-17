# Using Credentials in Jenkins Pipeline

Learn how to securely manage and use credentials in your Jenkins pipelines using the `withCredentials` block.

## Overview

The `withCredentials` step allows you to bind credentials to variables in your pipeline, ensuring sensitive information like passwords, API keys, and tokens are handled securely.

## Prerequisites

- Credentials must be configured in Jenkins
- Credentials Binding Plugin (usually pre-installed)

## Configuring Credentials

### Adding Credentials in Jenkins

1. Go to **Manage Jenkins** → **Manage Credentials**
2. Select appropriate domain (usually "Global")
3. Click **Add Credentials**
4. Choose credential type:
   - Username with password
   - Secret text
   - Secret file
   - SSH Username with private key
   - Certificate

## Using Pipeline Syntax Generator

### Generate withCredentials Block

1. In your pipeline configuration, click **Pipeline Syntax**
2. Select **withCredentials: Bind credentials to variables**
3. Click **Add** and select appropriate credential type
4. For **Username and password (separated)**:
   - Enter username variable name (e.g., `USERNAME`)
   - Enter password variable name (e.g., `PASSWORD`)
   - Select your credential from the dropdown
5. Click **Generate Pipeline Script**

### Generated Script Example

```groovy
withCredentials([
    usernamePassword(
        credentialsId: '81dd9bce-1bed-415a-87ac-945b1b3a9fdc',
        passwordVariable: 'PASSWORD',
        usernameVariable: 'USERNAME'
    )
]) {
    // Use credentials here
    echo "Username: ${USERNAME}"
    // Password is masked in logs
}
```

## Credential Types

### 1. Username and Password (Separated)

```groovy
pipeline {
    agent any
    
    stages {
        stage('Deploy') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'my-credentials-id',
                        passwordVariable: 'PASSWORD',
                        usernameVariable: 'USERNAME'
                    )
                ]) {
                    bat "deploy.bat ${USERNAME} ${PASSWORD}"
                }
            }
        }
    }
}
```

### 2. Username and Password (Combined)

```groovy
pipeline {
    agent any
    
    stages {
        stage('Login') {
            steps {
                withCredentials([
                    usernameColonPassword(
                        credentialsId: 'my-credentials-id',
                        variable: 'USERPASS'
                    )
                ]) {
                    sh 'curl -u $USERPASS https://api.example.com'
                }
            }
        }
    }
}
```

### 3. Secret Text

```groovy
pipeline {
    agent any
    
    stages {
        stage('Use API Key') {
            steps {
                withCredentials([
                    string(
                        credentialsId: 'api-key-id',
                        variable: 'API_KEY'
                    )
                ]) {
                    sh 'curl -H "Authorization: Bearer ${API_KEY}" https://api.example.com'
                }
            }
        }
    }
}
```

### 4. Secret File

```groovy
pipeline {
    agent any
    
    stages {
        stage('Use Config File') {
            steps {
                withCredentials([
                    file(
                        credentialsId: 'config-file-id',
                        variable: 'CONFIG_FILE'
                    )
                ]) {
                    sh 'cat ${CONFIG_FILE}'
                    sh 'app --config ${CONFIG_FILE}'
                }
            }
        }
    }
}
```

### 5. SSH Private Key

```groovy
pipeline {
    agent any
    
    stages {
        stage('SSH Deploy') {
            steps {
                withCredentials([
                    sshUserPrivateKey(
                        credentialsId: 'ssh-key-id',
                        keyFileVariable: 'SSH_KEY',
                        usernameVariable: 'SSH_USER'
                    )
                ]) {
                    sh 'ssh -i ${SSH_KEY} ${SSH_USER}@server.com "deploy.sh"'
                }
            }
        }
    }
}
```

## Multiple Credentials

### Using Multiple Credentials Together

```groovy
pipeline {
    agent any
    
    stages {
        stage('Deploy with Multiple Credentials') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'database-creds',
                        passwordVariable: 'DB_PASSWORD',
                        usernameVariable: 'DB_USERNAME'
                    ),
                    string(
                        credentialsId: 'api-key',
                        variable: 'API_KEY'
                    ),
                    file(
                        credentialsId: 'ssl-cert',
                        variable: 'SSL_CERT'
                    )
                ]) {
                    sh '''
                        echo "Deploying with credentials..."
                        deploy.sh --db-user ${DB_USERNAME} \
                                  --db-pass ${DB_PASSWORD} \
                                  --api-key ${API_KEY} \
                                  --ssl-cert ${SSL_CERT}
                    '''
                }
            }
        }
    }
}
```

## Practical Examples

### Docker Login

```groovy
pipeline {
    agent any
    
    stages {
        stage('Docker Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        passwordVariable: 'DOCKER_PASSWORD',
                        usernameVariable: 'DOCKER_USERNAME'
                    )
                ]) {
                    sh '''
                        echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin
                        docker push myimage:latest
                    '''
                }
            }
        }
    }
}
```

### Git Operations with Credentials

```groovy
pipeline {
    agent any
    
    stages {
        stage('Git Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'github-credentials',
                        passwordVariable: 'GIT_PASSWORD',
                        usernameVariable: 'GIT_USERNAME'
                    )
                ]) {
                    sh '''
                        git config user.name "${GIT_USERNAME}"
                        git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/user/repo.git
                    '''
                }
            }
        }
    }
}
```

### AWS Credentials

```groovy
pipeline {
    agent any
    
    stages {
        stage('Deploy to AWS') {
            steps {
                withCredentials([
                    string(
                        credentialsId: 'aws-access-key-id',
                        variable: 'AWS_ACCESS_KEY_ID'
                    ),
                    string(
                        credentialsId: 'aws-secret-access-key',
                        variable: 'AWS_SECRET_ACCESS_KEY'
                    )
                ]) {
                    sh '''
                        aws s3 cp build/ s3://my-bucket/ --recursive
                    '''
                }
            }
        }
    }
}
```

### Database Connection

```groovy
pipeline {
    agent any
    
    stages {
        stage('Database Migration') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'database-credentials',
                        passwordVariable: 'DB_PASS',
                        usernameVariable: 'DB_USER'
                    ),
                    string(
                        credentialsId: 'database-host',
                        variable: 'DB_HOST'
                    )
                ]) {
                    sh '''
                        mysql -h ${DB_HOST} -u ${DB_USER} -p${DB_PASS} < migration.sql
                    '''
                }
            }
        }
    }
}
```

## Security Best Practices

### 1. Never Echo Credentials

```groovy
// ❌ BAD - Exposes password in logs
withCredentials([
    usernamePassword(
        credentialsId: 'creds',
        passwordVariable: 'PASSWORD',
        usernameVariable: 'USERNAME'
    )
]) {
    echo "Password is: ${PASSWORD}"  // DON'T DO THIS!
}

// ✅ GOOD - Credentials are masked
withCredentials([
    usernamePassword(
        credentialsId: 'creds',
        passwordVariable: 'PASSWORD',
        usernameVariable: 'USERNAME'
    )
]) {
    sh 'deploy.sh ${USERNAME} ${PASSWORD}'  // Credentials are masked in logs
}
```

### 2. Limit Credential Scope

```groovy
pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                echo "Building..."
            }
        }
        
        stage('Deploy') {
            steps {
                // Only use credentials where needed
                withCredentials([
                    usernamePassword(
                        credentialsId: 'deploy-creds',
                        passwordVariable: 'PASSWORD',
                        usernameVariable: 'USERNAME'
                    )
                ]) {
                    sh 'deploy.sh'
                }
            }
        }
    }
}
```

### 3. Use Credential IDs, Not Hardcoded Values

```groovy
// ❌ BAD
def password = "hardcoded-password"

// ✅ GOOD
withCredentials([
    string(credentialsId: 'my-password', variable: 'PASSWORD')
]) {
    // Use PASSWORD variable
}
```

### 4. Rotate Credentials Regularly

- Update credentials in Jenkins periodically
- Use credential IDs so pipelines don't need updates
- Monitor credential usage

## Troubleshooting

### Credential Not Found

```groovy
pipeline {
    agent any
    
    stages {
        stage('Check Credentials') {
            steps {
                script {
                    try {
                        withCredentials([
                            string(credentialsId: 'my-cred-id', variable: 'CRED')
                        ]) {
                            echo "Credential found"
                        }
                    } catch (Exception e) {
                        error "Credential not found: ${e.message}"
                    }
                }
            }
        }
    }
}
```

### Debugging Credential Issues

```groovy
pipeline {
    agent any
    
    stages {
        stage('Debug') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'my-creds',
                        passwordVariable: 'PASSWORD',
                        usernameVariable: 'USERNAME'
                    )
                ]) {
                    // Check if variables are set (values are masked)
                    sh 'echo "Username length: ${#USERNAME}"'
                    sh 'echo "Password is set: $([ -n "$PASSWORD" ] && echo "yes" || echo "no")'
                }
            }
        }
    }
}
```

## Advanced Usage

### Conditional Credential Usage

```groovy
pipeline {
    agent any
    
    parameters {
        choice(name: 'ENVIRONMENT', choices: ['dev', 'staging', 'prod'])
    }
    
    stages {
        stage('Deploy') {
            steps {
                script {
                    def credId = "deploy-${params.ENVIRONMENT}-creds"
                    
                    withCredentials([
                        usernamePassword(
                            credentialsId: credId,
                            passwordVariable: 'PASSWORD',
                            usernameVariable: 'USERNAME'
                        )
                    ]) {
                        sh "deploy.sh ${params.ENVIRONMENT}"
                    }
                }
            }
        }
    }
}
```

## Resources

- [Credentials Plugin Documentation](https://plugins.jenkins.io/credentials/)
- [Credentials Binding Plugin](https://plugins.jenkins.io/credentials-binding/)
- [Jenkins Security Best Practices](https://www.jenkins.io/doc/book/security/)