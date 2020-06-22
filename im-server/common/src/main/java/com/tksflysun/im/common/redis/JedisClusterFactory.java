package com.tksflysun.im.common.redis;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisClusterFactory {

    @Autowired
    ClusterConfig redisClusterConfig;

    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.connectionTimeout}")
    private int connectionTimeout;
    @Value("${spring.redis.soTimeout}")
    private int soTimeout;
    @Value("${spring.redis.maxAttempts}")
    private int maxAttempts;

    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;
    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis;
    private static final Map<String, JedisCluster> map = new ConcurrentHashMap<String, JedisCluster>();
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JedisClusterFactory.class);

    @PostConstruct
    public void init() {
        try {
            Set<HostAndPort> sets = null;
            JedisPoolConfig config = null;
            HostAndPort hostAndPort = null;
            JedisCluster jedisCluster = null;
            for (int i = 0; i < redisClusterConfig.getClusters().size(); i++) {
                sets = new HashSet<>();
                Cluster cluster = redisClusterConfig.getClusters().get(i);
                String[] nodes = cluster.getNodes().split(",");
                for (String s : nodes) {
                    String strings[] = s.split(":");
                    hostAndPort = new HostAndPort(strings[0], Integer.parseInt(strings[1]));
                    sets.add(hostAndPort);
                }
                config = new JedisPoolConfig();
                config.setMaxTotal(maxActive);
                config.setMaxIdle(maxIdle);
                config.setTestOnBorrow(true);
                config.setMaxWaitMillis(maxWaitMillis);
                config.setMinIdle(minIdle);

                if (cluster.getPassword() == null || "".equals(cluster.getPassword())) {

                    jedisCluster = new JedisCluster(sets, connectionTimeout, soTimeout, maxAttempts, config);
                } else {
                    jedisCluster = new JedisCluster(sets, connectionTimeout, soTimeout, maxAttempts,
                        cluster.getPassword(), config);
                }
                map.put(cluster.getName(), jedisCluster);
            }
        } catch (Throwable e) {
            logger.error("redis 初始化失败", e);
        }
    }

    public static JedisCluster getJedisCluster(String key) {
        return map.get(key);
    }
}