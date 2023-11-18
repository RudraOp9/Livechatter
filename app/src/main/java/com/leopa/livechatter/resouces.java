package com.leopa.livechatter;

import android.graphics.Bitmap;
import android.net.Uri;

public class resouces {

    private static String userNameOfficial;
    private static Bitmap imageSrc;

    public String getUserNameOfficial() {
        return userNameOfficial;
    }

    public static Bitmap getImageSrc() {
        return imageSrc;
    }

    public static void setImageSrc(Bitmap imageSrc) {
        resouces.imageSrc = imageSrc;
    }

    public void setUserNameOfficial(String userNameOfficial) {
        this.userNameOfficial = userNameOfficial;
    }
}
