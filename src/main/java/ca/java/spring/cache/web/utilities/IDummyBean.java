package ca.java.spring.cache.web.utilities;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;


@Component("dummyBean")
public interface IDummyBean {

    @Cacheable("city")
    String getCity();

}
