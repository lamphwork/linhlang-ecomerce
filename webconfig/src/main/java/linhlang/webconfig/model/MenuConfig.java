package linhlang.webconfig.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuConfig implements Serializable {
    private String id;
    private String name;
    private String parentId;
    private String pathRoot;
    private String pathType;
    private String pathLink;
    private String tag;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long status;
}
