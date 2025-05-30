package linhlang.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageData<T> {

    private Integer page;
    private Integer size;
    private Integer totals;
    private List<T> content;


    public <R> PageData<R> map(Function<T, R> mapper) {
        List<R> result = content.stream()
                .map(mapper)
                .toList();
        return new PageData<>(page, size, totals, result);
    }
}
