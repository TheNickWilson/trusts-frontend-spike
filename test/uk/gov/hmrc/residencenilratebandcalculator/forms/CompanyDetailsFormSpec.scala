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

import uk.gov.hmrc.residencenilratebandcalculator.models.CompanyDetails

class CompanyDetailsFormSpec extends FormSpec {

  "Company details form" must {
    "bind valid values correctly when email is omitted" in {
      val data: Map[String, String] = Map("name" -> "name", "ukCompany" -> "true")
      val form = CompanyDetailsForm().bind(data)
      form.get shouldBe CompanyDetails("name", true, None)
    }

    "bind valid values correctly when email is provided" in {
      val data: Map[String, String] = Map("name" -> "name", "ukCompany" -> "true", "contactEmail" -> "email")
      val form = CompanyDetailsForm().bind(data)
      form.get shouldBe CompanyDetails("name", true, Some("email"))
    }

    "fail to bind when name is omitted" in {
      val expectedError = error("name", "error.required")
      val data: Map[String, String] = Map("ukCompany" -> "true")
      checkForError(CompanyDetailsForm(), data, expectedError)
    }

    "fail to bind when name is blank" in {
      val expectedError = error("name", "error.required")
      val data: Map[String, String] = Map("name" -> "", "ukCompany" -> "true")
      checkForError(CompanyDetailsForm(), data, expectedError)
    }

    "fail to bind when ukCompany is omitted" in {
      val expectedError = error("ukCompany", "error.boolean")
      val data: Map[String, String] = Map("name" -> "name")
      checkForError(CompanyDetailsForm(), data, expectedError)
    }

    "fail to bind when ukCompany is blank" in {
      val expectedError = error("ukCompany", "error.boolean")
      val data: Map[String, String] = Map("name" -> "name", "ukCompany" -> "")
      checkForError(CompanyDetailsForm(), data, expectedError)
    }
  }
}
