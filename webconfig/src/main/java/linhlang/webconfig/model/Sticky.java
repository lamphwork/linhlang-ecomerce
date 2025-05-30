package linhlang.webconfig.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sticky {

    private String id;
    private String title;
    private String content;
    private Boolean visible;
}
