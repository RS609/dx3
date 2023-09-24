package me.study.playground.member.query

import me.study.playground.common.jpa.Rangeable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.Repository

interface MemberDataDao : Repository<MemberData, String> {

    fun findById(memberId: String): MemberData?

    fun findByBlocked(blocked: Boolean, pageable: Pageable): Page<MemberData>
    fun findByNameLike(name: String, pageable: Pageable): List<MemberData>

    fun findAll(spec: Specification<MemberData>, pageable: Pageable): List<MemberData>

    fun getRange(spec: Specification<MemberData>, rangeable: Rangeable): List<MemberData>

    fun findFirst3ByNameLikeOrderByName(name: String): List<MemberData>
    fun findFirstByNameLikeOrderByName(name: String): MemberData?
    fun findFirstByBlockedOrderById(blocked: Boolean): MemberData
}
