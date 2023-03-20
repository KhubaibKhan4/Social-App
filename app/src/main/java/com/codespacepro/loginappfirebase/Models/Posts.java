package com.codespacepro.loginappfirebase.Models;

public class Posts {
    String desc, image;

    public Posts(String desc, String image) {
        this.desc = desc;
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
