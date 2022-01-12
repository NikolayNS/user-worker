package com.dmitrenko.userworker.kafka.producer


interface Producer<K, V> {

	fun sendMessage(key: K, value: V)
}