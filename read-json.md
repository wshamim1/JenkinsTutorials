# Reading JSON and CSV Files in Jenkins Pipeline

Learn how to read and parse JSON and CSV files in your Jenkins pipelines using the Pipeline Utility Steps plugin.

## Prerequisites

Install the **Pipeline Utility Steps** plugin:
1. Go to **Manage Jenkins** → **Manage Plugins**
2. Search for "Pipeline Utility Steps"
3. Install the plugin

## Reading JSON Files

### Basic JSON Reading

```groovy
def apps = readJSON file: 'test.json'
apps.apps.each { 
    det -> println "${det}"
}
```

### Example JSON File (test.json)

```json
{
    "apps": [
        {
            "name": "app1",
            "version": "1.0.0"
        },
        {
            "name": "app2",
            "version": "2.0.0"
        }
    ]
}
```

### Complete Pipeline Example

```groovy
pipeline {
    agent any
    
    stages {
        stage('Read JSON') {
            steps {
                script {
                    def apps = readJSON file: 'test.json'
                    
                    // Iterate through apps
                    apps.apps.each { app ->
                        println "App Name: ${app.name}"
                        println "Version: ${app.version}"
                    }
                }
            }
        }
    }
}
```

## Reading CSV Files

### Basic CSV Reading

```groovy
def csvs = readCSV file: 'test.csv'
csvs.each { 
    det -> println "${det}"
}
```

### Example CSV File (test.csv)

```csv
name,version,status
app1,1.0.0,active
app2,2.0.0,inactive
app3,3.0.0,active
```

### Complete Pipeline Example

```groovy
pipeline {
    agent any
    
    stages {
        stage('Read CSV') {
            steps {
                script {
                    def csvs = readCSV file: 'test.csv'
                    
                    // Iterate through CSV rows
                    csvs.each { row ->
                        println "Row: ${row}"
                        println "Name: ${row[0]}, Version: ${row[1]}, Status: ${row[2]}"
                    }
                }
            }
        }
    }
}
```

## Advanced Usage

### Reading JSON from Text

```groovy
def jsonText = '{"name": "test", "value": 123}'
def jsonObj = readJSON text: jsonText
println "Name: ${jsonObj.name}"
println "Value: ${jsonObj.value}"
```

### Reading CSV with Headers

```groovy
def records = readCSV file: 'test.csv', format: CSVFormat.DEFAULT.withHeader()
records.each { record ->
    println "Name: ${record.name}"
    println "Version: ${record.version}"
    println "Status: ${record.status}"
}
```

### Writing JSON

```groovy
def data = [
    name: 'myapp',
    version: '1.0.0',
    features: ['feature1', 'feature2']
]

writeJSON file: 'output.json', json: data
```

## Practical Examples

### Configuration Management

```groovy
pipeline {
    agent any
    
    stages {
        stage('Load Config') {
            steps {
                script {
                    // Read configuration from JSON
                    def config = readJSON file: 'config.json'
                    
                    // Use configuration values
                    env.APP_NAME = config.appName
                    env.VERSION = config.version
                    env.ENVIRONMENT = config.environment
                    
                    echo "Deploying ${env.APP_NAME} v${env.VERSION} to ${env.ENVIRONMENT}"
                }
            }
        }
    }
}
```

### Processing Test Results

```groovy
pipeline {
    agent any
    
    stages {
        stage('Process Results') {
            steps {
                script {
                    // Read test results from CSV
                    def results = readCSV file: 'test-results.csv'
                    
                    def passed = 0
                    def failed = 0
                    
                    results.each { result ->
                        if (result[2] == 'PASS') {
                            passed++
                        } else {
                            failed++
                        }
                    }
                    
                    echo "Tests Passed: ${passed}"
                    echo "Tests Failed: ${failed}"
                }
            }
        }
    }
}
```

## Best Practices

- Validate file existence before reading
- Handle parsing errors gracefully
- Use try-catch blocks for error handling
- Store configuration files in version control
- Use meaningful variable names
- Document expected file formats

## Error Handling

```groovy
pipeline {
    agent any
    
    stages {
        stage('Read with Error Handling') {
            steps {
                script {
                    try {
                        def data = readJSON file: 'config.json'
                        echo "Successfully read JSON: ${data}"
                    } catch (Exception e) {
                        echo "Error reading JSON: ${e.message}"
                        currentBuild.result = 'FAILURE'
                    }
                }
            }
        }
    }
}
```

## Additional Resources

- [Pipeline Utility Steps Plugin Documentation](https://plugins.jenkins.io/pipeline-utility-steps/)
- [Apache Commons CSV Format](https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.html)