package org.nuxeo.onboarding.product;

import org.nuxeo.ecm.core.api.DocumentModel;

public interface ProductService {
    /** Add some methods here. **/

    Double computePrice(DocumentModel doc, Double countryVat);

}
