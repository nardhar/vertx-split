package org.nardhar.vertxsplit.controller;

import java.util.Properties;

public class GitRepositoryState {
    private String branch; // =${git.branch}
    private String describe; // =${git.commit.id.describe}
    private String commitId; // =${git.commit.id}
    private String commitIdAbbrev; // =${git.commit.id.abbrev}
    private String buildUserName; // =${git.build.user.name}
    private String buildUserEmail; // =${git.build.user.email}
    private String buildTime; // =${git.build.time}
    private String commitUserName; // =${git.commit.user.name}
    private String commitUserEmail; // =${git.commit.user.email}
    private String commitMessageFull; // =${git.commit.message.full}
    private String commitMessageShort; // =${git.commit.message.short}
    private String commitTime; // =${git.commit.time}

    public GitRepositoryState() {

    }

    public GitRepositoryState(Properties properties) {
        if (properties.isEmpty())
            return;

        this.branch = properties.get("git.branch").toString();
        this.describe = properties.get("git.commit.id.describe").toString();
        this.commitId = properties.get("git.commit.id").toString();
        this.commitIdAbbrev = properties.get("git.commit.id.abbrev").toString();
        this.buildUserName = properties.get("git.build.user.name").toString();
        this.buildUserEmail = properties.get("git.build.user.email").toString();
        this.buildTime = properties.get("git.build.time").toString();
        this.commitUserName = properties.get("git.commit.user.name").toString();
        this.commitUserEmail = properties.get("git.commit.user.email").toString();
        this.commitMessageShort = properties.get("git.commit.message.short").toString();
        this.commitMessageFull = properties.get("git.commit.message.full").toString();
        this.commitTime = properties.get("git.commit.time").toString();
    }

    public String getBranch() {
        return branch;
    }

    public String getDescribe() {
        return describe;
    }

    public String getCommitId() {
        return commitId;
    }

    public String getCommitIdAbbrev() {
        return commitIdAbbrev;
    }

    public String getBuildUserName() {
        return buildUserName;
    }

    public String getBuildUserEmail() {
        return buildUserEmail;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public String getCommitUserName() {
        return commitUserName;
    }

    public String getCommitUserEmail() {
        return commitUserEmail;
    }

    public String getCommitMessageFull() {
        return commitMessageFull;
    }

    public String getCommitMessageShort() {
        return commitMessageShort;
    }

    public String getCommitTime() {
        return commitTime;
    }
}
