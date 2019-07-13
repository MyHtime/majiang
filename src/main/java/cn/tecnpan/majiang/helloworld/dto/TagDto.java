package cn.tecnpan.majiang.helloworld.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class TagDto {
    private String categoryName;
    private List<String> tags;
}
