package de.tum.in.www1.jenkins.notifications.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

@ExportedBean(defaultVisibility = 4)
@XmlRootElement(name = "error")
public class Error {

    @XmlAttribute
    private String message;

    @XmlValue
    private String messageWithStackTrace;

    @XmlAttribute
    private String type;

    @Exported
    public String getMessage() {
        return message;
    }

    @Exported
    public String getMessageWithStackTrace() {
        return messageWithStackTrace;
    }

    @Exported
    public String getType() {
        return type;
    }
}
