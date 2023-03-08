package android.ncdev.core_db.model

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
open class GirlModel(
    @PrimaryKey
    var id: Int = -1,
    var url: String = ""
) : RealmObject(), Parcelable