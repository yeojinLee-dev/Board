package com.web.Board.Domain.Category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Category {

    private int category_id;

    private String name;

    private boolean is_deleted;

    public Category(int category_id, String name, boolean is_deleted) {
        this.category_id = category_id;
        this.name = name;
        this.is_deleted = is_deleted;
    }
}
