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

class CompanyOrIndividualFormSpec extends FormSpec {

  "Company or Individual form" must {
    "bind the value 'company'" in {
      val form = CompanyOrIndividualForm().bind(Map("value" -> "company"))
      form.get shouldBe "company"
    }

    "bind the value 'individual'" in {
      val form = CompanyOrIndividualForm().bind(Map("value" -> "individual"))
      form.get shouldBe "individual"
    }

    "fail to bind an invalid value" in {
      val expectedError = error("value", "error.invalid_company_or_individual_option")
      checkForError(CompanyOrIndividualForm(), Map("value" -> "invalid"), expectedError)
    }

    "fail to bind a blank value" in {
      val expectedError = error("value", "error.invalid_company_or_individual_option")
      checkForError(CompanyOrIndividualForm(), Map("value" -> ""), expectedError)
    }

    "fail to bind when value is omitted" in {
      val expectedError = error("value", "error.invalid_company_or_individual_option")
      checkForError(CompanyOrIndividualForm(), emptyForm, expectedError)
    }
  }
}
