package de.tum.in.www1.jenkins.notifications.model;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * Based on <a href="https://github.com/jenkinsci/performance-signature-dynatrace-plugin/pull/81/files">an issue of
 * another Jenkins Plugin</a> I've created this object factory to ensure that JAXB works in JDK11.
 *
 * @author Dominik Fuchss
 *
 */
@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {
        // NOP
    }

    public Commit createCommit() {
        return new Commit();
    }

    public CustomFeedback createCustomFeedback() {
        return new CustomFeedback();
    }

    public Error createError() {
        return new Error();
    }

    public Failure createFailure() {
        return new Failure();
    }

    public SuccessInfo createSuccessInfo() {
        return new SuccessInfo();
    }

    public TestCase createTestCase() {
        return new TestCase();
    }

    public TestResults createTestResults() {
        return new TestResults();
    }

    public Testsuite createTestsuite() {
        return new Testsuite();
    }

}
