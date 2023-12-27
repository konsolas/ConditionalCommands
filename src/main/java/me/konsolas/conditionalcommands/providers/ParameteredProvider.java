package me.konsolas.conditionalcommands.providers;

public interface ParameteredProvider<T> {

    T getValue(String param);

}