/*
 * Copyright (c) 2015 Srinivas Rao. All rights reserved.
 * Creation Date : 22-Jun-2015
 */

package ca.java.spring.cache.web.utilities;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;

public class SpringCacheManager {

    public static void main(String[] args) throws Exception{

        Config config = new Config();
        config.getGroupConfig().setName("dev");
        config.getGroupConfig().setPassword("dev-pass");
        config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getTcpIpConfig().addMember("127.0.0.1:5702");
        config.getNetworkConfig().getJoin().getTcpIpConfig().addMember("127.0.0.1:5703");
        config.getManagementCenterConfig().setEnabled(true);
        config.getManagementCenterConfig().setUrl("http://127.0.0.1:8080/mancenter");
        Hazelcast.newHazelcastInstance(config);
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext-hz.xml");
        IDummyBean dummy = (IDummyBean)context.getBean("dummyBean");

        System.out.println("#######  BEGIN #######");
        System.out.println("####### first call to getName method #######");
        String city = dummy.getCity();
        System.out.println("city:" + city);
        System.out.println("####### second call to getName method  #######");
        city = dummy.getCity();
        System.out.println("city:"+ city);
        System.out.println("#######  END #######");
        Thread.sleep(2);
        //HazelcastClient.shutdownAll();
        //Hazelcast.shutdownAll();

    }
}
