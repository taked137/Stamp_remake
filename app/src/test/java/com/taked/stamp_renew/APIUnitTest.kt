package com.taked.stamp_renew

import com.taked.stamp_renew.viewmodel.util.APIController
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class APIUnitTest {
    @Test
    fun testRegister() {
        val name = "banana"
        val device = "apple"
        val version = "kiwi"

        val response = runBlocking {
            APIController.registerUser(name, device, version)
        }

        Assert.assertNotEquals(response!!.uuid, "")
    }

    @Test
    fun testBeacon() {
        val uuid = "uuid"
        val quiz = 1
        val beacon = arrayOf(1, 2, 3)

        val response = runBlocking {
            APIController.judgeBeacon(uuid, quiz, beacon.toMutableList())
        }

        Assert.assertTrue(response!!.url.contains("http"))
    }

    @Test
    fun testQuizImage() {
        val uuid = "uuid"
        val quiz = 1

        val response = runBlocking {
            APIController.requestQuiz(uuid, quiz)
        }

        Assert.assertTrue(response!!.url.contains("http"))
    }

    @Test
    fun testJudgeAnswer() {
        val uuid = "uuid"
        val quiz = 5
        val answer = "はじっこ"

        val correctResponse = runBlocking {
            APIController.judgeAnswer(uuid, quiz, answer)
        }

        Assert.assertEquals(true, correctResponse!!.correct)

        val wrongResponse = runBlocking {
            APIController.judgeAnswer(uuid, quiz, quiz.toString())
        }

        Assert.assertEquals(false, wrongResponse!!.correct)
    }
}