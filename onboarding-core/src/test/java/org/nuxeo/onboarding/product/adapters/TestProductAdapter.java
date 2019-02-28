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

package org.nuxeo.onboarding.product.adapters;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.onboarding.product.OnboardingTestFeature;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features({OnboardingTestFeature.class})
public class TestProductAdapter {
    @Inject
    protected CoreSession session;

    @Test
    public void shouldCallTheProductAdapterSetAndGetProperties() {
        String doctype = "product";
        String title = "My Adapter Title";
        String description = "This is a test description";
        Double price = 10d;

        DocumentModel doc = session.createDocumentModel("/", "test-adapter", doctype);
        ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
        productAdapter.setTitle(title);
        productAdapter.setDescription(description);
        productAdapter.setPrice(price);
        productAdapter.setDistributor("Test Distributor", "Test Location");
        Assert.assertEquals("Document title does not match when using the adapter", title, productAdapter.getTitle());
        Assert.assertEquals(title, productAdapter.getTitle());
        Assert.assertEquals(description, productAdapter.getDescription());
        Assert.assertEquals(description, productAdapter.getDescription());
        Assert.assertEquals(price, productAdapter.getPrice());
        Assert.assertEquals("Test Distributor", productAdapter.getDistributorName());
        Assert.assertEquals("Test Location", productAdapter.getDistributorLocation());
    }
}
