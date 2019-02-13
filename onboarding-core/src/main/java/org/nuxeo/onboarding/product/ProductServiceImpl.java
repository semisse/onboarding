package org.nuxeo.onboarding.product;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

import java.util.HashMap;
import java.util.Map;

public class ProductServiceImpl extends DefaultComponent implements ProductService {

    protected Map<String, VatValueDescriptor> countriesVat = new HashMap<>();

    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        VatValueDescriptor descriptor = (VatValueDescriptor) contribution;
    }

    @Override
    public void unregisterContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
    }

    @Override
    public Double computePrice(DocumentModel doc, Double countryVat) {
        if (countryVat == null) {
            countryVat = 1.23d;
        }

        Double price = (Double) doc.getPropertyValue("product_schema:price");
        return price * countryVat;
    }

}
