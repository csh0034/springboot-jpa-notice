package com.ask.sample.doc;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.ask.sample.constant.Constants;
import org.springframework.restdocs.payload.FieldDescriptor;

public class RestDocs {

  public static FieldDescriptor[] noticeFindResponseDescriptor = {
      fieldWithPath("timestamp").description("요청시간"),
      fieldWithPath("code").description("응답코드"),
      fieldWithPath("result.id").description("공지사항 ID"),
      fieldWithPath("result.title").description("제목"),
      fieldWithPath("result.content").description("내용"),
      fieldWithPath("result.fileCnt").description("파일수"),
      fieldWithPath("result.readCnt").description("조회수"),
      fieldWithPath("result.createdBy").description("등록자"),
      fieldWithPath("result.createdDt").description("등록일 " + Constants.DATE_FORMAT),
      fieldWithPath("result.files[].id").description("파일 ID"),
      fieldWithPath("result.files[].fileNm").description("파일명"),
      fieldWithPath("result.files[].contentType").description("Content Type"),
      fieldWithPath("result.files[].downloadCnt").description("다운로드수"),
      fieldWithPath("result.files[].fileUrl").description("파일Url"),
      fieldWithPath("result.files[].createdBy").description("등록자"),
      fieldWithPath("result.files[].createdDt").description("등록일 " + Constants.DATE_FORMAT)
  };
}
