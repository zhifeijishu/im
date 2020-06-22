package com.tksflysun.im.api.redis;

import com.tksflysun.im.common.compress.CompressFactory;
import com.tksflysun.im.common.compress.Icompress;
import com.tksflysun.im.common.redis.JedisClusterClientWrapper;
import com.tksflysun.im.common.redis.JedisClusterFactory;
import com.tksflysun.im.common.serialize.Iserialize;
import com.tksflysun.im.common.serialize.SerializeFactory;
import redis.clients.jedis.JedisCluster;

public class LoginLimitRedisCache extends JedisClusterClientWrapper {
    private static final String PREFIX = "im:login:limit:";
    public static final int TTL = 600;
    public static final int ACCOUNT_LIMIT_COUNT = 5;
    private static final String CLUSTER_NAME = "im_small_redis";
    private volatile static LoginLimitRedisCache loginLimitRedisCache;

    public static LoginLimitRedisCache getInstance() {
        if (loginLimitRedisCache == null) {
            synchronized (LoginLimitRedisCache.class) {
                if (loginLimitRedisCache == null) {
                    loginLimitRedisCache = new LoginLimitRedisCache();
                }
            }
        }
        return loginLimitRedisCache;
    }

    @Override
    public JedisCluster getJedisCluster() {
        return JedisClusterFactory.getJedisCluster(CLUSTER_NAME);
    }

    @Override
    public String getKeyPrefix() {
        return PREFIX;
    }

    @Override
    public Icompress getCompress() {
        return CompressFactory.getDefaultCompress();
    }

    @Override
    public Iserialize getSerialize() {
        return SerializeFactory.getDefaultSerialize();
    }

    @Override
    public int getTtl() {
        return TTL;
    }

}
