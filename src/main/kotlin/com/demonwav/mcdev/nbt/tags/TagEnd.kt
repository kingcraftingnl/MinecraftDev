/*
 * Minecraft Dev for IntelliJ
 *
 * https://minecraftdev.org
 *
 * Copyright (c) 2017 minecraft-dev
 *
 * MIT License
 */

package com.demonwav.mcdev.nbt.tags

import java.io.DataOutputStream

object TagEnd : NbtTag {
    override val payloadSize = 0
    override val typeId = NbtTypeId.END

    override fun write(stream: DataOutputStream) {
        stream.writeByte(typeIdByte.toInt())
    }

    override fun toString() = toString(StringBuilder(), 0).toString()

    override fun toString(sb: StringBuilder, indentLevel: Int): StringBuilder {
        indent(sb, indentLevel)
        sb.append(NbtTypeId.END.tagName)
        return sb
    }

    override fun copy(): NbtTag {
        return this
    }
}