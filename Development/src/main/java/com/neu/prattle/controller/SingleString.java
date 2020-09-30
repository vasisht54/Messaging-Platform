package com.neu.prattle.controller;

/**
 * A POJO class to store a single string via a {@POST} request.
 */
class SingleString {
    private String value;

    /**
     * Getter method for the string.
     *
     * @return the string
     */
    public String getValue() {
        return value;
    }

    /**
     * Setter method for the string value.
     *
     * @param value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
