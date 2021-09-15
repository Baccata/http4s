/*
 * Copyright 2013 http4s.org
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

package org.http4s
package headers

import cats.data.NonEmptyList
import cats.syntax.all._
import org.http4s.parser.HttpHeaderParser
import org.http4s.syntax.all._
import org.http4s.util.{CaseInsensitiveString, Writer}

// values should be case insensitive
//http://stackoverflow.com/questions/10953635/are-the-http-connection-header-values-case-sensitive
object Connection extends HeaderKey.Internal[Connection] with HeaderKey.Recurring {
  override def parse(s: String): ParseResult[Connection] =
    HttpHeaderParser.CONNECTION(s)
}

final case class Connection(values: NonEmptyList[CaseInsensitiveString]) extends Header.Recurring {
  override def key: Connection.type = Connection
  type Value = CaseInsensitiveString
  def hasClose: Boolean = values.contains_("close".ci)
  def hasKeepAlive: Boolean = values.contains_("keep-alive".ci)
  override def renderValue(writer: Writer): writer.type =
    writer.addStringNel(values.map(_.toString), ", ")

  override def isNameValid: Boolean = true
}
