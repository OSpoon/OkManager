package com.spoon.okmanager.callback;

public interface IGenericsSerializator {
    <T> T transform(String response, Class<T> classOfT);
}
