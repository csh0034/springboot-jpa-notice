package com.ask.sample.vo.response.common;

import static lombok.AccessLevel.PRIVATE;

import com.ask.sample.constant.Constants;
import com.ask.sample.constant.ResponseCode;
import com.ask.sample.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = PRIVATE)
public class CommonPageResponseVO<T> {

  private String timestamp;

  private String code;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<T> result;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private PageVO page;

  public static <T> CommonPageResponseVO<T> ok(Page<T> result) {
    CommonPageResponseVO<T> responseVO = new CommonPageResponseVO<>();
    responseVO.timestamp = DateUtils.formatNow(Constants.DATE_FORMAT);
    responseVO.code = ResponseCode.OK.getCode();
    responseVO.result = result.getContent();
    responseVO.page = PageVO.of(result);
    return responseVO;
  }

  @Getter
  @Setter
  @NoArgsConstructor(access = PRIVATE)
  public static class PageVO {

    private int size;
    private long totalElements;
    private int totalPages;
    private int currentPage;

    public static PageVO of(Page<?> result) {
      PageVO pageVO = new PageVO();
      pageVO.size = result.getSize();
      pageVO.totalElements = result.getTotalElements();
      pageVO.totalPages = result.getTotalPages();
      pageVO.currentPage = result.getNumber();
      return pageVO;
    }
  }
}