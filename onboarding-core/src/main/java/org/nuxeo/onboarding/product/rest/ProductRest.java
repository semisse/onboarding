package org.nuxeo.onboarding.product.rest;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;
import org.nuxeo.onboarding.product.adapters.ProductAdapter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@WebObject(type = "product")
@Produces("application/json")
@Path("product")
public class ProductRest extends ModuleRoot {
    @GET
    @Path("price/{productId}")
    public String getProductAndReturnPrice(@PathParam("productId") String productId) {
        return computePrice(new IdRef(productId));
    }

    private String computePrice(DocumentRef documentRef) {
        DocumentModel product = getContext().getCoreSession().getDocument(documentRef);
        ProductAdapter productAdapter = product.getAdapter(ProductAdapter.class);
        Double price = productAdapter.getPrice();
        return Double.toString(price);
    }
}

