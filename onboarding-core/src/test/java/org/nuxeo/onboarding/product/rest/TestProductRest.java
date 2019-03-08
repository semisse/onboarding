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
    /*
    @Inject
    CoreSession session;

    @Before
    public void setup() {
        DocumentModel domain = session.createDocumentModel("/", "testDomain", "Domain");
        session.createDocument(domain);
    }

    @Test
    public void shouldReturnTheProductPrice() {
        DocumentModel doc = session.createDocumentModel("/", "testDoc", "product");
        doc = session.createDocument(doc);
        ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
        productAdapter.save();


        CloseableClientResponse response = getResponse(RequestType.GET, "/");
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
    */
}
