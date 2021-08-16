package com.taked.stamp_renew

import com.taked.stamp_renew.viewmodel.util.APIController
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class APIUnitTest {
    val name = "banana"
    val device = "apple"
    val version = "kiwi"

    val uuid = "uuid"
    val quiz = 5
    val beacon = arrayOf(1, 2, 3)

    val answer = "はじっこ"

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
}