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

package org.nuxeo.onboarding.product.services;

import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.onboarding.product.adapters.ProductAdapter;
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
    protected CoreSession session;

    @Test
    public void testService() {
        assertNotNull(productservice);
    }

    @Test
    public void testDocumentCreation() {
        DocumentModel doc = session.createDocumentModel("/", "ProductTest", "product");
        ProductAdapter product = doc.getAdapter(ProductAdapter.class);
        product.setDocumentTitle("Test Product");
        product.setDocumentPrice(10d);
        doc = session.createDocument(doc);
        doc = session.saveDocument(doc);

        IdRef docIdRef = new IdRef(doc.getId());
        doc = session.getDocument(docIdRef);
        Assert.assertNotNull(doc);

        String title = (String) doc.getPropertyValue("dc:title");
        Assert.assertNotNull(title);

        Double price = (Double) doc.getPropertyValue("product_schema:price");
        Assert.assertNotNull(price);
    }

    @Test
    public void testContribution() throws OperationException {
        DocumentModel doc = session.createDocumentModel("/", "ProductTest", "product");
        ProductAdapter product = doc.getAdapter(ProductAdapter.class);
        doc = session.createDocument(doc);
        product.setDocumentTitle("Test Product");
        product.setDocumentPrice(10d);

        product.setDistributor("Some Store", "PT");

        doc = session.saveDocument(doc);
        IdRef docIdRef = new IdRef(doc.getId());
        doc = session.getDocument(docIdRef);
        assertNotNull(doc);

        product.getDistributorName();
        product.getDistributorLocation();

        Assert.assertEquals(product.getDistributorName(), "Some Store");
        Assert.assertEquals(product.getDistributorLocation(), "PT");
    }

}
