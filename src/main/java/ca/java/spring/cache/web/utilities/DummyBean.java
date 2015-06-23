/*
 * Copyright (c) 2015 Srinivas Rao. All rights reserved.
 * Creation Date : 22-Jun-2015
 */

package ca.java.spring.cache.web.utilities;

public class DummyBean implements IDummyBean {

    @Override
    public String getCity() {
        System.out.println("getYearMakeModel called");
        return "2015/KIA/XML";
    }

 }
