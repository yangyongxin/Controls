package com.example.yangyongxin.app2.bean;

/**
 * Created by yangyongxin on 15/8/28.
 */
public class Ad {
    private int iconResId;
    private String intro;


    public Ad() {
    }

    public Ad(int iconResId, String intro) {
        this.iconResId = iconResId;
        this.intro = intro;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
