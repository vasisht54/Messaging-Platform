package com.neu.prattle.controller;

/**
 * A POJO class to store Login credentials sent
 * via a {@POST} request.
 */
class GroupLogin {
    private String groupName;
    private String password;

    /**
     * Getter method for the groupName.
     *
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Setter method for groupName
     *
     * @param groupName groupName
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Getter method for group password
     *
     * @return group password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter method for group password
     *
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
