package com.tksflysun.im.common.redis;

import java.nio.charset.Charset;
import java.util.List;

import com.tksflysun.im.common.compress.Icompress;
import com.tksflysun.im.common.serialize.Iserialize;

import redis.clients.jedis.JedisCluster;

public abstract class JedisClusterClientWrapper {
    public abstract JedisCluster getJedisCluster();

    public abstract String getKeyPrefix();

    public abstract Icompress getCompress();

    public abstract Iserialize getSerialize();

    public abstract int getTtl();

    public void expire(String key, int seconds) {
        getJedisCluster().expire(getKeyPrefix() + key, seconds);
    }

    public boolean exists(String key) {
        return getJedisCluster().exists(getKeyPrefix() + key);
    }

    public <T> void setT(String key, T t) {
        getJedisCluster().set((getKeyPrefix() + key).getBytes(Charset.forName("UTF-8")),
            getCompress().compress(getSerialize().serializeT(t)));
    }

    public <T> void setexT(String key, T t) {
        getJedisCluster().setex((getKeyPrefix() + key).getBytes(Charset.forName("UTF-8")), getTtl(),
            getCompress().compress(getSerialize().serializeT(t)));
    }

    public <T> T getT(String key, Class<T> clazz) {
        byte[] b = getJedisCluster().get((getKeyPrefix() + key).getBytes(Charset.forName("UTF-8")));
        if (b == null) {
            return null;
        }
        return getSerialize().deserializeT(getCompress().uncompress(b), clazz);
    }

    public <T> void setList(String key, List<T> t) {
        getJedisCluster().set((getKeyPrefix() + key).getBytes(Charset.forName("UTF-8")),
            getCompress().compress(getSerialize().serializeList(t)));
    }

    public <T> void setexList(String key, int seconds, List<T> t) {
        getJedisCluster().setex((getKeyPrefix() + key).getBytes(Charset.forName("UTF-8")), seconds,
            getCompress().compress(getSerialize().serializeList(t)));
    }

    public <T> List<T> getList(String key, Class<T> clazz) {
        byte[] b = getJedisCluster().get((getKeyPrefix() + key).getBytes(Charset.forName("UTF-8")));
        if (b == null) {
            return null;
        }
        return getSerialize().deserializeList(getCompress().uncompress(b), clazz);
    }

}
