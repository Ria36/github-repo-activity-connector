package com.github.connector;

import com.github.connector.model.RepoInfo;
import com.github.connector.model.CommitInfo;
import com.github.connector.service.GitHubApiService;

import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter GitHub username or org: ");
        String username = scanner.nextLine();

        System.out.print("Enter your GitHub personal access token: ");
        String token = scanner.nextLine();

        GitHubApiService service = new GitHubApiService(token);

        try {
            List<RepoInfo> repos = service.getRepos(username);
            for (RepoInfo repo : repos) {
                System.out.println("Repository: " + repo.getName());
                for (CommitInfo commit : repo.getCommits()) {
                    System.out.println("  - Message: " + commit.getMessage());
                    System.out.println("    Author: " + commit.getAuthor());
                    System.out.println("    Timestamp: " + commit.getTimestamp());
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching data: " + e.getMessage());
        }
    }
}
