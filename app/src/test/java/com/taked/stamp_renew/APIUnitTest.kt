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
}