package com.example.baseskotlin

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.experimental.and

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        convertIntToByteArray()
        convertToBase64Str()
//        convertHexStringToByteArray()
    }

    fun convertIntToByteArray() {

        val number = 182830
//        val bytes: ByteArray = getByteArrayFromInt(number)
        val bytes: Array<Byte> = getByteArrayFromInt(number)

        println(
            "IntValue($number) => BinaryString(" +
                    "${bytes.joinToString { x -> "${getByteBinaryStr(x)}" }})"
        )
    }

    private fun getByteBinaryStr(byte: Byte): String {

        var result = ""
        var byte = byte
        var counter = java.lang.Byte.SIZE
        val mask: Byte = (0b10000000).toByte()

        while (counter > 0) {
            val c = if (byte.and(mask) == mask) '1' else '0'
            result += c
            byte = byte.toInt().shl(1).toByte()
            counter -= 1
        }
        return result
    }

    private fun getByteArrayFromInt(number: Int): Array<Byte> {

        val result = Array<Byte>(java.lang.Integer.BYTES, { 0 })
        var number = number
        var mask = 0xFF
        for (i in 0 until result.size) {
            result[i] = number.and(mask).toByte()
            number = number.shr(8)
        }
        result.reverse()

        return result
    }

    // = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToBase64Str() {

        val text = "abcde"

        println("text: $text")
        println()
//        println("From API: ${fromAPI(text)}")
//        println("From Manual: ${manual(text)}")
        Log.e("TAG", "From API: ${fromAPI(text)}")
        Log.e("TAG", "From Manual: ${manual(text)}")
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun fromAPI(text: String): String {

        var result = ""
        val bytes = text.toByteArray()
        result = String(Base64.getEncoder().encode(bytes))

        return result
    }

    private fun manual(text: String): String {

        var result = StringBuilder()
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"

        val bytes: Array<Byte> = text.toByteArray().toTypedArray()

        var nHighBits = 0
        var nLowBits = 0

        var code = 0
        var prevItRemainingBits = 0

        for (byte in bytes) {
            nHighBits = 6 - nLowBits
            nLowBits = 8 - nHighBits

            code = byte.toInt() shl (8 - nHighBits)
            code = (prevItRemainingBits shl nHighBits) or code

            result.append(alphabet[code])

            if (nLowBits == 6) {

                result.append(alphabet[prevItRemainingBits])

                // reset
                prevItRemainingBits = 0
                nLowBits = 0

            }
        }

        if (prevItRemainingBits != 0) {
            val lastCode = prevItRemainingBits shl (6 - nLowBits)
            result.append(alphabet[lastCode])
        }

        var padding = 3 - bytes.size % 3
        if (padding < 3) {
            while (padding-- > 0) result.append("=")
        }

        return result.toString()
    }


}