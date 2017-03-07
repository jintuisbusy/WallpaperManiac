package com.jackapps.wallpaper;

/**
 * Created by Jack on 2/28/2017.
 */
public class Wallpaper {


    public Wallpaper() {
    }

    String webformatURL;
    String hashId;

    public Wallpaper(String webformatURL,String hashId) {
        this.webformatURL = webformatURL;
        this.hashId = hashId;
    }

    public String getwebformatURL() {
        return webformatURL;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

}
