package cn.modificator.launcher.jdreadutil;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;

public class DeviceControl {

    private Class<?> classForName;
    private Method getFrontLightValueMethod;
    private Method setFrontLightValueMethod;
    private Method getFrontLightConfigValueMethod;
    private Method setFrontLightConfigValueMethod;
    private int[] lightValueList = new int[]{0,2,3,4,5,7,9,11,13,15,17,19,24,28,32,34,36,37,38,39,40};

    public DeviceControl(){
        classForName = ReflectUtil.classForName("android.onyx.hardware.DeviceController");
        getFrontLightValueMethod = ReflectUtil.getMethodSafely(classForName, "getFrontLightValue", Context.class);
        getFrontLightConfigValueMethod = ReflectUtil.getMethodSafely(classForName, "getFrontLightConfigValue", Context.class);
        setFrontLightValueMethod = ReflectUtil.getMethodSafely(classForName, "setFrontLightValue", Context.class, Integer.TYPE);
        setFrontLightConfigValueMethod = ReflectUtil.getMethodSafely(classForName, "setFrontLightConfigValue", Context.class, Integer.TYPE);;
    }


    public int getFrontLightValue(Context context)
    {
        return getIndex((Integer) invokeMethod(context,getFrontLightValueMethod, context));
    }
    public void setFrontLightValue(Context context,int v)
    {
        if(v>20){ v = 20; }else if(v<0){ v = 0; }
        invokeMethod(context, setFrontLightValueMethod, new Object[]{context, lightValueList[v]});
    }
    public int getFrontLightConfigValue(Context context)
    {
        return  getIndex((Integer) invokeMethod(context,getFrontLightConfigValueMethod, context));
    }
    public void setFrontLightConfigValue(Context context,int v)
    {
        if(v>20){
            v = 20;
        }
        else if(v<0){
            v = 0;
        }
        Log.e(String.valueOf(v),String.valueOf(lightValueList[v]));
        invokeMethod(context, setFrontLightConfigValueMethod, new Object[]{context, lightValueList[v]});
    }


    private Object invokeMethod(Context context, Method method, Object... objArr) {
        if (method == null) {
            return null;
        }
        return ReflectUtil.invokeMethodSafely(method, null, objArr);
    }

    private int getIndex(int value)
    {
        for(int i = 0;i < 21;i++)
        {
            if(lightValueList[i] == value)
            {
                return i;
            }
        }
        return value/2;
    }


}
