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

package uk.gov.hmrc.residencenilratebandcalculator.forms

import uk.gov.hmrc.residencenilratebandcalculator.models.{Date, IndividualSettlor}

class IndividualSettlorFormSpec extends FormBehaviours {

  val validData: Map[String, String] = Map(
    "firstName" -> "first name",
    "middleNames" -> "middle names",
    "lastName" -> "last name",
    "dateOfBirth.day" -> "1",
    "dateOfBirth.month" -> "2",
    "dateOfBirth.year" -> "2000",
    "hasNino" -> "true",
    "nino" -> "AA111111A",
    "phoneNumber" -> "01234 567890"
  )

  val form = IndividualSettlorForm()

  "Individual Settlor form" must {

    behave like trustsForm[IndividualSettlor](IndividualSettlor("first name", Some("middle names"), "last name", Date(1, 2, 2000), true, Some("AA111111A"), "01234 567890"))

    behave like formWithMandatoryTextFields("firstName", "lastName", "phoneNumber")

    behave like formWithDateField("dateOfBirth")

    behave like formWithOptionalTextFields("middleNames")

    behave like formWithBooleans("hasNino")

    behave like formWithConditionallyMandatoryField("hasNino", "nino")
  }
}