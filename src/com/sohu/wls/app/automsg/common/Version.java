package com.sohu.wls.app.automsg.common;

/**
 * User: chaocui200783
 * Date: 13-4-23
 * Time: 下午6:07
 */
public class Version {
    private int version;
    private String url;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Version{" +
                "version=" + version +
                ", url='" + url + '\'' +
                '}';
    }
}
