package cn.modificator.launcher.jdreadutil;

import android.content.Context;

import java.lang.reflect.Method;

public class DeviceControl {

    private Class<?> classForName;
    private Method getFrontLightValueMethod;
    private Method setFrontLightValueMethod;
    private Method getFrontLightConfigValueMethod;
    private Method setFrontLightConfigValueMethod;

    public DeviceControl(){
        classForName = ReflectUtil.classForName("android.onyx.hardware.DeviceController");
        getFrontLightValueMethod = ReflectUtil.getMethodSafely(classForName, "getFrontLightValue", Context.class);
        getFrontLightConfigValueMethod = ReflectUtil.getMethodSafely(classForName, "getFrontLightConfigValue", Context.class);
        setFrontLightValueMethod = ReflectUtil.getMethodSafely(classForName, "setFrontLightValue", Context.class, Integer.TYPE);
        setFrontLightConfigValueMethod = ReflectUtil.getMethodSafely(classForName, "setFrontLightConfigValue", Context.class, Integer.TYPE);;
    }


    public int getFrontLightValue(Context context)
    {
        return  (Integer) invokeMethod(context,getFrontLightValueMethod, context);
    }
    public void setFrontLightValue(Context context,int v)
    {
        invokeMethod(context, setFrontLightValueMethod, new Object[]{context, v});
    }
    public int getFrontLightConfigValue(Context context)
    {
        return  (Integer) invokeMethod(context,getFrontLightConfigValueMethod, context);
    }
    public void setFrontLightConfigValue(Context context,int v)
    {
        invokeMethod(context, setFrontLightConfigValueMethod, new Object[]{context, v});
    }


    private Object invokeMethod(Context context, Method method, Object... objArr) {
        if (method == null) {
            return null;
        }
        return ReflectUtil.invokeMethodSafely(method, null, objArr);
    }


}
