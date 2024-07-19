/*
 * Copyright 2019 http4s.org
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

package org.http4s.ember.client

import cats.effect.Async
import fs2.io.net.unixsocket.UnixSockets
import org.typelevel.log4cats.slf4j.Slf4jLogger
import logger.LoggerKernel

private[client] trait EmberClientBuilderPlatform {

  private[client] def defaultUnixSockets[F[_]: Async]: Option[UnixSockets[F]] =
    Some(UnixSockets.forAsync)

}

private[client] trait EmberClientBuilderCompanionPlatform {

  private[client] def defaultLogger[F[_]: Async]: LoggerKernel[F] =
    logger.interoplog4cats.log4catsBackend(Slf4jLogger.getLogger[F])

}
