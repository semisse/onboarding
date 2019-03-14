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

package org.nuxeo.onboarding.product.listeners;

import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.collections.api.CollectionManager;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.core.event.impl.EventListenerDescriptor;
import org.nuxeo.onboarding.product.OnboardingTestFeature;
import org.nuxeo.onboarding.product.adapters.ProductAdapter;
import org.nuxeo.onboarding.product.adapters.VisualAdapter;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import static org.junit.Assert.assertNotNull;
import static org.nuxeo.onboarding.product.utils.DummyData.*;

@RunWith(FeaturesRunner.class)
@Features({OnboardingTestFeature.class})
public class TestProductListener {
    protected Double price = 10d;
    protected static final String DOCUMENT_TYPE_PRODUCT = "product";
    protected static final String DOCUMENT_TYPE_VISUAL = "visual";
    protected static final String READ = "Read";
    protected final static String DC_TITLE = "dc:title";
    protected final static String USER_GROUP = "group1";

    @Inject
    protected EventService s;

    @Inject
    protected CoreSession session;

    @Inject
    protected CollectionManager collectionManager;

    @Test
    public void listenerRegistration() {
        EventListenerDescriptor listener = s.getEventListener("productlistener");
        assertNotNull(listener);
    }

    @Test
    public void shouldChangeTheTitleToSoldOutAndMoveToFolder() {
        //Create Product
        DocumentModel doc = session.createDocumentModel(WORKSPACE_ROOT, DOCUMENT_NAME_PRODUCT, DOCUMENT_TYPE_PRODUCT);
        ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
        productAdapter.setTitle(DOCUMENT_TITLE);
        productAdapter.setPrice(price);
        productAdapter.setAvailability(false);
        productAdapter.save();

        //Create Visual
        DocumentModel visual = session.createDocumentModel(WORKSPACE_ROOT, DOCUMENT_NAME_VISUAL, DOCUMENT_TYPE_VISUAL);
        VisualAdapter visualAdapter = visual.getAdapter(VisualAdapter.class);
        visual.setPropertyValue(DC_TITLE, DOCUMENT_TITLE);
        visualAdapter.save();

        //Add visual to collection
        collectionManager.addToCollection(productAdapter.getDoc(), visualAdapter.getDoc(), session);

        //Set availability of the product to false
        productAdapter.setAvailability(false);
        productAdapter.save();

        //Fire the event
        EventProducer eventProducer = Framework.getService(EventProducer.class);
        DocumentEventContext ctx = new DocumentEventContext(session, session.getPrincipal(), productAdapter.getDoc());
        Event event = ctx.newEvent("soldoutEvent");
        eventProducer.fireEvent(event);

        //Check permissions
        DocumentModel folder = session.getParentDocument(visualAdapter.getRef());
        ACE[] permissions = folder.getACP().getACL("soldout").getACEs();
        Assert.assertEquals(USER_GROUP, permissions[0].getUsername());
        Assert.assertEquals(READ, permissions[0].getPermission());
        Assert.assertTrue(permissions[0].isGranted());

        //Check if title was changed
        Assert.assertEquals(SOLD_OUT_TITLE, productAdapter.getTitle());

        // Check if it was moved
        DocumentModel newLocation = session.getDocument(visualAdapter.getRef());
        Assert.assertEquals(SOLD_OUT_NEW_PATH, newLocation.getPathAsString());
    }

    @Test(expected = NuxeoException.class)
    public void shouldThrowNuxeoExceptionIfDocumentIsNotProduct() {
        DocumentModel doc = session.createDocumentModel(WORKSPACE_ROOT, DOCUMENT_NAME_VISUAL, DOCUMENT_TYPE_VISUAL);
        EventProducer eventProducer = Framework.getService(EventProducer.class);
        DocumentEventContext ctx = new DocumentEventContext(session, session.getPrincipal(), doc);
        Event event = ctx.newEvent("soldoutEvent");
        eventProducer.fireEvent(event);
    }
}
