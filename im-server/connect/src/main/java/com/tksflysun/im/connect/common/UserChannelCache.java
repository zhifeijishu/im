package com.tksflysun.im.connect.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;

/**
 * 用户和通道缓存
 * 
 * @author lpf
 *
 */
public class UserChannelCache {
    // 用户id和channel映射map
    private static Map<String, Channel> userChannelMap = new ConcurrentHashMap<String, Channel>(100000);
    private static Map<Channel, String> channelUserMap = new ConcurrentHashMap<Channel, String>(100000);
    private static final Logger logger = LoggerFactory.getLogger(UserChannelCache.class);

    public static void set(String userId, Channel channel) {
        if (userId == null || channel == null) {
            return;
        }
        userChannelMap.remove(userId);
        channelUserMap.remove(channel);
        userChannelMap.put(userId, channel);
        channelUserMap.put(channel, userId);
    }

    public static Channel getChannel(String userId) {
        return userChannelMap.get(userId);
    }

    public static String getUserId(Channel channel) {
        return channelUserMap.get(channel);
    }

    public static void removeChannel(Channel channel) {
        if (channel == null) {
            return;
        }
        String userId = getUserId(channel);
        logger.warn("关闭用户通道:" + userId);
        if (userId != null) {
            userChannelMap.remove(userId);
        }
        channelUserMap.remove(channel);
    }
}
