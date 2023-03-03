package android.ncdev.core_db.realm

import io.realm.DynamicRealm
import io.realm.FieldAttribute
import io.realm.RealmMigration
import io.realm.RealmObjectSchema


class RealmMigrations : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        var oldVersion = oldVersion
        val schema = realm.schema
    }

    private fun RealmObjectSchema.addFields(
        type: Class<*>,
        fields: Array<String>,
        vararg attributes: FieldAttribute
    ) = apply {
        fields.forEach { this.addField(it, type, *attributes) }
    }

    companion object {
        const val CURRENT_VERSION = 1
    }
}
