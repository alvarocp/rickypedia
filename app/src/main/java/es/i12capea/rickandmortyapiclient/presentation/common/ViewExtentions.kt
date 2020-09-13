package es.i12capea.rickandmortyapiclient.presentation.common

import android.app.Activity
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import es.i12capea.rickandmortyapiclient.R


fun Activity.displayToast(@StringRes message:Int){
    Toast.makeText(this, message,Toast.LENGTH_LONG).show()
}

fun Fragment.displayToast(@StringRes message:Int){
    Toast.makeText(this.context, message,Toast.LENGTH_LONG).show()
}

fun Activity.displayToast(message:String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

fun Fragment.displayToast(message:String){
    Toast.makeText(this.context,message,Toast.LENGTH_LONG).show()
}

fun Activity.displaySuccessDialog(message: String?){
    MaterialDialog(this)
        .show{
            title(R.string.text_success)
            message(text = message)
            positiveButton(R.string.text_ok)
        }
}

fun Activity.displayErrorDialog(errorMessage: String?){
    MaterialDialog(this)
        .show{
            title(R.string.text_error)
            message(text = errorMessage)
            positiveButton(R.string.text_ok)
        }
}

fun Fragment.displayErrorDialog(errorMessage: String?){
    MaterialDialog(this.requireContext())
        .show{
            title(R.string.text_error)
            message(text = errorMessage)
            positiveButton(R.string.text_ok)
        }
}

fun Activity.displayInfoDialog(message: String?){
    MaterialDialog(this)
        .show{
            title(R.string.text_info)
            message(text = message)
            positiveButton(R.string.text_ok)
        }
}

fun Fragment.displayInfoDialog(message: String?){
    MaterialDialog(this.requireContext())
        .show{
            title(R.string.text_info)
            message(text = message)
            positiveButton(R.string.text_ok)
        }
}

fun Activity.areYouSureDialog(message: String, callback: AreYouSureCallback){
    MaterialDialog(this)
        .show{
            title(R.string.are_you_sure)
            message(text = message)
            negativeButton(R.string.text_cancel){
                callback.cancel()
            }
            positiveButton(R.string.text_yes){
                callback.proceed()
            }
        }
}


fun Fragment.areYouSureDialog(message: String, callback: AreYouSureCallback){
    MaterialDialog(this.requireContext())
        .show{
            title(R.string.are_you_sure)
            message(text = message)
            negativeButton(R.string.text_cancel){
                callback.cancel()
            }
            positiveButton(R.string.text_yes){
                callback.proceed()
            }
        }
}
interface AreYouSureCallback {
    fun proceed()

    fun cancel()
}









