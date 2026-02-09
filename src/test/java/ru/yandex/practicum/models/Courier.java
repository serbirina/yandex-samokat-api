package ru.yandex.practicum.models;

public class Courier {

    private String login;
    private String password;
    private Integer courierId;

    public Courier(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Courier() {
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Integer getCourierId() {
        return courierId;
    }

    public Courier setLogin(String login) {
        this.login = login;
        return this;
    }

    public Courier setPassword(String password) {
        this.password = password;
        return this;
    }

    public Courier setCourierId(Integer courierId) {
        this.courierId = courierId;
        return this;
    }
}
