/*
 * Copyright (c) 2015 Srinivas Rao. All rights reserved.
 * Creation Date : 22-Jun-2015
 */

package ca.java.spring.cache.web.utilities;

import java.util.concurrent.BlockingQueue;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

/**
 * @author Srinivas Rao
 *
 */
public class SpringCacheClient {
    public static void main(String[] args) throws Exception  {
	ClientConfig config = new ClientConfig();
        config.getGroupConfig().setName("dev");
        config.getGroupConfig().setPassword("dev-pass");
        config.getNetworkConfig().addAddress("127.0.0.1:5701");
        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext-hz.xml");

        System.out.println("#######  CLIENT BEGIN #######" + client);
        IMap map = client.getMap("map");
        map.put("city", "Istanbul");
        System.out.println("City: " + map.get("city"));
        System.out.println("#######  CLIENT END #######");

        BlockingQueue<String> queue = client.getQueue("queue");
        queue.put("Hello!");
        System.out.println("Message send by client!");
        System.exit(0);
    }

}
