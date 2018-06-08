package com.wf.data.common.config;

public class FishDbConfig {
    private String apphost;
    private String appusername;
    private String apppassword;
    private Integer appport;

    public FishDbConfig(String apphost, String appusername, String apppassword, Integer appport) {
        this.apphost = apphost;
        this.appusername = appusername;
        this.apppassword = apppassword;
        this.appport = appport;
    }

    public String getApphost() {
        return apphost;
    }

    public void setApphost(String apphost) {
        this.apphost = apphost;
    }

    public String getAppusername() {
        return appusername;
    }

    public void setAppusername(String appusername) {
        this.appusername = appusername;
    }

    public String getApppassword() {
        return apppassword;
    }

    public void setApppassword(String apppassword) {
        this.apppassword = apppassword;
    }

    public Integer getAppport() {
        return appport;
    }

    public void setAppport(Integer appport) {
        this.appport = appport;
    }


    @Override
    public String toString() {
        return "FishDbConfig{" +
                "apphost='" + apphost + '\'' +
                ", appusername='" + appusername + '\'' +
                ", apppassword='" + apppassword + '\'' +
                ", appport=" + appport +
                '}';
    }
}
