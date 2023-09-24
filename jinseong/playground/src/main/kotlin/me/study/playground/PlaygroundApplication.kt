package me.study.playground

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PlaygroundApplication

fun main(args: Array<String>) {
    runApplication<PlaygroundApplication>(*args)
}
