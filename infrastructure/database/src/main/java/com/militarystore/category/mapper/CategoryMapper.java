package com.militarystore.category.mapper;

import com.militarystore.entity.category.Category;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.militarystore.jooq.Tables.CATEGORIES;

@Component
public class CategoryMapper implements RecordMapper<Record, Category> {

    @Override
    public Category map(Record categoryRecord) {
        return Category.builder()
            .id(categoryRecord.get(CATEGORIES.ID))
            .name(categoryRecord.get(CATEGORIES.NAME))
            .build();
    }
}
