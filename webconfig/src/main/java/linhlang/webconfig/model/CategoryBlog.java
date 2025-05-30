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
public class CategoryBlog {
    private String id;
    private String title;
    private String content;
    private String feedburner;
    private String seoTitle;
    private String seoDescription;
    private String seoUrl;
    private BigDecimal commentRule;
    private String listMenu;
    private BigDecimal status;
    private String pageInterface;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
