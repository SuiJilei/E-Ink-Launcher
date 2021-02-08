package cn.modificator.launcher.floatball;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import cn.modificator.launcher.R;
import cn.modificator.launcher.jdreadutil.DeviceControl;


/**
 * Created by ZY on 2016/8/10.
 * 底部菜单栏
 */
public class FloatMenu extends LinearLayout {


    public FloatMenu(final Context context) {
        super(context);
        View root = View.inflate(context, R.layout.float_menu, null);
        LinearLayout layout = root.findViewById(R.id.float_menu_layout);
        TextView closeLight = root.findViewById(R.id.close_front_light);
        TextView fullLight = root.findViewById(R.id.full_front_light);
        final RatingBar light_bar = root.findViewById(R.id.light_settings_bar);
        final DeviceControl device_control = new DeviceControl();
        light_bar.setRating(device_control.getFrontLightConfigValue(context));
        root.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FloatViewManager manager = FloatViewManager.getInstance(context);
                if(manager.getFloatBallState())
                {
                    manager.showFloatBall();
                }
                manager.hideFloatMenu();
                return false;
            }
        });
        closeLight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                light_bar.setRating(0);
                device_control.setFrontLightValue(context,0);
                device_control.setFrontLightConfigValue(context,0);
            }
        });

        fullLight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                light_bar.setRating(20);
                device_control.setFrontLightValue(context,20);
                device_control.setFrontLightConfigValue(context,20);
            }
        });

        light_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean fromUser) {
                if (fromUser) {
                    device_control.setFrontLightValue(context,(int)v);
                    device_control.setFrontLightConfigValue(context,(int)v);
                }
            }
        });

        addView(root);
    }


}