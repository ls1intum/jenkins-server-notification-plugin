package de.tum.in.www1.jenkins.notifications;

import com.sun.xml.bind.v2.ContextFactory;
import de.tum.in.www1.jenkins.notifications.exception.TestParsingException;
import de.tum.in.www1.jenkins.notifications.model.ObjectFactory;
import de.tum.in.www1.jenkins.notifications.model.Testsuite;
import de.tum.in.www1.jenkins.notifications.model.Testsuites;
import java.io.InputStream;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class JunitXmlParser {

    private final JAXBContext context;

    public JunitXmlParser() throws JAXBException {
        this.context = createJAXBContext();
    }

    public Testsuites parseJunitTestReport(final InputStream report) throws TestParsingException {
        try {
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            final Object parsedTestResults = unmarshaller.unmarshal(report);

            /*
             * JUnit XMLs can either have a top-level `<testsuites>` element that contains multiple `<testsuite>`s, or
             * alternatively, if there is only one test suite, the `<testsuite>` is allowed to appear as the top-most
             * element in the XML.
             */
            if (parsedTestResults instanceof Testsuites testSuites) {
                return testSuites;
            } else if (parsedTestResults instanceof Testsuite testSuite) {
                final Testsuites suites = new Testsuites();
                suites.setTestSuites(List.of(testSuite));
                return suites;
            } else {
                throw new IllegalStateException("Cannot parse " + report + "! Unknown JUnit XML format.");
            }
        } catch (JAXBException | IllegalStateException e) {
            throw new TestParsingException(e);
        }
    }

    private JAXBContext createJAXBContext() throws JAXBException {
        return ContextFactory.createContext(
                ObjectFactory.class.getPackage().getName(), ObjectFactory.class.getClassLoader(), null);
    }
}
