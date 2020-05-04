package com.imooc.mall.service.impl;

import com.imooc.mall.dao.CategoryMapper;
import com.imooc.mall.pojo.Category;
import com.imooc.mall.service.ICategoryService;
import com.imooc.mall.vo.CategoryVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static com.imooc.mall.enums.MallConst.ROOT_PARENT_ID;

@Service
public class ICategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询所有的类目
     * @return
     */
    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {
        List<Category> categories = categoryMapper.selectAll();

        // 查询父目录
        // for-each 方法
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categories) {
            if (category.getParentId().equals(ROOT_PARENT_ID)) {
                categoryVoList.add(category2CategoryVo(category));
            }
        }

//        // lambda + stream 方法
//        List<CategoryVo> categoryVoList = categories.stream()
//                .filter(e -> e.getParentId() == ROOT_PARENT_ID)
//                .map(this::category2CategoryVo)
//                .collect(Collectors.toList());

        // 查询子目录

        categoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
        findSubCategories(categoryVoList, categories);

        return ResponseVo.success(categoryVoList);
    }

    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = categoryMapper.selectAll();
        findSubCategoryId(id, resultSet, categories);
    }

    private void findSubCategoryId(Integer id, Set<Integer> resultSet, List<Category> categories) {
        for (Category category : categories) {
            if (category.getParentId().equals(id)) {
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(), resultSet, categories);
            }
        }
    }

    private void findSubCategories(List<CategoryVo> categoryVoList, List<Category> categories) {
        for (CategoryVo categoryVo : categoryVoList) {
            List<CategoryVo> subCategoryVoList = new ArrayList<>();
            for (Category category : categories) {
                if (category.getParentId().equals(categoryVo.getId())) {
                    subCategoryVoList.add(category2CategoryVo(category));
                }
            }
            findSubCategories(subCategoryVoList, categories);
            subCategoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
            categoryVo.setSubCategories(subCategoryVoList);
        }
    }

    private CategoryVo category2CategoryVo(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }
}
