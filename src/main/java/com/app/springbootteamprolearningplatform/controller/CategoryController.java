package com.app.springbootteamprolearningplatform.controller;

import com.app.springbootteamprolearningplatform.model.Category;
import com.app.springbootteamprolearningplatform.repository.CategoryRepository;
import com.app.springbootteamprolearningplatform.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping({"/categories"})
public class CategoryController {
    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository, CategoryService categoryService) {
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getCategory(Model model, @RequestParam(required = false, defaultValue = "1") int page) {
//        model.addAttribute("categories", this.categoryRepository.getAll());
        if (page > 1) {
            categoryService.page = page;
        }
        model.addAttribute("categories", categoryService.getCategoriesPageable(page));
        model.addAttribute("currentPage", page);
        model.addAttribute("endPage", categoryService.endPage(page));
        model.addAttribute("beginPage", categoryService.beginPage(page));
        model.addAttribute("pageCount", categoryService.pageCount());
        model.addAttribute("listPage", categoryService.getPageList(categoryService.beginPage(page), categoryService.endPage(page)));
        return "view-categories";
    }

    @GetMapping({"/{categoryId}"})
    public String getCategoryById(@PathVariable UUID categoryId, Model model) {
        model.addAttribute("categoryById", this.categoryRepository.getById(categoryId));
        return "view-category";
    }

    @GetMapping({"/add"})
    public String addCategory() {
        return "add-category";
    }

    @PostMapping({"/add"})
    public String addCategory(Category category) {
        this.categoryRepository.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/edit-profile")
    public String editCategory() {
        return "edit-profile";
    }

    @PostMapping({"/update"})
    public String update(Category category) {
        categoryRepository.save(category);
        return "redirect:/categories";
    }

    @GetMapping({"/edit/{categoryId}"})
    public String editCategory(@PathVariable UUID categoryId, Model model) {
        model.addAttribute("category", categoryRepository.findById(categoryId).orElse(null));
        return "update-category";
    }

    @GetMapping({"/delete"})
    public String deleteCategory(@RequestParam UUID id) {
        this.categoryRepository.deleteById(id);
        return "redirect:/categories";
    }

    @ModelAttribute(value = "role")
    public String getRole(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (String) session.getAttribute("role");
    }
}
