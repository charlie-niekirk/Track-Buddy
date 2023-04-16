package me.cniekirk.trackbuddy.data.util

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type
import java.util.Collections

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
@JsonQualifier
annotation class SingleOrList

object SingleOrListAdapterFactory : JsonAdapter.Factory {
    override fun create(
        type: Type,
        annotations: Set<Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        val delegateAnnotations = Types.nextAnnotations(annotations, SingleOrList::class.java)
            ?: return null
        if (Types.getRawType(type) !== List::class.java) {
            throw IllegalArgumentException("@SingleOrList requires the type to be List. Found this type: $type")
        }
        val elementType = Types.collectionElementType(type, List::class.java)
        val delegateAdapter: JsonAdapter<List<Any?>?> = moshi.adapter(type, delegateAnnotations)
        val singleElementAdapter: JsonAdapter<Any?> = moshi.adapter(elementType)
        return object : JsonAdapter<List<Any?>?>() {
            override fun fromJson(reader: JsonReader): List<Any?>? =
                if (reader.peek() !== JsonReader.Token.BEGIN_ARRAY)
                    Collections.singletonList(singleElementAdapter.fromJson(reader))
                else
                    delegateAdapter.fromJson(reader)
            override fun toJson(writer: JsonWriter, value: List<Any?>?) {
                if (value == null) return
                if (value.size == 1)
                    singleElementAdapter.toJson(writer, value[0])
                else
                    delegateAdapter.toJson(writer, value)
            }
        }
    }
}