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

import javax.inject.Singleton

import play.api.libs.json._
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.residencenilratebandcalculator.Constants

@Singleton
class CascadeUpsert {

  def apply[A](key: String, value: A, originalCacheMap: CacheMap)(implicit wts: Writes[A]): CacheMap =
    funcMap.get(key).fold(store(key, value, originalCacheMap)) { fn => fn(Json.toJson(value), originalCacheMap)}

  val funcMap: Map[String, (JsValue, CacheMap) => CacheMap] =
    Map(
      Constants.typeOfTrustId -> ((v, cm) => cleanupTypeOfTrust(v, cm)),
      Constants.typeOfSettlorId -> ((v, cm) => cleanupTypeOfSettlor(v, cm))
    )

  private def store[A](key:String, value: A, cacheMap: CacheMap)(implicit wrts: Writes[A]) =
    cacheMap copy (data = cacheMap.data + (key -> Json.toJson(value)))

  private def clearIfFalse[A](key: String, value: A, keysToRemove: Set[String], cacheMap: CacheMap)(implicit wrts: Writes[A]): CacheMap = {
    val mapToStore = value match {
      case JsBoolean(false) => cacheMap copy (data = cacheMap.data.filterKeys(s => !keysToRemove.contains(s)))
      case _ => cacheMap
    }
    store(key, value, mapToStore)
  }

  private def cleanupTypeOfTrust[A](value: A, cacheMap: CacheMap)(implicit wrts: Writes[A]): CacheMap = {
    val mapToStore = value match {
      case JsString(Constants.company) => cacheMap copy (data = cacheMap.data.filterKeys(s => s != Constants.individualDetailsId))
      case JsString(Constants.individual) => cacheMap copy (data = cacheMap.data.filterKeys(s => s != Constants.companyDetailsId))
      case _ => cacheMap
    }
    store(Constants.typeOfTrustId, value, mapToStore)
  }

  private def cleanupTypeOfSettlor[A](value: A, cacheMap: CacheMap)(implicit wrts: Writes[A]): CacheMap = {
    val mapToStore = value match {
      case JsString(Constants.company) => cacheMap copy (data = cacheMap.data.filterKeys(s => s != Constants.individualSettlorId))
      case JsString(Constants.individual) => cacheMap copy (data = cacheMap.data.filterKeys(s => s != Constants.companySettlorId))
      case _ => cacheMap
    }
    store(Constants.typeOfSettlorId, value, mapToStore)
  }
}
