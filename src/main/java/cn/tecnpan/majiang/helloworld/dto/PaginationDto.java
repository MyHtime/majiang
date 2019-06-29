package cn.tecnpan.majiang.helloworld.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页对象
 */
@Setter
@Getter
@NoArgsConstructor
public class PaginationDto<E> {

    /**
     * 结果集
     */
    private List<E> objectList;
    /**
     * 是否有前一页
     */
    private boolean hasPrevious;
    /**
     * 是否有后一页
     */
    private boolean hasNext;
    /**
     * 是否展现第一页
     */
    private boolean showFirst;
    /**
     * 是否展现最后一页
     */
    private boolean showLast;
    /**
     * 当前页
     */
    private Integer currentPageNo;
    /**
     * 页码集合
     */
    private List<Integer> pageNoList = new ArrayList<>();
    /**
     * 总页数
     */
    private Integer totalPage;
    /**
     * 总记录数
     */
    private Integer totalCount;

    /**
     * 获取总页数、总记录数的构造函数
     * @param totalCount 总记录数
     * @param pageSize 每页记录数
     */
    public PaginationDto(Integer totalCount, Integer pageSize) {
        //设置总记录数
        this.totalCount = totalCount;
        //获取总页数
        if (totalCount % pageSize == 0) {
            this.totalPage = totalCount / pageSize;
        } else {
            this.totalPage = totalCount /pageSize + 1;
        }
    }

    /**
     * 分页对象各值初始化
     * @param pageNo 当前页码
     */
    public void init(Integer pageNo) {
        //设置当前页
        currentPageNo = pageNo;

        //设置页码列表（7个，当前页的前3和后3页）
        pageNoList.add(pageNo);
        for (int i = 1; i <= 3; i++) {
            if (pageNo - i > 0) {
                pageNoList.add(0, pageNo - i);
            }
            if (pageNo + i <= totalPage) {
                pageNoList.add(pageNo + i);
            }
        }


        //是否展示上一页
        this.hasPrevious = pageNo != 1;
        //是否展示下一页
        this.hasNext = !pageNo.equals(totalPage);

        //是否展示首页
        showFirst = !pageNoList.contains(1);
        //是否展示尾页
        showLast = !pageNoList.contains(totalPage);
    }

}
