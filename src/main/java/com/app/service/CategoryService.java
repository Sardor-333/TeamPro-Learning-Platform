package com.app.service;

import com.app.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryService {
    public int page=1;
    public int limit = 5;

    private CategoryRepository categoryRepository;

    @Autowired
    CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }


    public int beginPage(int page){
        if (page>4) {
            return page-2;
        }else {
            return 3;
        }
    }

    public int pageCount(){
        int categoryCount = categoryRepository.categoryCount();
        return categoryCount%limit==0? categoryCount/limit : categoryCount/limit+1;
    }

    public int endPage(int page){
        int pageCount = pageCount();
        if (page + 2<pageCount-2) {
            return page+2;
        }else {
            return pageCount-2;
        }
    }

    public List<Integer> getPageList(int beginPage, int endPage) {
        List<Integer> pageList = new ArrayList<>();
        for (int i=beginPage; i <= endPage; i++){
            pageList.add(i);
        }
        return pageList;
    }
}
