package com.ask.sample.restdocs.controller;

import com.ask.sample.constant.ResponseCode;
import com.ask.sample.vo.response.common.CommonResponseVO;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocsController {

  @GetMapping("/docs")
  public CommonResponseVO<Docs> docs() {
    return CommonResponseVO.ok(Docs.builder()
        .responseCodes(convertResponseCodes())
        .build());
  }

  private Map<String, String> convertResponseCodes() {
    return Arrays.stream(ResponseCode.values()).collect(Collectors.toMap(ResponseCode::getCode, ResponseCode::getMessage));
  }
}
