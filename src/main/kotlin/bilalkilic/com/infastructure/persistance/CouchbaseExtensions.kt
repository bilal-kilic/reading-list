package bilalkilic.com.infastructure.persistance

import bilalkilic.com.infastructure.plugins.jsonSerializer
import com.couchbase.lite.Database
import com.couchbase.lite.MutableDocument
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

inline fun <reified T> Database.save(id: String, document: T) {
    val mutableDocument = MutableDocument(id)
    mutableDocument.setJSON(jsonSerializer.encodeToString(document))
    this.save(mutableDocument)
}

inline fun <reified T> Database.get(id: String): T? {
    val doc = this.getDocument(id)?.toJSON() ?: return null

    return jsonSerializer.decodeFromString(doc)
}