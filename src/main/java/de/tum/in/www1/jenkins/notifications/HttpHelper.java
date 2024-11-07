package de.tum.in.www1.jenkins.notifications;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.google.gson.Gson;

import de.tum.in.www1.jenkins.notifications.model.TestResults;

public final class HttpHelper {

    private HttpHelper() {
        throw new IllegalCallerException("utility class constructor");
    }

    public static void postTestResults(TestResults results, String url, String secret) throws IOException, HttpException {
        final String body = new Gson().toJson(results);
        final HttpResponse response = Request.Post(url)
                .addHeader("Authorization", secret)
                .addHeader("Accept", ContentType.APPLICATION_JSON.getMimeType())
                .bodyString(body, ContentType.APPLICATION_JSON)
                .execute()
                .returnResponse();

        checkError(response);
    }

    private static void checkError(final HttpResponse response) throws IOException, HttpException {
        if (response.getStatusLine().getStatusCode() != 200) {
            final int statusCode = response.getStatusLine().getStatusCode();
            final String errorMessage = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
            final String messageTemplate = Messages.SendTestResultsNotificationPostBuildTask_errors_postFailed();

            throw new HttpException(String.format(messageTemplate, statusCode, errorMessage));
        }
    }
}
