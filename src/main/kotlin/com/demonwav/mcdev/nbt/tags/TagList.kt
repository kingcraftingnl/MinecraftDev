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
import java.util.Objects

class TagList(val type: NbtTypeId, val tags: List<NbtTag>) : NbtTag {
    // TAG_List has nameless tags, so we don't need to do anything for the names of tags
    override val payloadSize = 5 + tags.sumBy { it.payloadSize }
    override val typeId = NbtTypeId.LIST

    override fun write(stream: DataOutputStream) {
        stream.writeByte(typeIdByte.toInt())
        stream.writeInt(tags.size)
        tags.forEach { it.write(stream) }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is TagList) {
            return false
        }

        if (other === this) {
            return true
        }

        if (other.type != this.type) {
            return false
        }

        if (other.tags.size != this.tags.size) {
            return false
        }

        return (0 until this.tags.size).all { i -> other.tags[i] == this.tags[i] }
    }

    override fun hashCode(): Int {
        return Objects.hash(type, tags.hashCode())
    }

    override fun toString() = toString(StringBuilder(), 0).toString()

    override fun toString(sb: StringBuilder, indentLevel: Int): StringBuilder {
        sb.append("[")

        if (tags.isEmpty()) {
            sb.append("]")
            return sb
        }

        val isCollection = type == NbtTypeId.COMPOUND || type == NbtTypeId.LIST || type == NbtTypeId.BYTE_ARRAY || type == NbtTypeId.INT_ARRAY
        for (tag in tags) {
            if (isCollection) {
                sb.append("\n")
                indent(sb, indentLevel + 1)
            } else {
                sb.append(" ")
            }

            tag.toString(sb, indentLevel + 1)
            sb.append(",")
        }

        if (isCollection) {
            sb.append("\n")
            indent(sb, indentLevel)
            sb.append("]")
        } else {
            sb.append(" ]")
        }


        return sb
    }

    override fun copy(): TagList {
        val newTags = ArrayList<NbtTag>(tags.size)
        tags.mapTo(newTags) { it.copy() }
        return TagList(type, newTags)
    }
}