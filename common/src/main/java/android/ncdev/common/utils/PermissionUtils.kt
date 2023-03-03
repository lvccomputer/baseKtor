package android.ncdev.common.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener

fun FragmentActivity.runWithPermissionChecker(permission: String, onPermissionDeny: ((PermissionDeniedResponse?) -> Unit)? = null, onPermissionGranted: (PermissionGrantedResponse?) -> Unit) {
    Dexter.withContext(this)
        .withPermission(permission)
        .withListener(object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                lifecycleScope.launchWhenResumed { onPermissionGranted(response) }
            }

            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, permissionToken: PermissionToken?) {
                permissionToken?.continuePermissionRequest()
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                response?.requestedPermission
                lifecycleScope.launchWhenResumed { onPermissionDeny?.let { it(response) } }
            }
        })
        .onSameThread()
        .check()
}

fun FragmentActivity.runWithPermissionsChecker(permissions: Collection<String>, onPermissionDeny: ((List<PermissionDeniedResponse>?) -> Unit)? = null, onPermissionGranted: (List<PermissionGrantedResponse>?) -> Unit) {
    Dexter.withContext(this)
        .withPermissions(permissions)
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report?.areAllPermissionsGranted() == true){
                    lifecycleScope.launchWhenResumed { onPermissionGranted(report.grantedPermissionResponses) }
                } else {
                    lifecycleScope.launchWhenResumed { onPermissionDeny?.invoke(report?.deniedPermissionResponses) }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?,
            ) {
                p1?.continuePermissionRequest()
            }
        })
        .onSameThread()
        .check()
}

fun Fragment.runWithPermissionChecker(permission: String, onPermissionDeny: ((PermissionDeniedResponse?) -> Unit)? = null, onPermissionGranted: (PermissionGrantedResponse?) -> Unit) {
    activity?.runWithPermissionChecker(permission, onPermissionDeny, onPermissionGranted)
}
fun Fragment.runWithPermissionsChecker(permissions: Collection<String>, onPermissionDeny: ((List<PermissionDeniedResponse>?) -> Unit)? = null, onPermissionGranted: (List<PermissionGrantedResponse>?) -> Unit) {
    requireActivity().runWithPermissionsChecker(permissions,onPermissionDeny,onPermissionGranted)

}