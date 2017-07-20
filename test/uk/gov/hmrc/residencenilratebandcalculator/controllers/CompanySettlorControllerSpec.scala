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

package uk.gov.hmrc.residencenilratebandcalculator.controllers

import play.api.libs.json.{JsObject, JsValue, Json}
import uk.gov.hmrc.residencenilratebandcalculator.Constants
import uk.gov.hmrc.residencenilratebandcalculator.forms.CompanySettlorForm
import uk.gov.hmrc.residencenilratebandcalculator.models.CompanySettlor
import uk.gov.hmrc.residencenilratebandcalculator.views.html.company_settlor

class CompanySettlorControllerSpec extends SimpleControllerSpecBase {

  "Company settlor controller" must {
    def createView = (value: Option[CompanySettlor]) => {
      value match {
        case None => company_settlor(frontendAppConfig)(fakeRequest, messages)
        case Some(v) => company_settlor(frontendAppConfig, Some(CompanySettlorForm().fill(v)))(fakeRequest, messages)
      }
    }

    def createController = () => new CompanySettlorController(frontendAppConfig, messagesApi, mockSessionConnector, navigator)

    val testValue = CompanySettlor("name", true, Constants.trading, true)

    val invalidData: JsValue = Json.toJson(testValue).as[JsObject] - "name"

    val valuesToCache = Map(Constants.typeOfTrustId -> Constants.company)

    val cacheKey = Constants.companySettlorId

    behave like multiQuestionController[CompanySettlor](createController, createView, Constants.companySettlorId, testValue,
      invalidData)(CompanySettlor.reads, CompanySettlor.writes)
  }
}
