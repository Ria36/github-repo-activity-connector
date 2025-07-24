package com.github.connector.model;

import java.util.List;

public class RepoInfo {
    private String name;
    private List<CommitInfo> commits;

    public RepoInfo() {}

    public RepoInfo(String name, List<CommitInfo> commits) {
        this.name = name;
        this.commits = commits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CommitInfo> getCommits() {
        return commits;
    }

    public void setCommits(List<CommitInfo> commits) {
        this.commits = commits;
    }
}
