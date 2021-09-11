package com.taked.stamp

import com.taked.stamp.model.api.APIController
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class APIUnitTest {

    companion object {
        const val name = "banana"
        const val device = "apple"
        const val version = "kiwi"

        const val uuid = "uuid"
        const val quiz = 5
        val beacon = arrayOf(1, 2, 3)

        const val answer = "はじっこ"
    }

    @Test
    fun testRegister() {
        val response = runBlocking {
            APIController.registerUser(name, device, version)
        }

        Assert.assertNotEquals(response!!.uuid, "")
    }

    @Test
    fun testBeacon() {
        val response = runBlocking {
            APIController.judgeBeacon(uuid, quiz, beacon.toMutableList())
        }

        Assert.assertTrue(response!!.url.contains("http"))
    }

    @Test
    fun testQuizImage() {
        val response = runBlocking {
            APIController.requestQuiz(uuid, quiz)
        }

        Assert.assertTrue(response!!.url.contains("http"))
    }

    @Test
    fun testJudgeAnswer() {
        val correctResponse = runBlocking {
            APIController.judgeAnswer(uuid, quiz, answer)
        }

        Assert.assertEquals(true, correctResponse!!.correct)

        val wrongResponse = runBlocking {
            APIController.judgeAnswer(uuid, quiz, quiz.toString())
        }

        Assert.assertEquals(false, wrongResponse!!.correct)
    }

    @Test
    fun testGoal() {
        val correctResponse = runBlocking {
            APIController.postGoal(uuid)
        }

        Assert.assertEquals(true, correctResponse!!.accept)
    }

    @Test
    fun a() {
        val a = runBlocking {
            APIController.test()
        }
        println(a.toString())
    }

}
