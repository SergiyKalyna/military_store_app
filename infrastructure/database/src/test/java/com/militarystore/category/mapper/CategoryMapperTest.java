package com.militarystore.category.mapper;

import com.militarystore.entity.category.Category;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.militarystore.jooq.Tables.CATEGORIES;
import static org.assertj.core.api.Assertions.assertThat;

class CategoryMapperTest {

    private static final DSLContext DSL_CONTEXT = DSL.using(SQLDialect.POSTGRES);

    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        categoryMapper = new CategoryMapper();
    }

    @Test
    void map() {
        var categoryRecord = DSL_CONTEXT.newRecord(CATEGORIES.fields());
        categoryRecord.setValue(CATEGORIES.ID, 1);
        categoryRecord.setValue(CATEGORIES.NAME, "name");

        var expectedCategory = Category.builder()
            .id(1)
            .name("name")
            .build();

        assertThat(categoryMapper.map(categoryRecord)).isEqualTo(expectedCategory);
    }
}