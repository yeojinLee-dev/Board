package com.web.Board.Service;

import com.web.Board.Domain.Category.Category;
import com.web.Board.Domain.Category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAllCategory() {
        return categoryRepository.findAllCategory();
    }
}
