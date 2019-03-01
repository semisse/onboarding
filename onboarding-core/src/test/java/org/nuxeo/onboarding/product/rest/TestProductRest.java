package org.nuxeo.onboarding.product.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.restapi.test.BaseTest;
import org.nuxeo.ecm.restapi.test.RestServerFeature;
import org.nuxeo.jaxrs.test.CloseableClientResponse;
import org.nuxeo.onboarding.product.OnboardingTestFeature;
import org.nuxeo.onboarding.product.adapters.ProductAdapter;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

@RunWith(FeaturesRunner.class)
@Features({OnboardingTestFeature.class, RestServerFeature.class})
@Deploy("org.nuxeo.ecm.platform.tag")
@Deploy("org.nuxeo.ecm.platform.url.core")

public class TestProductRest extends BaseTest {
    @Inject
    CoreSession session;

    @Before
    public void setup() {
        DocumentModel domain = session.createDocumentModel("/", "testDomain", "Domain");
        session.createDocument(domain);
    }

    @Test
    public void shoouldReturnTheProductPrice() {
        DocumentModel doc = session.createDocumentModel("/", "testDoc", "product");
        doc = session.createDocument(doc);
        ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
        productAdapter.save();

        String returnedProduct = productAdapter.getId();
        CloseableClientResponse response = getResponse(RequestType.GET, "/site/product/price/" + returnedProduct);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}
