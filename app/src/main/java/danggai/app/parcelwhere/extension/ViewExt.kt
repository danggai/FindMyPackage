package danggai.app.parcelwhere.extension

import android.text.InputFilter
import androidx.appcompat.widget.AppCompatEditText
import danggai.app.parcelwhere.Constant
import java.util.regex.Pattern


fun AppCompatEditText.onlyEngNum() {
    this.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
        val ps: Pattern = Pattern.compile(Constant.PATTERN_ENG_NUM_ONLY)
        return@InputFilter if (!ps.matcher(source).matches()) {
            ""
        } else {
            null
        }
    })
}

fun AppCompatEditText.onlyEngCap() {
    this.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
}