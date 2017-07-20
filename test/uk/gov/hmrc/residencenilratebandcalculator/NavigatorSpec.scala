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

package uk.gov.hmrc.residencenilratebandcalculator

import org.joda.time.LocalDate
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.Matchers
import org.scalatest.mock.MockitoSugar
import play.api.libs.json.JsString
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}
import uk.gov.hmrc.residencenilratebandcalculator.controllers.routes
import uk.gov.hmrc.residencenilratebandcalculator.models.UserAnswers

class NavigatorSpec extends UnitSpec with MockitoSugar with Matchers with WithFakeApplication {
  val navigator = new Navigator

  "Navigator" must {
    "go to Company Settlor from Type Of Settlor when the type of settlor is Company" in {
      val cacheMap = CacheMap("a", Map(Constants.typeOfSettlorId -> JsString(Constants.company)))
      val userAnswers = new UserAnswers(cacheMap)
      navigator.nextPage(Constants.typeOfSettlorId)(userAnswers) shouldBe routes.CompanySettlorController.onPageLoad
    }

    "go to Individual Settlor from Type Of Settlor when the type of settlor is Individual" in {
      val cacheMap = CacheMap("a", Map(Constants.typeOfSettlorId -> JsString(Constants.individual)))
      val userAnswers = new UserAnswers(cacheMap)
      navigator.nextPage(Constants.typeOfSettlorId)(userAnswers) shouldBe routes.IndividualSettlorController.onPageLoad
    }
  }
}
