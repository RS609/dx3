package me.study.playground.member.query

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "member")
data class MemberData(
    @Id
    @Column(name = "member_id")
    val id: String? = null,

    @Column(name = "name")
    val name: String? = null,

    @Column(name = "blocked")
    val blocked: Boolean = false,
)