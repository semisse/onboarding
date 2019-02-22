package org.nuxeo.onboarding.product.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("product")
public class ProductRest {
    @GET
    public Object doGet() {
        return "Hello World!";
    }
}