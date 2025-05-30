package linhlang.webconfig.controller.request;

import linhlang.webconfig.model.QueryRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogRequest {
    private String id;
    private String title;
    private String content;
    private String createUser;
    private String blogCategory;
    private String blogQuote;
    private String seoTitle;
    private String seoDescription;
    private String seoUrl;
    private String canonicalUrl;
    private String metaIndex;
    private String metaFollow;
    private LocalDateTime displayTime;
    private BigDecimal isDisplay;
    private String image;
    private String imageDescription;
    private String tag;
    private BigDecimal status;
    private String pageInterface;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
