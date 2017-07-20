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

import play.api.libs.json._
import uk.gov.hmrc.residencenilratebandcalculator.Constants
import uk.gov.hmrc.residencenilratebandcalculator.forms.CompanyOrIndividualForm
import uk.gov.hmrc.residencenilratebandcalculator.views.html.type_of_trust

class TypeOfTrustControllerSpec extends SimpleControllerSpecBase {

  "Type of trust controller" must {
    def createView = (value: Option[String]) => {
      value match {
        case None => type_of_trust(frontendAppConfig)(fakeRequest, messages)
        case Some(v) => type_of_trust(frontendAppConfig, Some(CompanyOrIndividualForm().fill(v)))(fakeRequest, messages)
      }
    }

    def createController = () => new TypeOfTrustController(frontendAppConfig, messagesApi, mockSessionConnector, navigator)

    val testValue = Constants.company

    val invalidData = Json.parse("""{"value" : "invalid data"}""")

    behave like singleQuestionController[String](createController, createView, Constants.typeOfTrustId, testValue, invalidData)(Reads.StringReads, Writes.StringWrites)
  }
}
