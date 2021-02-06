package cn.modificator.launcher.jdreadutil;

import android.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ReflectUtil {
    private static final String a = "ReflectUtil";
    private static Object b = new Object();

    public static boolean getConstructorSafely(RefValue<Constructor<?>> refValue, Class<?> cls, Class<?>... clsArr) {
        if (cls == null) {
            return false;
        }
        try {
            refValue.setValue(cls.getConstructor(clsArr));
            return true;
        } catch (SecurityException e) {
            Log.w(a, e);
            return false;
        } catch (NoSuchMethodException e2) {
            Log.w(a, e2);
            return false;
        }
    }

    public static Constructor<?> getConstructorSafely(Class<?> cls, Class<?>... clsArr) {
        RefValue refValue = new RefValue();
        if (getConstructorSafely(refValue, cls, clsArr)) {
            return (Constructor) refValue.getValue();
        }
        return null;
    }

    public static Class<?> classForName(String str) {
        try {
            return Class.forName(str);
        } catch (Exception e) {
            Log.e(a, "", e);
            return null;
        }
    }

    public static boolean getMethodSafely(RefValue<Method> refValue, Class<?> cls, String str, Class<?>... clsArr) {
        if (cls == null) {
            return false;
        }
        try {
            refValue.setValue(cls.getMethod(str, clsArr));
            return true;
        } catch (NoSuchMethodException | SecurityException unused) {
            return false;
        }
    }

    public static Method getMethodSafely(Class<?> cls, String str, Class<?>... clsArr) {
        RefValue refValue = new RefValue();
        if (getMethodSafely(refValue, cls, str, clsArr)) {
            return (Method) refValue.getValue();
        }
        return null;
    }

    public static boolean getStaticIntFieldSafely(RefValue<Integer> refValue, Class<?> cls, String str) {
        if (cls == null) {
            return false;
        }
        try {
            refValue.setValue(Integer.valueOf(cls.getField(str).getInt(null)));
            return true;
        } catch (IllegalArgumentException e) {
            Log.w(a, e);
            return false;
        } catch (SecurityException e2) {
            Log.w(a, e2);
            return false;
        } catch (IllegalAccessException e3) {
            Log.w(a, e3);
            return false;
        } catch (NoSuchFieldException e4) {
            Log.w(a, e4);
            return false;
        }
    }

    public static int getStaticIntFieldSafely(Class<?> cls, String str) {
        RefValue refValue = new RefValue();
        if (getStaticIntFieldSafely(refValue, cls, str)) {
            return ((Integer) refValue.getValue()).intValue();
        }
        return 0;
    }

    public static boolean getStaticFieldSafely(RefValue<Object> refValue, Class<?> cls, String str) {
        if (cls == null) {
            return false;
        }
        try {
            refValue.setValue(cls.getField(str).get(null));
            return true;
        } catch (IllegalArgumentException e) {
            Log.w(a, e);
            return false;
        } catch (SecurityException e2) {
            Log.w(a, e2);
            return false;
        } catch (IllegalAccessException e3) {
            Log.w(a, e3);
            return false;
        } catch (NoSuchFieldException e4) {
            Log.w(a, e4);
            return false;
        }
    }

    public static Object getStaticFieldSafely(Class<?> cls, String str) {
        RefValue refValue = new RefValue();
        if (getStaticFieldSafely(refValue, cls, str)) {
            return refValue.getValue();
        }
        return null;
    }

    public static boolean constructObjectSafely(RefValue<Object> refValue, Constructor<?> constructor, Object... objArr) {
        if (constructor == null) {
            return false;
        }
        try {
            refValue.setValue(constructor.newInstance(objArr));
            return true;
        } catch (Throwable th) {
            Log.w(a, "", th);
            return false;
        }
    }

    public static Object newInstance(Constructor<?> constructor, Object... objArr) {
        RefValue refValue = new RefValue();
        if (constructObjectSafely(refValue, constructor, objArr)) {
            return refValue.getValue();
        }
        return null;
    }

    public static boolean invokeMethodSafely(RefValue<Object> refValue, Method method, Object obj, Object... objArr) {
        if (method == null) {
            return false;
        }
        try {
            refValue.setValue(method.invoke(obj, objArr));
            return true;
        } catch (Throwable th) {
            Log.w(a, th);
            return false;
        }
    }

    public static Object invokeMethodSafely(Method method, Object obj, Object... objArr) {
        RefValue refValue = new RefValue();
        if (!invokeMethodSafely(refValue, method, obj, objArr)) {
            return null;
        }
        if (refValue.getValue() != null) {
            return refValue.getValue();
        }
        return b;
    }

    public static Method getDeclaredMethodSafely(Class<?> cls, String str, Class<?>... clsArr) {
        RefValue refValue = new RefValue();
        if (getDeclaredMethod(refValue, cls, str, clsArr)) {
            return (Method) refValue.getValue();
        }
        return null;
    }

    public static boolean getDeclaredMethod(RefValue<Method> refValue, Class<?> cls, String str, Class<?>... clsArr) {
        if (cls == null) {
            return false;
        }
        try {
            Method declaredMethod = cls.getDeclaredMethod(str, clsArr);
            declaredMethod.setAccessible(true);
            refValue.setValue(declaredMethod);
            return true;
        } catch (NoSuchMethodException | SecurityException unused) {
            return false;
        }
    }
}
