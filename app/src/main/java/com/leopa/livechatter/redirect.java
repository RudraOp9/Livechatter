package com.leopa.livechatter;

public class redirect {

    private int mimg;
    private String msite;

    public redirect(int mimg, String msite) {
        this.mimg = mimg;
        this.msite = msite;
    }

    public int getMimg() {
        return mimg;
    }

    public void setMimg(int mimg) {
        this.mimg = mimg;
    }

    public String getMsite() {
        return msite;
    }

    public void setMsite(String msite) {
        this.msite = msite;
    }
}
