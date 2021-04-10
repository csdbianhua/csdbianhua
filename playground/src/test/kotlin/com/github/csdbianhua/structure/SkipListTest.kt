package com.github.csdbianhua.structure

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SkipListTest {

    @Test
    fun test() {
        val skipList = SkipList()
        val step = 3
        val data = generateSequence(1) { it + step }.takeWhile { it <= 100 }.shuffled().toList()
        for (datum in data) {
            assertTrue(skipList.insert(datum), "insert fail: $datum")
        }
        println(skipList)
        for (i in 1 until 100) {
            assertEquals(isTarget(i, step), skipList.find(i), "insert then find: $i")
        }
        println(skipList)
        val nextStep = 4
        for (i in 1 until 100 step nextStep) {
            assertEquals(isTarget(i, step), skipList.delete(i), "delete: $i")
        }
        println(skipList)
        for (i in 1 until 100) {
            assertEquals(isTarget(i, step) && !isTarget(i, nextStep), skipList.find(i), "delete then find: $i")
        }
        println(skipList)
        for (datum in data) {
            skipList.delete(datum)
        }
        println(skipList)
    }

    private fun isTarget(number: Int, step: Int) = (number - 1) % step == 0

}