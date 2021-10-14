package com.ask.sample.restdocs.controller;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Docs {

  Map<String, String> responseCodes;
}
