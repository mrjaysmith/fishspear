package com.fisher.fishspear.common.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis操作
 */
@Service
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    /**
     * 获取项目基础配置
     * @param key
     * @return
     */
    public String getBaseSetting(final String key) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return (String) hash.get("setting", key);
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean setJSONObject(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, JSON.toJSONString(value));
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取keys
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern + "*");
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern + "*");
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 读取布尔类型缓存
     *
     * @param key
     * @return
     */
    public Boolean getBoolean(final String key) {
        Object obj = get(key);
        return obj == null ? null : Boolean.valueOf(obj.toString());
    }

    /**
     * 读取整数类型缓存
     *
     * @param key
     * @return
     */
    public Integer getInteger(final String key) {
        Object obj = get(key);
        return obj == null ? null : Integer.valueOf(obj.toString());
    }

    /**
     * 读取整数类型缓存
     *
     * @param key
     * @return
     */
    public Long getLong(final String key) {
        Object obj = get(key);
        return obj == null ? null : Long.valueOf(obj.toString());
    }

    /**
     * 读取小数类型缓存
     *
     * @param key
     * @return
     */
    public Double getDouble(final String key) {
        Object obj = get(key);
        return obj == null ? null : Double.valueOf(obj.toString());
    }

    /**
     * 读取String缓存
     *
     * @param key
     * @return
     */
    public String getString(final String key) {
        Object obj = stringRedisTemplate.opsForValue().get(key);
        return obj == null ? null : obj.toString();
    }

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKey
     * @param value
     */
    public void hmSet(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    /**
     * 获取hash中所有键值对
     * @param key
     * @return
     */
    public Map<Object, Object> hmGetAll(String key){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        Map<Object, Object> maps = hash.entries(key);
        return maps;
    }


    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    public Object hmGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }

    /**
     * 列表添加
     *
     * @param k
     * @param v
     */
    public void lPush(String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k, v);
    }

    /**
     * 列表获取
     *
     * @param k
     * @param l
     * @param l1
     * @return
     */
    public List<Object> lRange(String k, long l, long l1) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k, l, l1);
    }

    /**
     * 列表获取
     *
     * @param k
     * @return
     */
    public List<Object> lRangeAll(String k) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        Long size = list.size(k);
        return list.range(k, 0, size == null ? 0 : size - 1);
    }

    /**
     * 集合添加
     *
     * @param key
     * @param value
     */
    public void add(String key, Object value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key, value);
    }


    /**
     * 集合获取
     *
     * @param key
     * @return
     */
    public Set<Object> setMembers(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 集合移除
     */
    public void remove(String key, Object... values) {
        SetOperations<String, Object> oper = redisTemplate.opsForSet();
        int value = oper.remove(key, (Object[]) values).intValue();
    }

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    public void zAdd(String key, Object value, double scoure) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, scoure);
    }

    /**
     * 有序集合获取
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }

}
