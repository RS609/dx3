package me.study.playground.common.jpa

import org.springframework.data.domain.Sort

class Rangeable(
    val start: Int,
    val limit: Int,
    val sort: Sort
) {
    companion object {
        fun of(start: Int, limit: Int): Rangeable {
            return Rangeable(start, limit, Sort.unsorted())
        }
    }
}