package com.minkiapps.hos.test.net.model;

public class Joke {

    private String id;
    private String url;
    private String value;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Joke{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
