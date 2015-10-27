package io.mangoo.test;

import org.scribe.utils.Preconditions;

import com.google.inject.Injector;

import io.mangoo.core.Application;
import io.mangoo.enums.Key;
import io.mangoo.enums.Mode;

/**
 * 
 * @author svenkubiak
 *
 */
public enum MangooInstance {
    TEST;

    MangooInstance() {
        System.setProperty(Key.APPLICATION_MODE.toString(), Mode.TEST.toString());
        Application.main(null);
    }

    public void start() {
        //intentionally left blank
    }

    public Injector getInjector() {
        return Application.getInjector();
    }
    
    public <T> T getInstance(Class<T> clazz) {
        Preconditions.checkNotNull(clazz, "clazz can not be null");
        return Application.getInjector().getInstance(clazz);
    }
}