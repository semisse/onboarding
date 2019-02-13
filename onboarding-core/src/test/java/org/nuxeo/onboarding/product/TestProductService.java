package org.nuxeo.onboarding.product;

import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(FeaturesRunner.class)
@Features({AutomationFeature.class})
@Deploy({"org.nuxeo.onboarding.product.onboarding-core", "studio.extensions.sfialho-SANDBOX"})
public class TestProductService {

    @Inject
    protected ProductService productservice;

    @Inject
    protected CoreSession coreSession;

    @Test
    public void testService() {
        assertNotNull(productservice);
    }

    @Test
    public void testDocumentCreation() {
        DocumentModel doc = coreSession.createDocumentModel("/", "ProductTest", "product");

        doc = coreSession.createDocument(doc);
        doc.setPropertyValue("dc:title", "some title");
        doc.setPropertyValue("product_schema:price", 10d);
        doc = coreSession.saveDocument(doc);

        IdRef docIdRef = new IdRef(doc.getId());
        doc = coreSession.getDocument(docIdRef);
        Assert.assertNotNull(doc);

        String title = (String) doc.getPropertyValue("dc:title");
        Assert.assertNotNull(title);

        Double price = (Double) doc.getPropertyValue("product_schema:price");
        Assert.assertNotNull(price);

        return;

    }
}
