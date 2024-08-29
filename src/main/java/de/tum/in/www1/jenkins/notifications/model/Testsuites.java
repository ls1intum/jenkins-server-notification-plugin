package de.tum.in.www1.jenkins.notifications.model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

@ExportedBean(defaultVisibility = 2)
@XmlRootElement(name = "testsuites")
public class Testsuites {

    private List<Testsuite> testSuites;

    @Exported
    public List<Testsuite> getTestSuites() {
        return testSuites;
    }

    @XmlElement(name = "testsuite")
    public void setTestSuites(List<Testsuite> testSuites) {
        this.testSuites = testSuites;
    }
}
