package br.com.chamaapp.driver.extensions

import android.content.Context
import android.media.MediaPlayer

fun Context.playSound(resId: Int) {
  MediaPlayer.create(this, resId).run {
   start()
  }
}