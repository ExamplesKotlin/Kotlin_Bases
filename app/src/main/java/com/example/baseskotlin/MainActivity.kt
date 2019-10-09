package com.example.baseskotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlin.experimental.and

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        convertIntToByteArray()
    }

    fun convertIntToByteArray() {

        val number = 182830
//        val bytes: ByteArray = getByteArrayFromInt(number)
        val bytes: Array<Byte> = getByteArrayFromInt(number)

        println("IntValue($number) => BinaryString(" +
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
}