package com.hypercubes.cubic.hypercubes.util;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;

public class Util {

    private static final int FADE_IN_ANIM_TIME = 200;
    private static final int FADE_OUT_ANIM_TIME = 200;

    public static Bitmap base64ToBitmap(String base64String){
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedBitmap;
    }

    public static String removeHtmlFromJson(String jsonResponse){
        jsonResponse = jsonResponse.replace(" </body></html>", "");// Trailing html tag
        jsonResponse = jsonResponse.replace("<html><body>", "");// Leading html tag

        return jsonResponse;
    }

    public static void fadeInView(final View view) {
        view.animate().alpha(1f).setDuration(FADE_IN_ANIM_TIME).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {}

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) { }
        });
    }

    public static void fadeOutView(final View view) {
        view.animate().alpha(0f).setDuration(FADE_OUT_ANIM_TIME).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
    }
}
