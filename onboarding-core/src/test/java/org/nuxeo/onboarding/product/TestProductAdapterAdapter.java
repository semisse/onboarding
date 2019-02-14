package org.nuxeo.onboarding.product;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.onboarding.product.ProductAdapterAdapter;

@RunWith(FeaturesRunner.class)
@Features(CoreFeature.class)
@Deploy({"org.nuxeo.onboarding.product.onboarding-core", "studio.extensions.sfialho-SANDBOX"})
public class TestProductAdapterAdapter {
  @Inject
  CoreSession session;

  @Test
  public void shouldCallTheAdapter() {
    String doctype = "product";
    String testTitle = "My Adapter Title";

    DocumentModel doc = session.createDocumentModel("/", "test-adapter", doctype);
    ProductAdapterAdapter adapter = doc.getAdapter(ProductAdapterAdapter.class);
    adapter.setTitle(testTitle);
    Assert.assertEquals("Document title does not match when using the adapter", testTitle, adapter.getTitle());
  }
}
