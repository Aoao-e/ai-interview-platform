package com._306.aijob.utils;

import com._306.aijob.pojo.requirements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootConfiguration
public class redisUtils {
    @Autowired
    private StringRedisTemplate StringRedisTemplate;
    Map<String,String> tone_String = Map.of(
            "1","专业的",
            "2","学术的",
            "3","对话式"
    );
    public void set(String key, String value) {
        StringRedisTemplate.opsForValue().set(key, value, 3, TimeUnit.MINUTES);
    }

    public String get(String key) {
        return StringRedisTemplate.opsForValue().get(key);
    }
    public void delete(String key) {
        StringRedisTemplate.delete(key);
    }
    public void push_task(String serviceId, requirements q){
        // 存任务数据

        StringRedisTemplate.opsForHash().put("service:"+serviceId,"job",q.getJob());
        StringRedisTemplate.opsForHash().put("service:"+serviceId,"tone",tone_String.get(q.getTone()));
        StringRedisTemplate.opsForHash().put("service:"+serviceId,"count",q.getCount().toString());
        StringRedisTemplate.opsForHash().put("service:"+serviceId,"extrainfo",q.getExtraInfo());
        StringRedisTemplate.opsForHash().put("service:"+serviceId,"question_answer","");
        StringRedisTemplate.opsForHash().put("service:"+serviceId,"is_deal","false");
        // 放入队列
        StringRedisTemplate.opsForList().leftPush("service_queue",serviceId);
    }
    //读取redis，已完成就将问题与答案返回
    public String gettask(String serviceId) throws InterruptedException {
        String redis_key="service:"+serviceId;
        System.out.println(redis_key);
        while(true){
            String status = (String) StringRedisTemplate
                .opsForHash()
                .get(redis_key,"is_deal");
            System.out.println(status);
            if("true".equals(status)){

                String res=(String) StringRedisTemplate
                    .opsForHash()
                    .get(redis_key,"question_answer");
                StringRedisTemplate.delete(redis_key);
                return res;

            }
            Thread.sleep(2000);




    }

    }



}
