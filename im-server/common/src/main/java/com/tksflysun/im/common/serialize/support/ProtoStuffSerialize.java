package com.tksflysun.im.common.serialize.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.tksflysun.im.common.exception.BusinessException;
import com.tksflysun.im.common.serialize.Iserialize;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public class ProtoStuffSerialize implements Iserialize {
    private static final ThreadLocal<LinkedBuffer> BUFFERLOCAL_LOCAL = new ThreadLocal<LinkedBuffer>() {
        protected LinkedBuffer initialValue() {
            return LinkedBuffer.allocate(1024 * 10);
        };
    };
    private volatile static ProtoStuffSerialize protoStuffSerialize;

    private ProtoStuffSerialize() {

    }

    public static ProtoStuffSerialize getInstance() {
        if (protoStuffSerialize == null) {
            synchronized (ProtoStuffSerialize.class) {
                if (protoStuffSerialize == null) {
                    protoStuffSerialize = new ProtoStuffSerialize();
                }
            }
        }
        return protoStuffSerialize;
    }

    public <T> byte[] serializeT(T obj) {
        if (obj == null) {
            throw new BusinessException("序列化对象(" + obj + ")!");
        }
        @SuppressWarnings("unchecked")
        Schema<T> schema = (Schema<T>)RuntimeSchema.getSchema(obj.getClass());
        LinkedBuffer buffer = BUFFERLOCAL_LOCAL.get();
        byte[] protostuff = null;
        try {
            protostuff = ProtobufIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new BusinessException("序列化(" + obj.getClass() + ")对象(" + obj + ")发生异常!", e.getMessage());
        } finally {
            buffer.clear();
        }
        return protostuff;
    }

    public <T> T deserializeT(byte[] paramArrayOfByte, Class<T> targetClass) {
        if (paramArrayOfByte == null || paramArrayOfByte.length == 0) {
            throw new BusinessException("反序列化对象发生异常,byte序列为空!");
        }
        T instance = null;
        try {
            instance = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BusinessException("反序列化过程中依据类型创建对象失败!", e.getMessage());
        }
        Schema<T> schema = RuntimeSchema.getSchema(targetClass);
        ProtobufIOUtil.mergeFrom(paramArrayOfByte, instance, schema);
        return instance;
    }

    public <T> byte[] serializeList(List<T> objList) {
        if (objList == null || objList.isEmpty()) {
            throw new BusinessException("序列化对象列表(" + objList + ")参数异常!");
        }
        @SuppressWarnings("unchecked")
        Schema<T> schema = (Schema<T>)RuntimeSchema.getSchema(objList.get(0).getClass());
        LinkedBuffer buffer = BUFFERLOCAL_LOCAL.get();
        byte[] protostuff = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            ProtobufIOUtil.writeListTo(bos, objList, schema, buffer);
            protostuff = bos.toByteArray();
        } catch (Exception e) {
            throw new BusinessException("序列化对象列表(" + objList + ")发生异常!", e.getMessage());
        } finally {
            buffer.clear();
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return protostuff;
    }

    public <T> List<T> deserializeList(byte[] paramArrayOfByte, Class<T> targetClass) {
        if (paramArrayOfByte == null || paramArrayOfByte.length == 0) {
            throw new BusinessException("反序列化对象发生异常,byte序列为空!");
        }

        Schema<T> schema = RuntimeSchema.getSchema(targetClass);
        List<T> result = null;
        try {
            result = ProtobufIOUtil.parseListFrom(new ByteArrayInputStream(paramArrayOfByte), schema);
        } catch (IOException e) {
            throw new BusinessException("反序列化对象列表发生异常!", e.getMessage());
        }
        return result;
    }

}
