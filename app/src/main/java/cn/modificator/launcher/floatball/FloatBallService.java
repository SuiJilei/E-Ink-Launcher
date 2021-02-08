package cn.modificator.launcher.floatball;



import android.app.Service;
import android.content.Intent;

import android.os.IBinder;
import androidx.annotation.Nullable;


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
    public void onCreate() {
        super.onCreate();
        manager = FloatViewManager.getInstance(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        manager.CreateFloatBall();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        manager.removeFloatBall();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}