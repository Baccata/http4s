/*
 * Copyright 2014 http4s.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.http4s.client

import cats.effect.Async
import cats.implicits._
import io.netty.channel.{Channel, ChannelFuture}

package object scaffold {

  implicit class NettySyntax[F[_]](val fcf: F[ChannelFuture]) extends AnyVal {
    def liftToF(implicit F: Async[F]): F[Unit] =
      liftToFWithChannel.void

    def liftToFWithChannel(implicit F: Async[F]): F[Channel] =
      F.async((callback: Either[Throwable, Channel] => Unit) =>
        fcf.flatMap(cf =>
          F.delay {
            cf.addListener { (f: ChannelFuture) =>
              if (f.isSuccess) callback(Right(f.channel()))
              else callback(Left(f.cause()))
            }
            Some(F.delay(cf.cancel(true)))
          }))
  }
}
