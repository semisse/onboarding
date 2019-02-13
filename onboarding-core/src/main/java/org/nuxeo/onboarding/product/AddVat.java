package org.nuxeo.onboarding.product;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.*;

/**
 *
 */
@Operation(id=AddVat.ID, category=Constants.CAT_DOCUMENT, label="add vat to price product", description="Describe here what your operation does.")
public class AddVat {
    public static final String ID = "Document.AddVat";

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

    private DocumentModel getProductAndSetNewPrice(DocumentModel product) {
        Double price = (Double) product.getPropertyValue("product_schema:price");
        if(price == null) {
            product.setPropertyValue("product_schema:price", 1);
        }
        Double newPrice = productService.computePrice(product, null);
        product.setPropertyValue("product_schema:price", newPrice);
        return session.saveDocument(product);
    }
}