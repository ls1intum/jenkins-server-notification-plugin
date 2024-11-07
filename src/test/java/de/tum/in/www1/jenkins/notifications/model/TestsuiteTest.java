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

        List<String> actualTestCaseNames = testSuite.getTestCases().stream().map(TestCase::getName).toList();

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
        Testsuites suites = loadTestSuite(Paths.get("nested_with_failures_top_level_testsuites.xml"));
        assertEquals(2, suites.getTestSuites().size());

        assertEquals("Properties", suites.getTestSuites().get(0).getName());
        assertEquals("Unit Tests", suites.getTestSuites().get(1).getName());
    }

    @Test
    void testNestedTestsuiteEmptyNames() throws Exception {
        Testsuites suites = loadTestSuite(Paths.get("nested_empty_names.xml"));
        assertEquals(1, suites.getTestSuites().size());

        Testsuite flattened = suites.getTestSuites().get(0).flatten();
        assertEquals(3, flattened.getTests());
        assertIterableEquals(
                List.of("Test1", "Test2", "Test3"),
                flattened.getTestCases().stream().map(TestCase::getName).toList()
        );
    }

    @Test
    void testMultipleTestSuites() throws Exception {
        Testsuites suites = loadTestSuite(Paths.get("multiple_testsuites.xml"));
        assertEquals(2, suites.getTestSuites().size());

        List<String> actualTestNames = getTestNames(suites);
        List<String> expectedTestNames = List.of(
                "SuiteA.Test1", "SuiteA.Test2", "SuiteA.Test3",
                "SuiteB.Test1", "SuiteB.Test2", "SuiteB.Test3"
        );

        assertSameElementsAnyOrder(expectedTestNames, actualTestNames);
    }

    @Test
    void testMultipleTestSuitesNested() throws Exception {
        Testsuites suites = loadTestSuite(Paths.get("multiple_testsuites_nested.xml"));
        assertEquals(2, suites.getTestSuites().size());

        List<String> actualTestNames = getTestNames(suites);
        List<String> expectedTestNames = List.of(
                "SuiteA.Test nested once",
                "SuiteA.SuiteC.Test1", "SuiteA.SuiteC.Test2", "SuiteA.SuiteC.Test3",
                "SuiteB.SuiteD.Test1", "SuiteB.SuiteD.Test2", "SuiteB.SuiteD.Test3"
        );

        assertSameElementsAnyOrder(expectedTestNames, actualTestNames);
    }

    @Test
    void testCaseNamesSameWhenSingleTestsuiteWrapped() throws Exception {
        Testsuites suitesWrapped = loadTestSuite(Paths.get("nested_successful_wrapped_testsuites.xml"));
        Testsuites suitesNonWrapped = loadTestSuite(Paths.get("nested_successful.xml"));

        List<String> wrappedNames = getTestNames(suitesWrapped);
        List<String> nonWrappedNames = getTestNames(suitesNonWrapped);

        assertIterableEquals(wrappedNames, nonWrappedNames);
    }

    private Testsuites loadTestSuite(final Path reportXml) throws IOException {
        Path resourcePath = new File("testsuite_examples").toPath().resolve(reportXml);
        URL resource = getClass().getClassLoader().getResource(resourcePath.toString());
        assertNotNull(resource);

        return junitXmlParser.parseJunitTestReport(resource.openStream());
    }

    private Testsuite loadSingleTestSuite(final Path reportXml) throws IOException {
        final Testsuites testsuites = loadTestSuite(reportXml);
        assertEquals(1, testsuites.getTestSuites().size());
        return testsuites.getTestSuites().get(0);
    }

    private List<String> getTestNames(final Testsuites suites) {
        return suites.flattened().flatMap(suite -> suite.getTestCases().stream()).map(TestCase::getName).toList();
    }

    private static <T> void assertSameElementsAnyOrder(final List<T> expected, final List<T> actual) {
        assertAll(
                () -> assertEquals(expected.size(), actual.size()),
                () -> assertTrue(expected.containsAll(actual), () -> String.format("Expected: %s%nActual: %s", expected, actual)),
                () -> assertTrue(actual.containsAll(expected))
        );
    }
}
