package br.com.chamaapp.driver.presentation

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import br.com.chamaapp.driver.R

class StartStopFloatingActionButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0, attrs: AttributeSet?
) : FloatingActionButton(context, attrs, defStyleAttr) {

  var started = false

  init {
    setImageResource(R.drawable.ic_play_arrow_white_24dp)
  }

  fun setStatus(status: Boolean) {
    started = status
    when {
      status -> setImageResource(R.drawable.ic_stop_white_24dp)
      else -> setImageResource(R.drawable.ic_play_arrow_white_24dp)
    }
  }

}