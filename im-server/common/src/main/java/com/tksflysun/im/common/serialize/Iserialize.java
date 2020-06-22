package com.tksflysun.im.common.serialize;

import java.util.List;

public interface Iserialize {
    <T> byte[] serializeT(T t);

    <T> byte[] serializeList(List<T> list);

    <T> T deserializeT(byte[] b, Class<T> clazz);

    <T> List<T> deserializeList(byte[] b, Class<T> clazz);

}
