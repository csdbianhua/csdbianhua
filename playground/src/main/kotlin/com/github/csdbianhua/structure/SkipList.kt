package com.github.csdbianhua.structure

import java.util.*
import kotlin.random.Random

/**
 * 简单跳表
 */
class SkipList {
    private val random = Random(System.currentTimeMillis())

    /**
     * 最底层的头
     */
    private var head: Node = Node(Int.MIN_VALUE)

    /**
     * 最底层的尾
     */
    private var tail: Node = Node(Int.MAX_VALUE, previous = head)

    /**
     * 每层头所组成的列表，底层在前，高层在后
     */
    private val headList: LinkedList<Node> = LinkedList(listOf(head))

    /**
     * 每层尾所组成的列表，底层在前，高层在后
     */
    private val tailList: LinkedList<Node> = LinkedList(listOf(tail))

    /**
     * 查询数据是否存在
     */
    fun find(key: Int): Boolean {
        val result = findNearestNode(key)
        return result.key == key
    }

    /**
     * 插入数据
     */
    fun insert(key: Int): Boolean {
        val node = findNearestNode(key)
        return if (node.key == key) {
            false
        } else {
            val newNode = Node(key, next = node.next, previous = node)
            flip(newNode)
            true
        }
    }

    /**
     * 删除数据
     */
    fun delete(key: Int): Boolean {
        val node = findNearestNode(key)
        if (node.key != key) {
            return false
        }
        var currentNode: Node? = node
        while (currentNode != null) {
            val previous = currentNode.previous
            val next = currentNode.next
            previous?.next = next
            next?.previous = previous
            currentNode = currentNode.downstream
        }
        // 此处清理headList和tailList
        val headIt = headList.descendingIterator()
        val tailIt = tailList.descendingIterator()
        while (headIt.hasNext()) {
            val h = headIt.next()
            val t = tailIt.next()
            if (h.next === t) {
                headIt.remove()
                tailIt.remove()
            } else {
                break
            }
        }
        return true
    }

    /**
     * 找到最近的节点
     */
    private fun findNearestNode(key: Int): Node {
        if (key == Int.MAX_VALUE || key == Int.MIN_VALUE) {
            throw IllegalArgumentException("不支持的数据 $key")
        }
        var currentNode: Node = headList.last()
        while (true) {
            if (currentNode.key == key) {
                break
            } else if (currentNode.key > key) {
                currentNode = currentNode.previous ?: break
                currentNode = currentNode.downstream ?: break
            } else if (currentNode.key < key) {
                currentNode = currentNode.next ?: currentNode.downstream ?: break
            }
        }
        return currentNode
    }

    private fun flip(node: Node) {
        if (!random.nextBoolean()) {
            return
        }
        // 找到左边的存在上层引用的节点
        var currentNode = node.previous
        while (currentNode?.upstream == null && currentNode?.key != head.key) {
            currentNode = currentNode?.previous
        }
        val newNode: Node
        // 存在上层引用的节点
        if (currentNode.upstream != null) {
            currentNode = currentNode.upstream!!
            newNode = Node(node.key, downstream = node, next = currentNode.next, previous = currentNode)
        } else {
            // 最左边的头节点，准备向上新建一层
            val newHead = Node(Int.MIN_VALUE, downstream = currentNode)
            val newTail = Node(Int.MAX_VALUE, downstream = tailList.last())
            headList.add(newHead)
            tailList.add(newTail)
            newNode = Node(node.key, downstream = node, next = newTail, previous = newHead)
        }
        flip(newNode)
    }


    private data class Node(
        val key: Int,
        var downstream: Node? = null,
        var upstream: Node? = null,
        var next: Node? = null,
        var previous: Node? = null
    ) {
        init {
            previous?.next = this
            next?.previous = this
            downstream?.upstream = this
            upstream?.downstream = this
        }

        override fun toString() =
            "${previous?.key} -> $key -> ${next?.key} (up: ${upstream?.key}) (down: ${downstream?.key})"
    }

    override fun toString(): String {
        val sj = StringJoiner("\n", "-----\n", "\n----")
        for (h in headList.descendingIterator()) {
            var node: Node? = h
            val result = mutableListOf<Int>()
            while (node != null && node.next?.key != Int.MAX_VALUE) {
                node.next?.also {
                    result.add(it.key)
                }
                node = node.next
            }
            sj.add(result.toString())
        }
        return sj.toString()
    }
}