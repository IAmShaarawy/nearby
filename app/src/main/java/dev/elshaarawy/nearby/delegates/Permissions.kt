package dev.elshaarawy.nearby.delegates

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
class PermissionsDelegate private constructor(
    private val requestCode: Int,
    private vararg val permissions: String
) {
    private lateinit var instance: PermissionsManager

    companion object {
        operator fun invoke(requestCode: Int, vararg permissions: String): PermissionsDelegate {
            return PermissionsDelegate(requestCode, *permissions)
        }
    }

    operator fun getValue(thisRef: Any, property: KProperty<*>): PermissionsManager {
        return if (::instance.isInitialized) instance else
            when (thisRef) {
                is Fragment -> FragmentPermissionsManager(
                    WeakReference(thisRef),
                    requestCode,
                    *permissions
                )
                is AppCompatActivity -> AppCompatActivityPermissionsManager(
                    WeakReference(thisRef), requestCode, *permissions
                )
                else -> throw TypeCastException("Can't cast $thisRef to any thing because it is not supported")
            }.also { instance = it }
    }
}

abstract class PermissionsManager(
    protected val requestCode: Int,
    private vararg val permissions: String
) {

    protected lateinit var onAllGranted: (() -> Unit)
        @Synchronized get
        @Synchronized set

    fun Context.guaranteePermissions(): Pair<Boolean, List<String>> {
        val unGrantedPermissions = mutableListOf<String>()

        permissions.forEach {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val isGranted = ActivityCompat.checkSelfPermission(this, it) ==
                        PackageManager.PERMISSION_GRANTED
                if (!isGranted)
                    unGrantedPermissions.add(it)
            }
        }

        val isAllGranted = unGrantedPermissions.isEmpty()

        return isAllGranted to unGrantedPermissions
    }

    abstract fun withPermission(
        onRequestPermissions: ((List<String>, () -> Unit) -> Unit)? = null,
        onAllGranted: () -> Unit
    )

    abstract fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        onAllDenied: (List<String>) -> Unit
    )
}

private class FragmentPermissionsManager(
    private val fragment: WeakReference<Fragment>,
    requestCode: Int,
    vararg permissions: String
) : PermissionsManager(requestCode, *permissions) {

    override fun withPermission(
        onRequestPermissions: ((List<String>, () -> Unit) -> Unit)?,
        onAllGranted: () -> Unit
    ) {
        this.onAllGranted = onAllGranted
        fragment.get()?.context?.run {
            val (isGranted, unGrantedPermissions) = guaranteePermissions()

            if (isGranted) {
                onAllGranted()
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                onRequestPermissions?.invoke(unGrantedPermissions) {
                    fragment.get()
                        ?.requestPermissions(unGrantedPermissions.toTypedArray(), requestCode)
                } ?: fragment.get()?.requestPermissions(
                    unGrantedPermissions.toTypedArray(),
                    requestCode
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        onAllDenied: (List<String>) -> Unit
    ) {
        if (grantResults.isEmpty())
            return
        fragment.get()?.context?.also {
            val (isGranted, unGrantedPermissions) = it.guaranteePermissions()
            if (requestCode == this.requestCode && isGranted) {
                onAllGranted()
            } else if (requestCode == this.requestCode) {
                onAllDenied(unGrantedPermissions)
            }
        }
    }
}

private class AppCompatActivityPermissionsManager(
    private val activity: WeakReference<AppCompatActivity>,
    requestCode: Int,
    vararg permissions: String
) : PermissionsManager(requestCode, *permissions) {
    override fun withPermission(
        onRequestPermissions: ((List<String>, () -> Unit) -> Unit)?,
        onAllGranted: () -> Unit
    ) {
        this.onAllGranted = onAllGranted
        activity.get()?.run {
            val (isGranted, unGrantedPermissions) = guaranteePermissions()

            if (isGranted) {
                onAllGranted()
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                onRequestPermissions?.invoke(unGrantedPermissions) {
                    requestPermissions(unGrantedPermissions.toTypedArray(), requestCode)
                } ?: requestPermissions(unGrantedPermissions.toTypedArray(), requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        onAllDenied: (List<String>) -> Unit
    ) {
        if (grantResults.isEmpty())
            return
        activity.get()?.let {
            val (isGranted, unGrantedPermissions) = it.guaranteePermissions()
            if (requestCode == this.requestCode && isGranted) {
                onAllGranted()
            } else if (requestCode == this.requestCode) {
                onAllDenied(unGrantedPermissions)
            }
        }
    }
}
