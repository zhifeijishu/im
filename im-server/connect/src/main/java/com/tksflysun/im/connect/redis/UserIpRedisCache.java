package com.tksflysun.im.connect.redis;

import com.tksflysun.im.common.compress.CompressFactory;
import com.tksflysun.im.common.compress.Icompress;
import com.tksflysun.im.common.redis.JedisClusterClientWrapper;
import com.tksflysun.im.common.redis.JedisClusterFactory;
import com.tksflysun.im.common.serialize.Iserialize;
import com.tksflysun.im.common.serialize.SerializeFactory;

import redis.clients.jedis.JedisCluster;

public class UserIpRedisCache extends JedisClusterClientWrapper {
    private static final String PREFIX = "im:login:limit:";
    private volatile static UserIpRedisCache userIpRedisCache;
    private static final String CLUSTER_NAME = "im_small_redis";

    public static UserIpRedisCache getInstance() {
        if (userIpRedisCache == null) {
            synchronized (UserIpRedisCache.class) {
                if (userIpRedisCache == null) {
                    userIpRedisCache = new UserIpRedisCache();
                }
            }
        }
        return userIpRedisCache;
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
        return -1;
    }

}
