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
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.onboarding.product.OnboardingTestFeature;
import org.nuxeo.onboarding.product.adapters.ProductAdapter;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(FeaturesRunner.class)
@Features({OnboardingTestFeature.class})
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
    public void shouldCreateProduct() {
        DocumentModel doc = session.createDocumentModel("/", "ProductTest", "product");
        ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
        productAdapter.setTitle("Test Product");
        productAdapter.setPrice(10d);
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
    public void shouldSetDistributor() {
        DocumentModel doc = session.createDocumentModel("/", "ProductTest", "product");
        ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
        doc = session.createDocument(doc);
        productAdapter.setTitle("Test Product");
        productAdapter.setPrice(10d);

        productAdapter.setDistributor("Some Store", "PT");

        doc = session.saveDocument(doc);
        IdRef docIdRef = new IdRef(doc.getId());
        doc = session.getDocument(docIdRef);
        assertNotNull(doc);

        productAdapter.getDistributorName();
        productAdapter.getDistributorLocation();

        Assert.assertEquals(productAdapter.getDistributorName(), "Some Store");
        Assert.assertEquals(productAdapter.getDistributorLocation(), "PT");
    }

}
