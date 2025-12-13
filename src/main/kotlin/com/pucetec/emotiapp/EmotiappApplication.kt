package com.pucetec.emotiapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EmotiappApplication

fun main(args: Array<String>) {
	runApplication<EmotiappApplication>(*args)
}
