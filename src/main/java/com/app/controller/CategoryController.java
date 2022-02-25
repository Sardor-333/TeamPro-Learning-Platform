

package com.app.controller;

import com.app.model.Category;
import com.app.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Controller
@RequestMapping({"/categories"})
public class CategoryController {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String getCategory(Model model) {
        model.addAttribute("categories", this.categoryRepository.getAll());
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
    public String editCategory(){
        return "edit-profile";
    }

    @PostMapping({"/update"})
    public String update(Category category) {
        this.categoryRepository.update(category);
        return "redirect:/categories";
    }

    @GetMapping({"/edit/{categoryId}"})
    public String editCategory(@PathVariable UUID categoryId, Model model) {
        model.addAttribute("category", this.categoryRepository.getById(categoryId));
        return "update-category";
    }

    @GetMapping({"/delete"})
    public String deleteCategory(@RequestParam UUID id) {
        this.categoryRepository.deleteById(id);
        return "redirect:/categories";
    }
}
