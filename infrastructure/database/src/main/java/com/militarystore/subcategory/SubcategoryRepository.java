package com.militarystore.subcategory;

import com.militarystore.entity.subcategory.Subcategory;
import com.militarystore.jooq.tables.records.SubcategoriesRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.militarystore.jooq.Tables.SUBCATEGORIES;

@Repository
@RequiredArgsConstructor
public class SubcategoryRepository {

    private final DSLContext dslContext;

    public void addSubcategory(Subcategory subcategory) {
        dslContext.insertInto(SUBCATEGORIES)
            .set(SUBCATEGORIES.NAME, subcategory.name())
            .set(SUBCATEGORIES.CATEGORY_ID, subcategory.categoryId())
            .execute();
    }

    public void updateSubcategory(Subcategory subcategory) {
        dslContext.update(SUBCATEGORIES)
            .set(SUBCATEGORIES.NAME, subcategory.name())
            .set(SUBCATEGORIES.CATEGORY_ID, subcategory.categoryId())
            .where(SUBCATEGORIES.ID.eq(subcategory.id()))
            .execute();
    }

    public void deleteSubcategory(int id) {
        dslContext.deleteFrom(SUBCATEGORIES)
            .where(SUBCATEGORIES.ID.eq(id))
            .execute();
    }

    public List<SubcategoriesRecord> getSubcategoriesByCategoryId(int categoryId) {
        return dslContext.selectFrom(SUBCATEGORIES)
            .where(SUBCATEGORIES.CATEGORY_ID.eq(categoryId))
            .fetch();
    }

    public boolean isSubcategoryExists(int id) {
        return dslContext.fetchExists(dslContext.selectFrom(SUBCATEGORIES)
            .where(SUBCATEGORIES.ID.eq(id)));
    }

    public SubcategoriesRecord getSubcategoryById(int id) {
        return dslContext.selectFrom(SUBCATEGORIES)
            .where(SUBCATEGORIES.ID.eq(id))
            .fetchOne();
    }
}
