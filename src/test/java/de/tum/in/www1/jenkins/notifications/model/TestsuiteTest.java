package de.tum.in.www1.jenkins.notifications.model;

import de.tum.in.www1.jenkins.notifications.JunitXmlParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TestsuiteTest {

    private JunitXmlParser junitXmlParser;

    @BeforeEach
    void setup() throws JAXBException {
        junitXmlParser = new JunitXmlParser();
    }

    @Test
    void testFlattenNestedSuiteSuccessful() throws Exception {
        Testsuite input = loadSingleTestSuite(Paths.get("nested_successful.xml"));
        assertEquals(2, input.getTestSuites().size());

        Testsuite flattened = input.flatten();
        assertNull(flattened.getTestSuites());

        assertEquals(12, flattened.getTests());
        assertEquals(12, flattened.getTestCases().size());
        assertEquals(0, flattened.getErrors());
        assertEquals(0, flattened.getFailures());
    }

    @Test
    void testFlattenBuildTestCaseNames() throws Exception {
        Testsuite testSuite = loadSingleTestSuite(Paths.get("nested_successful.xml")).flatten();

        List<String> expectedTestCaseNames = new ArrayList<>();
        expectedTestCaseNames.add("Properties.Checked by SmallCheck.Testing filtering in A");
        expectedTestCaseNames.add("Testing selectAndReflectA (0,0) []");

        List<String> actualTestCaseNames = testSuite.getTestCases().stream().map(TestCase::getName).collect(Collectors.toList());

        for (String testCaseName : expectedTestCaseNames) {
            Optional<String> testCase = actualTestCaseNames.stream().filter(testCaseName::equals).findFirst();
            assertTrue(testCase.isPresent(), String.format("Did not find test case '%s' in %s", testCaseName, actualTestCaseNames));
        }
    }

    @Test
    void testFlattenNestedSuiteWithFailures() throws Exception {
        Testsuite input = loadSingleTestSuite(Paths.get("nested_with_failures.xml"));
        assertEquals(2, input.getTestSuites().size());

        Testsuite flattened = input.flatten();
        assertNull(flattened.getTestSuites());

        assertEquals(12, flattened.getTests());
        assertEquals(12, flattened.getTestCases().size());
        assertEquals(2, flattened.getFailures());
        assertEquals(1, flattened.getErrors());
    }

    @Test
    void testParseTestSuites() throws Exception {
        List<Testsuite> suites = loadTestSuite(Paths.get("nested_with_failures_top_level_testsuites.xml")).collect(Collectors.toList());
        assertEquals(2, suites.size());

        assertEquals("Properties", suites.get(0).getName());
        assertEquals("Unit Tests", suites.get(1).getName());
    }

    private Stream<Testsuite> loadTestSuite(final Path reportXml) throws IOException {
        Path resourcePath = new File("testsuite_examples").toPath().resolve(reportXml);
        URL resource = getClass().getClassLoader().getResource(resourcePath.toString());
        assertNotNull(resource);

        return junitXmlParser.parseJunitTestReport(resource.openStream());
    }

    private Testsuite loadSingleTestSuite(final Path reportXml) throws IOException {
        final List<Testsuite> testsuites = loadTestSuite(reportXml).collect(Collectors.toList());
        assertEquals(1, testsuites.size());
        return testsuites.get(0);
    }
}
