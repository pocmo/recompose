package com.jds.recompose.values

import com.jds.recompose.attributes.Attribute

data class Id(val value: String) : Attribute {
    override fun toString(): String {
        return value
    }
}
