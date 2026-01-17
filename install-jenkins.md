# Installing Jenkins

This guide walks you through the process of installing and running Jenkins locally.

## Prerequisites

- Java JDK 8 or higher installed on your system

## Installation Steps

### 1. Download Jenkins

Download the `jenkins.war` file from the [official Jenkins website](https://www.jenkins.io/download/).

### 2. Start Jenkins

Run the following command to start Jenkins on a custom port:

```bash
java -jar jenkins.war --httpPort=9090
```

This will start Jenkins on port 9090.

### 3. Access Jenkins

Open your web browser and navigate to:
```
http://localhost:9090
```

### 4. Initial Setup

1. **Username**: Use `admin` as the default username
2. **Password**: The initial admin password is provided in a security file during startup
   - The file location will be displayed in the console output
   - Typically located at: `~/.jenkins/secrets/initialAdminPassword`

### 5. Complete Setup Wizard

Follow the on-screen instructions to:
- Install suggested plugins or select specific plugins
- Create your first admin user
- Configure Jenkins URL

## Troubleshooting

- If port 9090 is already in use, choose a different port number
- Ensure Java is properly installed and accessible from your PATH
- Check firewall settings if you cannot access Jenkins from your browser

## Next Steps

After installation, you can:
- Install additional plugins
- Create your first pipeline
- Configure build tools (Maven, Gradle, etc.)