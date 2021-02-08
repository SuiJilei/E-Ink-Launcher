package cn.modificator.launcher.floatball;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.lang.reflect.Field;

import cn.modificator.launcher.EInkAccessibilityService;
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
    private boolean floatBallViewAdded = false;// 透明窗体是否已经显示
    private boolean viewHide = false; // 窗口隐藏
    private int statusBarHeight;// 状态栏高度
    private Context context;
    private AccessibilityService accessibilityService;

    private boolean isLongClickFlag;
    private WindowManager.LayoutParams floatMenuParams;
    private int clickNum = 0;

    //私有化构造函数
    private FloatViewManager(Context context) {
        this.context = context;
        floatMenu = new FloatMenu(context);
        windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
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
        floatBallInit();
        viewHide = false;
        refresh();
        accessibilityService = EInkAccessibilityService.getInstance();
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
        if (floatBallViewAdded) {
            windowManager.updateViewLayout(floatBallView, layoutParams);
        } else {
            windowManager.addView(floatBallView, layoutParams);
            floatBallViewAdded = true;
        }
    }



    @SuppressLint("ClickableViewAccessibility")
    private void floatBallInit() {
        floatBallView = LayoutInflater.from(context).inflate(R.layout.floating_ball, null);
        floatBallBtn = floatBallView.findViewById(R.id.float_ball_btn);
        /*
         * LayoutParams.TYPE_SYSTEM_ERROR：保证该悬浮窗所有View的最上层
         * LayoutParams.FLAG_NOT_FOCUSABLE:该浮动窗不会获得焦点，但可以获得拖动
         * PixelFormat.TRANSPARENT：悬浮窗透明
         */
        layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
        layoutParams.gravity = Gravity.RIGHT | Gravity.CENTER;


        /**
         * 监听窗体移动事件
         */
        floatBallBtn.setOnTouchListener(new View.OnTouchListener() {
            float[] temp = new float[] { 0f, 0f };
            float downX;
            float downY;

            public boolean onTouch(View v, MotionEvent event) {
                layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN: // 按下事件，记录按下时手指在悬浮窗的XY坐标值
                        isLongClickFlag = true;
                        floatBallBtn.setBackgroundResource(R.drawable.float_ball_bg_press);
                        temp[0] = event.getX();
                        temp[1] = event.getY();
                        downX = event.getRawX();
                        downY = event.getRawY();


                        break;
                    case MotionEvent.ACTION_MOVE:
                        floatBallBtn.setBackgroundResource(R.drawable.float_ball_bg);
                        refreshView((int) (event.getRawX() - temp[0]),
                                (int) (event.getRawY() - temp[1]));
                        if (Math.abs(event.getRawX() - downX) > 5 && Math.abs(event.getRawY() - downY) > 5) {
                            isLongClickFlag = false;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        isLongClickFlag = false;
                        floatBallBtn.setBackgroundResource(R.drawable.float_ball_bg);
                        float upX = event.getRawX();
                        float upY = event.getRawY();

                        if(upX<getScreenWidth()/4)
                        {
                            refreshView((int) (0 - temp[0]),
                                    (int) (event.getRawY() - temp[1]));
                        }
                        else if(upX>getScreenWidth()/4*3)
                        {
                            refreshView((int) (getScreenWidth() - temp[0]),
                                    (int) (event.getRawY() - temp[1]));
                        }

                        if (Math.abs(upX - downX) > 5 && Math.abs(upY - downY) > 5) {
                            return true;
                        }
                        break;
                }
                return false;
            }
        });

        floatBallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                clickNum++;
                //accessibilityService = (AccessibilityService)context;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (clickNum == 1) {
                            if(accessibilityService != null) {
                                AccessibilityUtil.doBack(accessibilityService);
                            }
                            //AccessibilityUtil.doBack(accessibilityService);
                        }else if(clickNum==2){
                            Intent intent = new Intent(context, Launcher.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                            );
                            context.startActivity(intent);
                        }
                        clickNum = 0;
                    }
                },300);


            }
        });


        floatBallBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //匿名内部类实现点击事件
                try {
                    Thread.sleep(200);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                if(isLongClickFlag) {
                    showFloatMenu();
                }
                return true;
            }
        });
    }

    public void showFloatMenu() {
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
        if (floatBallViewAdded) {
            windowManager.removeView(floatBallView);
            floatBallViewAdded = false;
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

    public boolean getFloatBallState()
    {
        return floatBallViewAdded;
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
