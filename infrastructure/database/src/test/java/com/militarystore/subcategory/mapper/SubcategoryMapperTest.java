package com.militarystore.subcategory.mapper;

import com.militarystore.entity.subcategory.Subcategory;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.militarystore.jooq.Tables.SUBCATEGORIES;
import static org.assertj.core.api.Assertions.assertThat;

class SubcategoryMapperTest {
    private static final DSLContext DSL_CONTEXT = DSL.using(SQLDialect.POSTGRES);

    private SubcategoryMapper subcategoryMapper;

    @BeforeEach
    void setUp() {
        subcategoryMapper = new SubcategoryMapper();
    }

    @Test
    void map() {
        var subcategoryRecord = DSL_CONTEXT.newRecord(SUBCATEGORIES.fields());
        subcategoryRecord.setValue(SUBCATEGORIES.ID, 1);
        subcategoryRecord.setValue(SUBCATEGORIES.NAME, "name");
        subcategoryRecord.setValue(SUBCATEGORIES.CATEGORY_ID, 1);

        var expectedSubcategory = Subcategory.builder()
            .id(1)
            .name("name")
            .categoryId(1)
            .build();

        assertThat(subcategoryMapper.map(subcategoryRecord)).isEqualTo(expectedSubcategory);
    }
}