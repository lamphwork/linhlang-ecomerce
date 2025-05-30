package linhlang.webconfig.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageContent {
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
