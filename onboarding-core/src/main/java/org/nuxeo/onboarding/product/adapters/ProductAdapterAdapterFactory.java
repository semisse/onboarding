package org.nuxeo.onboarding.product.adapters;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

public class ProductAdapterAdapterFactory implements DocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> itf) {
        if ("product".equals(doc.getType()) && doc.hasSchema("dublincore")) {
            return new ProductAdapterAdapter(doc);
        } else {
            return null;
        }
    }
}
