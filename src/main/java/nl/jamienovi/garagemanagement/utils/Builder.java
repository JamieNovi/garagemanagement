package nl.jamienovi.garagemanagement.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

/**
 * Generic object builder.
 * @param <T>
 */
public class Builder<T> {
    private T instance;

    public Builder(Class<T> tClass){
        try{
            instance = tClass.getDeclaredConstructor().newInstance();
        }catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Builder<T> with(Consumer<T> setter) {
        setter.accept(instance);
        return this;
    }

    public T get(){
        return instance;
    }

    public static <T> Builder<T> build(Class<T> tClass) {
        return new Builder<>(tClass);
    }
}
