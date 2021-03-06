/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.residencenilratebandcalculator.connectors

import play.api.libs.json._
import play.api.Logger
import javax.inject.{Inject, Singleton}

import com.google.inject.ImplementedBy
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.residencenilratebandcalculator.repositories.SessionRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class RealSessionConnector @Inject()(val sessionRepository: SessionRepository, val cascadeUpsert: CascadeUpsert) extends SessionConnector {

  def cache[A](key: String, value: A)(implicit wts: Writes[A], hc: HeaderCarrier): Future[CacheMap] = {
    hc.sessionId match {
      case None =>
       val msg = "Unable to find session with id " + hc.sessionId + "while caching " + key + " = " + value
       Logger.error(msg)
       throw new RuntimeException(msg)
      case Some(id) =>
        sessionRepository().get(id.toString).flatMap { optionalCacheMap =>
          val updatedCacheMap = cascadeUpsert(key, value, optionalCacheMap.getOrElse(new CacheMap(id.toString, Map())))
          sessionRepository().upsert(updatedCacheMap).map {_ => updatedCacheMap}
        }
    }
  }

  def delete(key: String)(implicit hc: HeaderCarrier): Future[Boolean] = {
    hc.sessionId match {
      case None => Future(false)
      case Some(id) =>
        sessionRepository().get(id.toString).flatMap { optionalCacheMap =>
          optionalCacheMap.fold(Future(false)) { cm =>
            val newCacheMap: CacheMap = cm copy (data = cm.data - key)
            sessionRepository().upsert(newCacheMap)
          }
        }
    }
  }

  def fetch()(implicit hc: HeaderCarrier): Future[Option[CacheMap]] = {
    hc.sessionId match {
      case None => Future.successful(None)
      case Some(id) => sessionRepository().get(id.toString)
    }
  }

  def fetchAndGetEntry[A](key: String)(implicit hc: HeaderCarrier, rds: Reads[A]): Future[Option[A]] = {
    val futureOptionCacheMap = fetch()
    futureOptionCacheMap.map {optionalCacheMap =>
      optionalCacheMap.flatMap { cm =>
        cm.getEntry(key)
      }
    }
  }
}

@ImplementedBy(classOf[RealSessionConnector])
trait SessionConnector {
  def cache[A](key: String, value: A)(implicit wts: Writes[A], hc: HeaderCarrier): Future[CacheMap]

  def delete(key: String)(implicit hc: HeaderCarrier): Future[Boolean]

  def fetch()(implicit hc: HeaderCarrier): Future[Option[CacheMap]]

  def fetchAndGetEntry[A](key: String)(implicit hc: HeaderCarrier, rds: Reads[A]): Future[Option[A]]
}