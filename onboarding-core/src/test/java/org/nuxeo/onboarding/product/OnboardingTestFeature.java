package org.nuxeo.onboarding.product;

import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.collections.core.test.CollectionFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.RunnerFeature;


@Features({AutomationFeature.class, CollectionFeature.class})
@Deploy({"org.nuxeo.onboarding.product.onboarding-core",
        "studio.extensions.sfialho-SANDBOX"})
public class OnboardingTestFeature implements RunnerFeature {

}
