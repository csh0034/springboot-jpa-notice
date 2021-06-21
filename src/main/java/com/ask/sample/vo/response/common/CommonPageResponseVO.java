package com.ask.sample.vo.response.common;

import com.ask.sample.constant.Constant;
import com.ask.sample.constant.ResponseCode;
import com.ask.sample.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

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

    private CommonPageResponseVO(ResponseCode responseCode, List<T> result, PageVO pageVO) {
        this.timestamp = DateUtils.formatNow(Constant.DATE_FORMAT.getValue());
        this.code = responseCode.getCode();
        this.result = result;
        this.page = pageVO;
    }

    public static <T> CommonPageResponseVO<T> ok(Page<T> result) {
        return new CommonPageResponseVO<>(ResponseCode.OK, result.getContent(), PageVO.of(result));
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = PRIVATE)
    public static class PageVO {
        private int size;
        private long totalElements;
        private int totalPages;
        private int number;

        public static PageVO of(Page<?> result) {
            PageVO pageVO = new PageVO();
            pageVO.size = result.getSize();
            pageVO.totalElements = result.getTotalElements();
            pageVO.totalPages = result.getTotalPages();
            pageVO.number = result.getNumber();
            return pageVO;
        }
    }
}