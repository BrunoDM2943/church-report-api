package com.bitshammer.infra;

import com.bitshammer.SparkMain;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class WeldConfiguration {

    private static Weld weld;
    private static WeldContainer container;

    public static void init(){
        weld = new Weld()
                .disableDiscovery()
                .addPackages(true, SparkMain.class.getPackage())
                .property("org.jboss.weld.construction.relaxed", true);
        container = weld.initialize();
    }

    public static <T> T getBean(Class<T> clazz){
        return container.select(clazz).get();
    }
}
