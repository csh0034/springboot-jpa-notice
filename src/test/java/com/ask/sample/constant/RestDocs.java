package com.ask.sample.constant;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;

public class RestDocs {

  public static FieldDescriptor[] noticeResponseDescriptor = {
      fieldWithPath("timestamp").description("응답시간"),
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

  public static FieldDescriptor[] userResponseDescriptor = {
      fieldWithPath("timestamp").description("응답시간"),
      fieldWithPath("code").description("응답코드"),
      fieldWithPath("result.id").description("유저 ID"),
      fieldWithPath("result.email").description("이메일"),
      fieldWithPath("result.name").description("이름"),
      fieldWithPath("result.createdBy").description("등록자"),
      fieldWithPath("result.createdDt").description("등록일 " + Constants.DATE_FORMAT),
  };
}
