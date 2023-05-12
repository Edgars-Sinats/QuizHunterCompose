package com.example.quizhuntercompose.core_dbo

interface EntityMapper<Domain, Entity> {
    fun toDomain(entity: Entity): Domain
    fun toEntity(domain: Domain): Entity
}