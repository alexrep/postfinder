package com.aktorbackend

import java.util.concurrent.TimeUnit

import akka.util.Timeout

/**
 * Created by alex on 28.03.15.
 */
package object infrastructure {
  implicit val timeout = Timeout(500, TimeUnit.MILLISECONDS)


}
