package ru.mcb.baas.userworker.kafka.producer


interface Producer<K, V> {

	fun sendMessage(key: K, value: V)
}