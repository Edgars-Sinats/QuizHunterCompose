package com.example.quizhuntercompose.core_dbo

interface EntityMapper<Domain, Entity> {
    fun toDomain(entity: Entity): Domain
    fun toEntity(domain: Domain): Entity
}

interface QuestionMapper<Entity, DomainQuestion, DomainAnswer, testId> {
    fun toDomain(entity: Entity): Pair<DomainQuestion, DomainAnswer>
    fun toEntity(domain: Pair<DomainQuestion, DomainAnswer>, testID: testId): Entity
}