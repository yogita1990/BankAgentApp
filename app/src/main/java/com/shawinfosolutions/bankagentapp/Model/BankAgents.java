package com.shawinfosolutions.bankagentapp.Model;

public class BankAgents {


    private String agentLoginStatus,agentName,agentPhoto,agentToken,agentmobileNo,branchName,id;

    public BankAgents() {
    }

    public BankAgents(String id,String branchName, String agentName, String agentmobileNo,String agentPhoto,  String agentLoginStatus,String agentToken) {
        this.agentLoginStatus = agentLoginStatus;
        this.agentName = agentName;
        this.agentPhoto = agentPhoto;
        this.agentToken = agentToken;
        this.agentmobileNo = agentmobileNo;
        this.branchName = branchName;
        this.id = id;
    }

    public String getAgentLoginStatus() {
        return agentLoginStatus;
    }

    public void setAgentLoginStatus(String agentLoginStatus) {
        this.agentLoginStatus = agentLoginStatus;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentPhoto() {
        return agentPhoto;
    }

    public void setAgentPhoto(String agentPhoto) {
        this.agentPhoto = agentPhoto;
    }

    public String getAgentToken() {
        return agentToken;
    }

    public void setAgentToken(String agentToken) {
        this.agentToken = agentToken;
    }

    public String getAgentmobileNo() {
        return agentmobileNo;
    }

    public void setAgentmobileNo(String agentmobileNo) {
        this.agentmobileNo = agentmobileNo;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
