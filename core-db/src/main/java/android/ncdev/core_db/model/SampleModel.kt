package android.ncdev.core_db.model

import android.os.Parcelable
import io.realm.RealmObject
import kotlinx.parcelize.Parcelize

@Parcelize
open class SampleModel(
    var id: Long = 0,
    var name: String = ""
) : RealmObject(), Parcelable