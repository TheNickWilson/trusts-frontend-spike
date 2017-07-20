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
import uk.gov.hmrc.residencenilratebandcalculator.forms.IndividualDetailsForm
import uk.gov.hmrc.residencenilratebandcalculator.models.{Date, IndividualDetails}
import uk.gov.hmrc.residencenilratebandcalculator.views.html.individual_details

class IndividualDetailsControllerSpec extends SimpleControllerSpecBase {

  "Individual details controller" must {
    def createView = (value: Option[IndividualDetails]) => {
      value match {
        case None => individual_details(frontendAppConfig)(fakeRequest, messages)
        case Some(v) => individual_details(frontendAppConfig, Some(IndividualDetailsForm().fill(v)))(fakeRequest, messages)
      }
    }

    def createController = () => new IndividualDetailsController(frontendAppConfig, messagesApi, mockSessionConnector, navigator)

    val testValue = IndividualDetails("first name", None, "last name", Date(1, 1, 1980), false, None, "01234 567890", None)

    val invalidData: JsValue = Json.toJson(testValue).as[JsObject] - "firstName"

    val valuesToCache = Map(Constants.typeOfTrustId -> Constants.individual)

    val cacheKey = Constants.individualDetailsId

    behave like multiQuestionController[IndividualDetails](createController, createView, Constants.individualDetailsId, testValue,
      invalidData)(IndividualDetails.reads, IndividualDetails.writes)
  }
}
