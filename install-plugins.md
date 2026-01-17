# Installing Jenkins Plugins

Learn how to install and manage plugins in Jenkins to extend its functionality.

## Installation Steps

### 1. Navigate to Plugin Manager

1. Go to **Manage Jenkins** from the Jenkins dashboard
2. Click on **Manage Plugins**

### 2. Browse Available Plugins

1. Select the **Available** tab
2. You will see a list of all available plugins

### 3. Install a Plugin

For example, to install the Groovy plugin:

1. Type "groovy" in the search field
2. Check the checkbox next to the desired plugin
3. Click **Install without restart** (recommended) or **Download now and install after restart**

## Recommended Plugins

Here are some commonly used plugins for Jenkins pipelines:

- **Pipeline Utility Steps** - Provides utility steps for pipelines (reading JSON, CSV, etc.)
- **Git Plugin** - Integrates Git with Jenkins
- **Maven Integration** - For building Maven projects
- **Credentials Plugin** - Manages credentials securely
- **Blue Ocean** - Modern UI for Jenkins pipelines
- **Docker Pipeline** - Docker integration for pipelines

## Managing Installed Plugins

### Update Plugins

1. Go to **Manage Jenkins** → **Manage Plugins**
2. Select the **Updates** tab
3. Check plugins you want to update
4. Click **Download now and install after restart**

### Uninstall Plugins

1. Go to **Manage Jenkins** → **Manage Plugins**
2. Select the **Installed** tab
3. Find the plugin you want to remove
4. Click the **Uninstall** button

## Best Practices

- Always review plugin dependencies before installation
- Keep plugins updated for security and bug fixes
- Only install plugins you actually need
- Test plugin updates in a non-production environment first
- Restart Jenkins after installing critical plugins

## Troubleshooting

- If a plugin fails to install, check the Jenkins logs
- Ensure you have sufficient permissions
- Verify Jenkins version compatibility with the plugin
- Check for conflicting plugins