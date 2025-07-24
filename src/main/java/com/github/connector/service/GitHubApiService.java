package com.github.connector.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.connector.model.CommitInfo;
import com.github.connector.model.RepoInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitHubApiService {

    private static final String BASE_URL = "https://api.github.com";
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final String token;

    public GitHubApiService(String token) {
        this.token = token;
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<RepoInfo> getRepos(String username) throws IOException {
        List<RepoInfo> repos = new ArrayList<>();
        int page = 1;

        while (true) {
            String url = BASE_URL + "/users/" + username + "/repos?per_page=100&page=" + page;

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "token " + token)
                    .header("Accept", "application/vnd.github.v3+json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.err.println("Failed to fetch repositories: " + response.message());
                    break;
                }

                JsonNode repoArray = objectMapper.readTree(response.body().string());

                if (repoArray.isEmpty()) break;

                for (JsonNode repoNode : repoArray) {
                    String repoName = repoNode.get("name").asText();
                    RepoInfo repo = new RepoInfo();
                    repo.setName(repoName);
                    repo.setCommits(getCommits(username, repoName));
                    repos.add(repo);
                }

                page++;
            }
        }

        return repos;
    }

    public List<CommitInfo> getCommits(String owner, String repoName) throws IOException {
        List<CommitInfo> commits = new ArrayList<>();

        String url = BASE_URL + "/repos/" + owner + "/" + repoName + "/commits?per_page=20";

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "token " + token)
                .header("Accept", "application/vnd.github.v3+json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Failed to fetch commits for repo " + repoName + ": " + response.message());
                return commits;
            }

            JsonNode commitArray = objectMapper.readTree(response.body().string());

            for (JsonNode commitNode : commitArray) {
                JsonNode commit = commitNode.get("commit");

                CommitInfo commitInfo = new CommitInfo();
                commitInfo.setMessage(commit.get("message").asText());
                commitInfo.setAuthor(commit.get("author").get("name").asText());
                commitInfo.setTimestamp(commit.get("author").get("date").asText());

                commits.add(commitInfo);
            }
        }

        return commits;
    }
}
