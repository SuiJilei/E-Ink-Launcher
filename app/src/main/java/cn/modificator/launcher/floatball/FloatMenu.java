package cn.modificator.launcher.floatball;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import cn.modificator.launcher.R;


/**
 * Created by ZY on 2016/8/10.
 * 底部菜单栏
 */
public class FloatMenu extends LinearLayout {

    private TranslateAnimation animation;

    public FloatMenu(final Context context) {
        super(context);
        View root = View.inflate(context, R.layout.float_menu, null);
        LinearLayout layout = root.findViewById(R.id.float_menu_layout);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(500);
        animation.setFillAfter(true);
        layout.setAnimation(animation);
        root.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FloatViewManager manager = FloatViewManager.getInstance(context);
                manager.showFloatBall();
                manager.hideFloatMenu();
                return false;
            }
        });
        addView(root);
    }

    public void startAnimation() {
        animation.start();
    }

}