package br.com.livelo.wiremock;

import java.lang.reflect.Field;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.servlet.WireMockHttpServletRequestAdapter;

public class WiremockProxyTransformer extends ResponseDefinitionTransformer {
    private static final String PREFIX_FIELD_NAME = "urlPrefixToRemove";

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return responseDefinition;
        }

        String urlPrefixToRemove = parameters.getString(PREFIX_FIELD_NAME);

        try {
            Field field = WireMockHttpServletRequestAdapter.class.getDeclaredField(PREFIX_FIELD_NAME);
            field.setAccessible(true);
            field.set(request, urlPrefixToRemove);
        } catch (Exception e) {
            return responseDefinition;
        }

        return responseDefinition;
    }

    public String getName() {
        return "my-proxy";
    }

}
