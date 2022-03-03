package com.app.springbootteamprolearningplatform.service;

import com.app.springbootteamprolearningplatform.model.Category;
import com.app.springbootteamprolearningplatform.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CategoryService {
    public int page = 1;
    public final int limit = 5;

    private final CategoryRepository categoryRepository;

    @Autowired
    CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public int beginPage(int page) {
        if (page > 4) {
            return page - 2;
        } else {
            return 3;
        }
    }

    public int pageCount() {
        int categoryCount = Integer.parseInt(Long.valueOf(categoryRepository.count()).toString());
        return categoryCount % limit == 0 ? categoryCount / limit : categoryCount / limit + 1;
    }

    public int endPage(int page) {
        return Math.min(page + 2, pageCount() - 2);
    }

    public List<Integer> getPageList(int beginPage, int endPage) {
        List<Integer> pageList = new ArrayList<>();
        for (int i = beginPage; i <= endPage; i++) {
            pageList.add(i);
        }
        return pageList;
    }

    public List<Category> getCategoriesPageable(int page) {
        return categoryRepository.findAll(PageRequest.of(page, this.limit)).get().toList();
    }
}
