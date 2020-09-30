package com.neu.prattle.controller;

/**
 * A POJO class to store Login credentials sent
 * via a {@POST} request.
 */
class Login {
    private String username;
    private String password;

    /**
     * Getter method for the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter method for the username for testing.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter method for the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter method for the password for testing.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
