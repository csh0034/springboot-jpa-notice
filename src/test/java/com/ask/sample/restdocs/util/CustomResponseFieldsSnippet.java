package com.ask.sample.restdocs.util;

import java.util.Collections;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.payload.AbstractFieldsSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;

public class CustomResponseFieldsSnippet extends AbstractFieldsSnippet {

  public CustomResponseFieldsSnippet(String type, PayloadSubsectionExtractor<?> subsectionExtractor,
      List<FieldDescriptor> descriptors, boolean ignoreUndocumentedFields) {
    super(type, descriptors, Collections.emptyMap(), ignoreUndocumentedFields, subsectionExtractor);
  }

  @Override
  protected MediaType getContentType(Operation operation) {
    return operation.getResponse().getHeaders().getContentType();
  }

  @Override
  protected byte[] getContent(Operation operation) {
    return operation.getResponse().getContent();
  }
}
