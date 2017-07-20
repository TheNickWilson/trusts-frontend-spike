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
import uk.gov.hmrc.residencenilratebandcalculator.forms.IndividualSettlorForm
import uk.gov.hmrc.residencenilratebandcalculator.models.{Date, IndividualSettlor}
import uk.gov.hmrc.residencenilratebandcalculator.views.html.individual_settlor

class IndividualSettlorControllerSpec extends SimpleControllerSpecBase {

  "Individual settlor controller" must {
    def createView = (value: Option[IndividualSettlor]) => {
      value match {
        case None => individual_settlor(frontendAppConfig)(fakeRequest, messages)
        case Some(v) => individual_settlor(frontendAppConfig, Some(IndividualSettlorForm().fill(v)))(fakeRequest, messages)
      }
    }

    def createController = () => new IndividualSettlorController(frontendAppConfig, messagesApi, mockSessionConnector, navigator)

    val testValue = IndividualSettlor("first name", None, "last name", Date(1, 2, 2000), false, None, "01234 567890")

    val invalidData: JsValue = Json.toJson(testValue).as[JsObject] - "firstName"

    val valuesToCache = Map(Constants.typeOfTrustId -> Constants.individual)

    val cacheKey = Constants.individualSettlorId

    behave like multiQuestionController[IndividualSettlor](createController, createView, Constants.individualSettlorId, testValue,
      invalidData)(IndividualSettlor.reads, IndividualSettlor.writes)
  }
}
