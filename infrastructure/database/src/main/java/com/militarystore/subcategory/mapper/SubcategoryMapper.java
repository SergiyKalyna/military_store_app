package com.militarystore.subcategory.mapper;

import com.militarystore.entity.subcategory.Subcategory;
import org.jooq.RecordMapper;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.militarystore.jooq.Tables.SUBCATEGORIES;

@Component
public class SubcategoryMapper implements RecordMapper<Record, Subcategory> {

    @Override
    public Subcategory map(Record subcategoryRecord) {
        return Subcategory.builder()
            .id(subcategoryRecord.get(SUBCATEGORIES.ID))
            .name(subcategoryRecord.get(SUBCATEGORIES.NAME))
            .categoryId(subcategoryRecord.get(SUBCATEGORIES.CATEGORY_ID))
            .build();
    }
}
