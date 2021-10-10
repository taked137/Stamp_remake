package com.taked.stamp

import com.taked.stamp.model.api.*
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

        val instance = APIClient().mockInstance()
    }

    @Test
    fun register() {
        val response = runBlocking {
            instance.register(UserRequest(name, device, version))
        }

        Assert.assertNotEquals(response.uuid, "")
    }

    @Test
    fun beacon() {
        val response = runBlocking {
            instance.beacon(uuid, BeaconRequest(quiz, beacon.toMutableList()))
        }

        Assert.assertTrue(response.url.contains("http"))
    }

    @Test
    fun image() {
        val response = runBlocking {
            instance.image(uuid, quiz)
        }

        Assert.assertTrue(response.url.contains("http"))
    }

    @Test
    fun judge() {
        val correctResponse = runBlocking {
            instance.judge(uuid, AnswerRequest(quiz, answer))
        }

        Assert.assertEquals(true, correctResponse.correct)

        val wrongResponse = runBlocking {
            instance.judge(uuid, AnswerRequest(quiz, quiz.toString()))
        }

        Assert.assertEquals(false, wrongResponse.correct)
    }

    @Test
    fun goalFailure() {
        val response = runBlocking { instance.goal("") }
        Assert.assertFalse(response.accept)
    }

    @Test
    fun goalSuccess() {
        val response = runBlocking { instance.goal("goal") }
        Assert.assertTrue(response.accept)
    }

    @Test
    fun info() {
        val response = runBlocking {
            instance.infotitle(0, 0)
        }
        Assert.assertTrue(response.result.isNotEmpty())
    }

}
