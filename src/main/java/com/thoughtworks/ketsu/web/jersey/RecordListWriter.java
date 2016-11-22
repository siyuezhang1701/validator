package com.thoughtworks.ketsu.web.jersey;

import com.thoughtworks.ketsu.infrastructure.records.Record;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import static com.thoughtworks.ketsu.util.Json.toJson;
import static java.util.stream.Collectors.toList;

@Provider
public class RecordListWriter implements MessageBodyWriter<List<? extends Record>> {
    @Context
    javax.inject.Provider<Routes> routesProvider;

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return List.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(List<? extends Record> records, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return 0;
    }

    @Override
    public void writeTo(List<? extends Record> records, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(entityStream)) {
            outputStreamWriter.write(toJson(records.stream().map(record -> record.toJson(routesProvider.get())).collect(toList())));
        }
    }
}
