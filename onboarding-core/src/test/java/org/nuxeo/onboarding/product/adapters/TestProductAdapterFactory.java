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
public class TestProductAdapterFactory {
    @Inject
    private CoreSession session;

    @Test
    public void shouldNotCallTheAdapterIfNotProductOrVisual() {
        DocumentModel doc = session.createDocumentModel("/", "test-adapter", "File");
        VisualAdapter visualAdapter = doc.getAdapter(VisualAdapter.class);
        Assert.assertNull(visualAdapter);
    }
}