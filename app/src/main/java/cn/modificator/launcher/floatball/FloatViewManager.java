package cn.modificator.launcher.floatball;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.lang.reflect.Field;

import cn.modificator.launcher.Launcher;
import cn.modificator.launcher.R;

import static android.content.Context.WINDOW_SERVICE;

public class FloatViewManager {

    private View floatBallView;
    private Button floatBallBtn;
    private FloatMenu floatMenu;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private static FloatViewManager manager;
    private boolean viewAdded = false;// 透明窗体是否已经显示
    private boolean viewHide = false; // 窗口隐藏
    private int statusBarHeight;// 状态栏高度
    private Context context;

    private WindowManager.LayoutParams floatMenuParams;

    //私有化构造函数
    private FloatViewManager(Context context) {
        this.context = context;
        init();
    }

    public static FloatViewManager getInstance(Context context) {
        if (manager == null) {
            synchronized (FloatViewManager.class) {
                if (manager == null) {
                    manager = new FloatViewManager(context);
                }
            }
        }
        return manager;
    }

    public void CreateFloatBall()
    {
        viewHide = false;
        refresh();
    }

    private void refreshView(int x, int y) {
        // 状态栏高度不能立即取，不然得到的值是0
        if (statusBarHeight == 0) {
            View rootView = floatBallView.getRootView();
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            statusBarHeight = r.top;
        }

        layoutParams.x = x;
        // y轴减去状态栏的高度，因为状态栏不是用户可以绘制的区域，不然拖动的时候会有跳动
        layoutParams.y = y - statusBarHeight;// STATUS_HEIGHT;
        refresh();
    }

    /**
     * 添加悬浮窗或者更新悬浮窗 如果悬浮窗还没添加则添加 如果已经添加则更新其位置
     */
    private void refresh() {
        // 如果已经添加了就只更新view
        if (viewAdded) {
            windowManager.updateViewLayout(floatBallView, layoutParams);
        } else {
            windowManager.addView(floatBallView, layoutParams);
            viewAdded = true;
        }
    }



    private void init() {
        floatBallView = LayoutInflater.from(context).inflate(R.layout.floating_ball, null);
        floatBallBtn = (Button) floatBallView.findViewById(R.id.float_ball_btn);
        floatMenu = new FloatMenu(context);
        windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        /*
         * LayoutParams.TYPE_SYSTEM_ERROR：保证该悬浮窗所有View的最上层
         * LayoutParams.FLAG_NOT_FOCUSABLE:该浮动窗不会获得焦点，但可以获得拖动
         * PixelFormat.TRANSPARENT：悬浮窗透明
         */
        layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
        // layoutParams.gravity = Gravity.RIGHT|Gravity.BOTTOM; //悬浮窗开始在右下角显示
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;

        /**
         * 监听窗体移动事件
         */
        floatBallBtn.setOnTouchListener(new View.OnTouchListener() {
            float[] temp = new float[] { 0f, 0f };
            float tempX;
            float tempY;

            public boolean onTouch(View v, MotionEvent event) {
                layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN: // 按下事件，记录按下时手指在悬浮窗的XY坐标值
                        floatBallBtn.setBackgroundResource(R.drawable.float_ball_bg_press);
                        temp[0] = event.getX();
                        temp[1] = event.getY();
                        tempX = event.getRawX();
                        tempY = event.getRawY();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        floatBallBtn.setBackgroundResource(R.drawable.float_ball_bg);
                        refreshView((int) (event.getRawX() - temp[0]),
                                (int) (event.getRawY() - temp[1]));
                        break;
                    case MotionEvent.ACTION_UP:
                        floatBallBtn.setBackgroundResource(R.drawable.float_ball_bg);
                        float endX = event.getRawX();
                        float endY = event.getRawY();
                        if (Math.abs(endX - tempX) > 6 && Math.abs(endY - tempY) > 6) {
                            return true;
                        }
                        break;

                }
                return false;
            }
        });
/*
        hideBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                viewHide = true;
                removeView();
                System.out.println("----------hideBtn");
            }
        });
*/
        floatBallBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context, Launcher.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                );
                context.startActivity(intent);//getApplication()不可以去掉否则没用
            }
        });


        floatBallBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //匿名内部类实现点击事件
                //showFloatMenu();
                return true;
            }
        });
    }

    private void showFloatMenu() {
        if (floatMenuParams == null) {
            floatMenuParams = new WindowManager.LayoutParams();
            floatMenuParams.width = getScreenWidth();
            floatMenuParams.height = getScreenHeight() - getStatusHeight();
            floatMenuParams.gravity = Gravity.BOTTOM;
            floatMenuParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            floatMenuParams.format = PixelFormat.RGBA_8888;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                floatMenuParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                floatMenuParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
        }
        windowManager.addView(floatMenu, floatMenuParams);
        hideFloatBall();
    }

    //隐藏底部菜单
    public void hideFloatMenu() {
        if (floatMenu != null) {
            windowManager.removeView(floatMenu);
            showFloatBall();
        }
    }

    public  void removeFloatBall()
    {
        if (viewAdded) {
            windowManager.removeView(floatBallView);
            viewAdded = false;
        }
    }

    public  void hideFloatBall()
    {
        floatBallBtn.setVisibility(View.GONE);
    }

    public  void showFloatBall()
    {
        floatBallBtn.setVisibility(View.VISIBLE);
    }

    //获取屏幕宽度
    private int getScreenWidth() {
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        return point.x;
    }

    //获取屏幕高度
    private int getScreenHeight() {
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        return point.y;
    }

    //获取状态栏高度
    private int getStatusHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object object = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(object);
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            return 0;
        }
    }

}
