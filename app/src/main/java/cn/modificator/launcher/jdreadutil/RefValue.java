package cn.modificator.launcher.jdreadutil;

public class RefValue<T> {
    private T a = null;

    public RefValue() {
    }

    public RefValue(T t) {
        this.a = t;
    }

    public T getValue() {
        return this.a;
    }

    public void setValue(T t) {
        this.a = t;
    }
}