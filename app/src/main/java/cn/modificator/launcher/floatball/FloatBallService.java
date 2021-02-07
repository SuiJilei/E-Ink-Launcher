package cn.modificator.launcher.floatball;


import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import cn.modificator.launcher.Launcher;
import cn.modificator.launcher.R;


/**
 * 悬浮窗Service 该服务会在后台一直运行一个悬浮的透明的窗体
 *
 * @author Administrator
 *
 */

public class FloatBallService extends Service {

    private  FloatViewManager manager;
    public FloatBallService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manager = FloatViewManager.getInstance(this);
        manager.CreateFloatBall();
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        manager.removeFloatBall();

    }
}