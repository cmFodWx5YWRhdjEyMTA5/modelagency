package com.talentnew.models;

import java.util.List;

public class Boost {
    private int id;
    private String header,pay;
    private List<Object> itemList;

    private int agentId,boostId,jobPost,emailShoutOut,fbShoutOut,boostJob,customAdd;
    private String title,dedicatedManager,contactModel,proTag,verifiedTag,
            validity,scheme,userName;
    private float amount;

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getBoostId() {
        return boostId;
    }

    public void setBoostId(int boostId) {
        this.boostId = boostId;
    }

    public int getJobPost() {
        return jobPost;
    }

    public void setJobPost(int jobPost) {
        this.jobPost = jobPost;
    }

    public int getEmailShoutOut() {
        return emailShoutOut;
    }

    public void setEmailShoutOut(int emailShoutOut) {
        this.emailShoutOut = emailShoutOut;
    }

    public int getFbShoutOut() {
        return fbShoutOut;
    }

    public void setFbShoutOut(int fbShoutOut) {
        this.fbShoutOut = fbShoutOut;
    }

    public int getBoostJob() {
        return boostJob;
    }

    public void setBoostJob(int boostJob) {
        this.boostJob = boostJob;
    }

    public int getCustomAdd() {
        return customAdd;
    }

    public void setCustomAdd(int customAdd) {
        this.customAdd = customAdd;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDedicatedManager() {
        return dedicatedManager;
    }

    public void setDedicatedManager(String dedicatedManager) {
        this.dedicatedManager = dedicatedManager;
    }

    public String getContactModel() {
        return contactModel;
    }

    public void setContactModel(String contactModel) {
        this.contactModel = contactModel;
    }

    public String getProTag() {
        return proTag;
    }

    public void setProTag(String proTag) {
        this.proTag = proTag;
    }

    public String getVerifiedTag() {
        return verifiedTag;
    }

    public void setVerifiedTag(String verifiedTag) {
        this.verifiedTag = verifiedTag;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public List<Object> getItemList() {
        return itemList;
    }

    public void setItemList(List<Object> itemList) {
        this.itemList = itemList;
    }
}
