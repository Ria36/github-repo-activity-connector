# github-repo-activity-connector

# GitHub Repository Activity Connector

A Java CLI tool that fetches public repositories and the latest 20 commits for a given GitHub user or organization.

##  How to Use

1. Clone the repo
2. Run the app using:
mvn compile
mvn exec:java -Dexec.mainClass="com.github.connector.App"


4. Enter GitHub username and your personal access token when prompted.

##  Dependencies

- Java 11+
- OkHttp
- Jackson
