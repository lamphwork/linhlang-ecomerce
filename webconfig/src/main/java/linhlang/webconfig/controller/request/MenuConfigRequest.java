package linhlang.webconfig.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuConfigRequest {
    private String id;
    private String name;
    private String parentId;
    private String pathRoot;
    private String pathType;
    private String pathLink;
    private String tag;
    private Long status;
    private Set<MenuConfigRequest> menuChild = new HashSet<>();
}
