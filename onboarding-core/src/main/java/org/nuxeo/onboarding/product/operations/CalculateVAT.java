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

package org.nuxeo.onboarding.product.operations;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.onboarding.product.adapters.ProductAdapter;
import org.nuxeo.onboarding.product.services.ProductService;

@Operation(id = CalculateVAT.ID, category = Constants.CAT_DOCUMENT, label = "add vat to price product", description = "Describe here what your operation does.")
public class CalculateVAT {
    public static final String ID = "Document.CalculateVAT";
    static final String DOCUMENT_TYPE_ERROR = "The document provided is not of the type product";

    @Context
    protected ProductService productService;

    @Context
    protected CoreSession session;

    @OperationMethod
    public DocumentModel run(DocumentModel product) {
        return getProductAndSetNewPrice(product);
    }

    @OperationMethod
    public DocumentModelList run(DocumentModelList products) {
        for (DocumentModel product : products) {
            getProductAndSetNewPrice(product);
        }
        return products;
    }

    private DocumentModel getProductAndSetNewPrice(DocumentModel product) throws NuxeoException {
        if (product.getType().equals("product")) {
            ProductAdapter productAdapter = product.getAdapter(ProductAdapter.class);
            Double price = productAdapter.getPrice();
            if (price == null) {
                productAdapter.setPrice(1d);
            }
            Double newPrice = productService.computePrice(product);
            productAdapter.setPrice(newPrice);
            return session.saveDocument(product);
        } else {
            throw new NuxeoException(DOCUMENT_TYPE_ERROR);
        }
    }
}