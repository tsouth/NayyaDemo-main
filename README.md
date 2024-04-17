## Automated integration tests for the Nayya Demo Request Form
Go Go Gadget Nayya :)


PreRequisite installations:
- Maven - https://maven.apache.org/install.html
- Java Development Kit - https://www.oracle.com/java/technologies/downloads/#jdk19-mac

You will also need to set environment variables `Java_Home` and `M2_Home`

Example:
```
export M2_HOME=/Users/thomassouthworth/Library/apache-maven-3.8.6
export PATH=$PATH:$M2_HOME/bin
export JAVA_HOME=$(/usr/libexec/java_home)
```

Tests can be run using Maven's `mvn test` command. 

### Local execution

Drivers are included for MacOS Chrome. Other drivers can be included easily, but the test runner does not attempt to load a platform-appropriate binary; only MacOS binaries are included.

To run locally, set the `BROWSER` environment variable to `chrome`

## CLI commands


To run a test suite locally, the CLI command will look like: `mvn -Psuite -Pchrome test -DsuiteFile=smokeTests.xml -e`

*Note*
The testng.xml's are immutable, so we rely on dictating production or staging xml's to run tests in production or staging environments, instead of setting the `Environment` variable to `staging`.


