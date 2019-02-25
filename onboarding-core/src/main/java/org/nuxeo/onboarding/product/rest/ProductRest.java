/*
 * (C) Copyright 2019 Nuxeo (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Samuel Fialho
 */

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

