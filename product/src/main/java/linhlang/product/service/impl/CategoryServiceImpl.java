package linhlang.product.service.impl;

import linhlang.commons.exceptions.BusinessException;
import linhlang.commons.model.PageData;
import linhlang.product.constants.Errors;
import linhlang.product.controller.request.CommonSearchReq;
import linhlang.product.controller.request.SaveCategoryReq;
import linhlang.product.model.Category;
import linhlang.product.repository.CategoryRepository;
import linhlang.product.service.CategoryService;
import linhlang.product.service.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public PageData<Category> query(CommonSearchReq request) {
        return categoryRepository.query(request);
    }

    @Override
    public Category create(SaveCategoryReq req) {
        if (categoryRepository.findSameName(req.name()) != null) {
            throw new BusinessException(Errors.CATEGORY_EXISTED);
        }

        Category category = categoryMapper.toEntity(req);
        categoryRepository.save(category);
        return category;
    }

    @Override
    public Category update(String id, SaveCategoryReq req) {
        Category category = categoryRepository.load(id);
        if (category == null) {
            throw new BusinessException(Errors.CATEGORY_NOTFOUND);
        }

        categoryMapper.update(category, req);
        categoryRepository.save(category);
        return category;
    }

    @Override
    public void delete(String id) {
        Category category = categoryRepository.load(id);
        if (category == null) {
            throw new BusinessException(Errors.CATEGORY_NOTFOUND);
        }

        categoryRepository.delete(id);
    }
}
