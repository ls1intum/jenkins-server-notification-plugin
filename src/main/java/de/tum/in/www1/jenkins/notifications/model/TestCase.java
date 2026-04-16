package de.tum.in.www1.jenkins.notifications.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

@ExportedBean(defaultVisibility = 3)
@XmlRootElement(name = "testcase")
public class TestCase {

    private String name;

    @XmlAttribute
    private String classname;

    @XmlAttribute
    private double time;

    private List<Failure> failures;

    private List<Error> errors;

    private List<SuccessInfo> successInfos;

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exported
    public String getClassname() {
        return classname;
    }

    @Exported
    public double getTime() {
        return time;
    }

    @Exported
    public List<Failure> getFailures() {
        return failures;
    }

    @XmlElement(name = "failure")
    public void setFailures(List<Failure> failures) {
        this.failures = failures;
    }

    @Exported
    public List<Error> getErrors() {
        return errors;
    }

    @XmlElement(name = "error")
    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    @Exported
    public List<SuccessInfo> getSuccessInfos() {
        return successInfos;
    }

    @XmlElement(name = "successInfo")
    public void setSuccessInfos(List<SuccessInfo> successInfos) {
        this.successInfos = successInfos;
    }
}
