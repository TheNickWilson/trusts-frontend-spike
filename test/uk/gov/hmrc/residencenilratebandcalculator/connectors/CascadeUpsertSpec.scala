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

import play.api.libs.json.{JsString, Json}
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.residencenilratebandcalculator.Constants
import uk.gov.hmrc.residencenilratebandcalculator.models.{CompanySettlor, Date, IndividualSettlor}

class CascadeUpsertSpec extends UnitSpec {

  val cacheMapId = "id"

  "Cascade upsert" when {

    "asked to save the answer 'company' as the type of settlor" must {
      val individualSettlor = Json.toJson(IndividualSettlor("firstName", None, "lastName", Date(1, 2, 2000), false, None, "01234 567890"))
      val cacheMap = CacheMap(cacheMapId, Map(Constants.individualSettlorId -> individualSettlor))
      val updatedCacheMap = (new CascadeUpsert)(Constants.typeOfSettlorId, Constants.company, cacheMap)

      "delete any existing value for 'individual settlor'" in {
        updatedCacheMap.data.keys should not contain Constants.individualSettlorId
      }

      "save the type of settlor" in {
        updatedCacheMap.data(Constants.typeOfSettlorId) shouldBe JsString(Constants.company)
      }
    }

    "asked to save the answer 'individual' as the type of settlor" must {
      val companySettlor = Json.toJson(CompanySettlor("name", true, Constants.trading, true))
      val cacheMap = CacheMap(cacheMapId, Map(Constants.companySettlorId -> companySettlor))
      val updatedCacheMap = (new CascadeUpsert)(Constants.typeOfSettlorId, Constants.individual, cacheMap)

      "delete any existing value for 'company settlor'" in {
        updatedCacheMap.data.keys should not contain Constants.individualSettlorId
      }

      "save the type of settlor" in {
        updatedCacheMap.data(Constants.typeOfSettlorId) shouldBe JsString(Constants.individual)
      }
    }
  }
}
