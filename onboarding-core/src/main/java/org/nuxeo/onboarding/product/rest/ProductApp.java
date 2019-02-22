package org.nuxeo.onboarding.product.rest;

import org.nuxeo.ecm.webengine.app.WebEngineModule;

import java.util.HashSet;
import java.util.Set;

public class ProductApp extends WebEngineModule {
    @Override
    public Set<Class<?>> getClasses() {
        HashSet<Class<?>> result = new HashSet<Class<?>>();
        result.add(ProductRest.class);
        return result;
    }
}
