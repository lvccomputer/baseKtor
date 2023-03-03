package android.ncdev.core_db.model

import android.os.Parcelable
import io.realm.RealmObject
import kotlinx.parcelize.Parcelize

@Parcelize
open class TestModel(
    var id: Int = 0,
    var name: String = ""
) : RealmObject(), Parcelable