package org.nuxeo.onboarding.product.rest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.onboarding.product.OnboardingTestFeature;
import org.nuxeo.onboarding.product.adapters.VisualAdapter;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features({OnboardingTestFeature.class})
public class TestProductRest {
    @Inject
    CoreSession session;

    @Test
    public void shouldCallTheAdapter() {
        DocumentModel doc = session.createDocumentModel("/", "test-adapter", "visual");
        VisualAdapter adapter = doc.getAdapter(VisualAdapter.class);
        adapter.setTitle("My Adapter Title");
        Assert.assertEquals("Document title does not match when using the adapter", "My Adapter Title", adapter.getTitle());
    }
}
