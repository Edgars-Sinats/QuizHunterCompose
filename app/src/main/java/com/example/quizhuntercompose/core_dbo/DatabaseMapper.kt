package com.example.quizhuntercompose.core_dbo

import com.example.quizhuntercompose.core_dbo.test.TestEntity
import com.example.quizhuntercompose.domain.model.Test

object DatabaseMapper: EntityMapper<List<Test>, List<TestEntity>> {
    override fun toDomain(entity: List<TestEntity>): List<Test> {
        return entity.map { data ->
            Test(
                testID = data.testId,
                testName = data.testName,
                testDescription = data.testDescription ?: "",
                dateCreated = data.testCreated,
                dateModified = data.testModified ?: "",
                isFavorite = data.isFavorite,
                needKey = data.needKey,
                additionalInfo = data.additionalInfo ?: "",
                language = data.language,
                testRank = data.testRank
//                topics = data.
            )
        }
    }

    override fun toEntity(domain: List<Test>): List<TestEntity> {
        return domain.map { data ->
            TestEntity(
                testId = data.testID,
                testName = data.testName,
                testDescription = data.testDescription,
                testCreated = data.dateCreated,
                testModified = data.dateModified,
                isFavorite = data.isFavorite,
                needKey = data.needKey,
                additionalInfo = data.additionalInfo,
                language = data.language,
                testImageUrl = data.testImageUrl,
                isLocal = false,
                testRank = data.testRank
            )
        }
    }
}

fun List<TestEntity>.toDomain() = DatabaseMapper.toDomain(this)
fun List<Test>.toEntity() = DatabaseMapper.toEntity(this)