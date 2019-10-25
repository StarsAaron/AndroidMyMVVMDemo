package com.aaron.mvvmlibrary.binding.command;

/**
 * A one-argument action.
 */
public interface BindingConsumer<T> {
    void call(T t);
}
