package com.mail.sample.model;

public class ProxyModel {

    private String host;
    private Integer port;
    private String user;
    private String password;
    private Boolean active;

    public ProxyModel() {
    }

    public ProxyModel(String host, Integer port, String user, String password, Boolean active) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.active = active;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
