package linhlang.webconfig.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Banner {

    private String id;
    private String imageUrl;
    private Integer orderIndex;
}
