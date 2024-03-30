package com.militarystore.category;

import com.militarystore.entity.category.Category;
import com.militarystore.jooq.tables.records.CategoriesRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.militarystore.jooq.Tables.CATEGORIES;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final DSLContext dslContext;

    public void addCategory(Category category) {
        dslContext.insertInto(CATEGORIES)
            .set(CATEGORIES.NAME, category.name())
            .execute();
    }

    public void updateCategory(Category category) {
        dslContext.update(CATEGORIES)
            .set(CATEGORIES.NAME, category.name())
            .where(CATEGORIES.ID.eq(category.id()))
            .execute();
    }

    public void deleteCategory(int id) {
        dslContext.deleteFrom(CATEGORIES)
            .where(CATEGORIES.ID.eq(id))
            .execute();
    }

    public List<CategoriesRecord> getCategories() {
        return dslContext.selectFrom(CATEGORIES)
            .fetch();
    }

    public boolean isCategoryExists(int id) {
        return dslContext.fetchExists(dslContext.selectFrom(CATEGORIES)
            .where(CATEGORIES.ID.eq(id)));
    }

    public CategoriesRecord getCategoryById(int id) {
        return dslContext.selectFrom(CATEGORIES)
            .where(CATEGORIES.ID.eq(id))
            .fetchOne();
    }
}
