package com.example.docenteapp

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ServerIntegrationTest {

    @Test
    fun testRootEndpoint() = testApplication {
        application {
            module()
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Backend Running"))
    }

    @Test
    fun testLoginEndpoint() = testApplication {
        application {
            module()
        }
        val response = client.post("/login")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("test@example.com"))
    }

    @Test
    fun testGetCoursesEndpoint() = testApplication {
        application {
            module()
        }
        val response = client.get("/courses?professorId=prof_100")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("prof_100"))
        assertTrue(response.bodyAsText().contains("Matemáticas 101"))
    }
}
