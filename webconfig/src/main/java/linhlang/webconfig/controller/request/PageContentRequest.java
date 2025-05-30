package linhlang.webconfig.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageContentRequest {
    private String id;
    private String title;
    private String content;
    private String image;
    private String seoTitle;
    private String seoDescription;
    private String seoLink;
    private String pageInterface;
    private String menuLink;
    private BigDecimal status;
    private LocalDateTime displayTime;
    private BigDecimal isDisplay;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
