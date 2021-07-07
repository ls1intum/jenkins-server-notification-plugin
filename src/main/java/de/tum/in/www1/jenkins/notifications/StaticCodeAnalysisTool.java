package de.tum.in.www1.jenkins.notifications;

import org.apache.commons.io.FilenameUtils;

/**
 * The file names of the results generated by static code analysis tools
 * supported by the static code analysis parser.
 *
 * Unlike in Bamboo, Jenkins does not use artifacts so we hard code the
 * file names of the results that we are interested in.
 *
 * The file names must match the names defined in the Artemis project
 * within the StaticCodeAnalysisTool.java file.
 *
 */
public enum StaticCodeAnalysisTool {

    SPOTBUGS("spotbugsXml.xml"),
    CHECKSTYLE("checkstyle-result.xml"),
    SWIFTLINT("swiftlint-result.xml"),
    PMD("pmd.xml"),
    PMD_CPD("cpd.xml");

    private final String filename;

    StaticCodeAnalysisTool(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return this.filename;
    }

    /**
     * Returns whether the specified filename is a file supported by the
     * static code analysis parser.
     *
     * @param filename the file name of the static code analysis report
     * @return true if the file is supported by the static code analysis parser.
     */
    public static boolean isStaticCodeAnalysisReportsFile(String filename) {
        // The static code analysis parser only supports xml files.
        if (!FilenameUtils.getExtension(filename).equals("xml")) {
            return false;
        }

        for (StaticCodeAnalysisTool staticCodeAnalysisArtifact : StaticCodeAnalysisTool.values()) {
            if (staticCodeAnalysisArtifact.getFilename().equalsIgnoreCase(filename)) {
                return true;
            }
        }

        return false;
    }
}
