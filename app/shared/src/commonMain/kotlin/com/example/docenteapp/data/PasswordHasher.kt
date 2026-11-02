package com.example.docenteapp.data

@OptIn(ExperimentalUnsignedTypes::class)
object PasswordHasher {
    private const val GLOBAL_SALT = "DocenteApp_Salt_2026_Secured_v1"

    fun hash(password: String): String {
        val saltedPassword = "$GLOBAL_SALT:$password:$GLOBAL_SALT"
        val bytes = saltedPassword.encodeToByteArray()
        val mainLen = bytes.size
        val padLen = if (mainLen % 64 < 56) 64 - (mainLen % 64) else 128 - (mainLen % 64)
        val totalLen = mainLen + padLen
        val padded = ByteArray(totalLen)
        bytes.copyInto(padded)
        padded[mainLen] = 0x80.toByte()

        val bits = (mainLen.toLong() * 8)
        for (i in 0..7) {
            padded[totalLen - 1 - i] = ((bits ushr (i * 8)) and 0xFFL).toByte()
        }

        val k = uintArrayOf(
            0x428a2f98U, 0x71374491U, 0xb5c0fbcfU, 0xe9b5dba5U, 0x3956c25bU, 0x59f111f1U, 0x923f82a4U, 0xab1c5ed5U,
            0xd807aa98U, 0x12835b01U, 0x243185beU, 0x550c7dc3U, 0x72be5d74U, 0x80deb1feU, 0x9bdc06a7U, 0xc19bf174U,
            0xe49b69c1U, 0xefbe4786U, 0x0fc19dc6U, 0x240ca1ccU, 0x2de92c6fU, 0x4a7484aaU, 0x5cb0a9dcU, 0x76f988daU,
            0x983e5152U, 0xa831c66dU, 0xb00327c8U, 0xbf597fc7U, 0xc6e00bf3U, 0xd5a79147U, 0x06ca6351U, 0x14292967U,
            0x27b70a85U, 0x2e1b2138U, 0x4d2c6dfcU, 0x53380d13U, 0x650a7354U, 0x766a0abbU, 0x81c2c92eU, 0x92722c85U,
            0xa2bfe8a1U, 0xa81a664bU, 0xc24b8b70U, 0xc76c51a3U, 0xd192e819U, 0xd6990624U, 0xf40e3585U, 0x106aa070U,
            0x19a4c116U, 0x1e376c08U, 0x2748774cU, 0x34b0bcb5U, 0x391c0cb3U, 0x4ed8aa4aU, 0x5b9cca4fU, 0x682e6ff3U,
            0x748f82eeU, 0x78a5636fU, 0x84c87814U, 0x8cc70208U, 0x90befffaU, 0xa4506cebU, 0xbef9a3f7U, 0xc67178f2U
        )

        var h0 = 0x6a09e667U
        var h1 = 0xbb67ae85U
        var h2 = 0x3c6ef372U
        var h3 = 0xa54ff53aU
        var h4 = 0x510e527fU
        var h5 = 0x9b05688cU
        var h6 = 0x1f83d9abU
        var h7 = 0x5be0cd19U

        val w = UIntArray(64)
        for (chunk in 0 until totalLen step 64) {
            for (i in 0..15) {
                val p = chunk + i * 4
                w[i] = ((padded[p].toUInt() and 0xFFU) shl 24) or
                       ((padded[p + 1].toUInt() and 0xFFU) shl 16) or
                       ((padded[p + 2].toUInt() and 0xFFU) shl 8) or
                       (padded[p + 3].toUInt() and 0xFFU)
            }
            for (i in 16..63) {
                val s0 = (w[i - 15] rotateRight 7) xor (w[i - 15] rotateRight 18) xor (w[i - 15] shr 3)
                val s1 = (w[i - 2] rotateRight 17) xor (w[i - 2] rotateRight 19) xor (w[i - 2] shr 10)
                w[i] = w[i - 16] + s0 + w[i - 7] + s1
            }

            var a = h0
            var b = h1
            var c = h2
            var d = h3
            var e = h4
            var f = h5
            var g = h6
            var h = h7

            for (i in 0..63) {
                val S1 = (e rotateRight 6) xor (e rotateRight 11) xor (e rotateRight 25)
                val ch = (e and f) xor (e.inv() and g)
                val temp1 = h + S1 + ch + k[i] + w[i]
                val S0 = (a rotateRight 2) xor (a rotateRight 13) xor (a rotateRight 22)
                val maj = (a and b) xor (a and c) xor (b and c)
                val temp2 = S0 + maj

                h = g
                g = f
                f = e
                e = d + temp1
                d = c
                c = b
                b = a
                a = temp1 + temp2
            }

            h0 += a
            h1 += b
            h2 += c
            h3 += d
            h4 += e
            h5 += f
            h6 += g
            h7 += h
        }

        return listOf(h0, h1, h2, h3, h4, h5, h6, h7).joinToString("") { 
            it.toString(16).padStart(8, '0') 
        }
    }
}

private infix fun UInt.rotateRight(bits: Int): UInt = (this shr bits) or (this shl (32 - bits))
