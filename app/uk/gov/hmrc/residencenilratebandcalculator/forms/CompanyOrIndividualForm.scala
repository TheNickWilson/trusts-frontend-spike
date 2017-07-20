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

import play.api.data.format.Formatter
import FormValidators.isValidCompanyOrIndividualOption
import play.api.data.Form
import play.api.data.Forms._

object CompanyOrIndividualForm extends FormErrorHelper {

  def typeOfTrustFormatter = new Formatter[String] {
    def bind(key: String, data: Map[String, String]) = data.get(key) match {
      case Some(s) if isValidCompanyOrIndividualOption(s) => Right(s)
      case _ => produceError(key, "error.invalid_company_or_individual_option")
    }

    def unbind(key: String, value: String) = Map(key -> value)
  }

  def apply(): Form[String] =
    Form(single("value" -> of(typeOfTrustFormatter)))
}
