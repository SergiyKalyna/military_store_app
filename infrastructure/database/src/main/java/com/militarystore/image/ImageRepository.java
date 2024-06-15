package com.militarystore.image;

import com.militarystore.jooq.tables.records.ImagesRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.militarystore.jooq.Tables.IMAGES;

@Repository
@RequiredArgsConstructor
public class ImageRepository {

    private final DSLContext dslContext;

    public void saveImages(List<ImagesRecord> imagesRecords) {
        dslContext.batchInsert(imagesRecords).execute();
    }

    public List<String> getImagesByProductId(Integer productId) {
        return dslContext.select(IMAGES.GOOGLE_DRIVE_ID)
            .from(IMAGES)
            .where(IMAGES.PRODUCT_ID.eq(productId))
            .orderBy(IMAGES.ORDINAL_NUMBER)
            .fetch(IMAGES.GOOGLE_DRIVE_ID);
    }

    public Map<Integer, String> getPrimaryImagesByProductIds(List<Integer> productIds) {
        return dslContext.select(IMAGES.PRODUCT_ID, IMAGES.GOOGLE_DRIVE_ID)
            .from(IMAGES)
            .where(IMAGES.PRODUCT_ID.in(productIds))
            .and(IMAGES.ORDINAL_NUMBER.eq(1))
            .fetchMap(IMAGES.PRODUCT_ID, IMAGES.GOOGLE_DRIVE_ID);
    }

    public void deleteImagesByProductId(Integer productId) {
        dslContext.deleteFrom(IMAGES)
            .where(IMAGES.PRODUCT_ID.eq(productId))
            .execute();
    }

    public boolean isImageExist(Integer productId) {
        return dslContext.fetchExists(
            dslContext.selectOne()
                .from(IMAGES)
                .where(IMAGES.PRODUCT_ID.eq(productId))
        );
    }
}