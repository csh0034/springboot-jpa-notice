package com.ask.sample.vo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString(callSuper = true)
public class NoticeResponseVO extends BaseResponseVO {

    private String id;

    private String title;

    private String content;

    private Long readCnt;

    private List<AttachmentResponseVO> files;
}