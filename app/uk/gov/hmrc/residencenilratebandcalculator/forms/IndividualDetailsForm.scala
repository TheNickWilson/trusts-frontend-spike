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

import play.api.data.Form
import play.api.data.Forms._
import uk.gov.hmrc.residencenilratebandcalculator.forms.DateForm.{datePartFormatter, upperBoundDays, upperBoundMonths, upperBoundYears}
import uk.gov.hmrc.residencenilratebandcalculator.forms.FormValidators.isValidDate
import uk.gov.hmrc.residencenilratebandcalculator.models.{Date, IndividualDetails}

object IndividualDetailsForm extends WithRequiredBooleanMapping {

  def apply(): Form[IndividualDetails] = Form(
    mapping(
      "firstName" -> nonEmptyText,
      "middleNames" -> optional(text),
      "lastName" -> nonEmptyText,
      "dateOfBirth" -> mapping(
        "day" -> of(datePartFormatter("error.date.day_blank", "error.date.day_invalid", upperBoundDays)),
        "month" -> of(datePartFormatter("error.date.month_blank", "error.date.month_invalid", upperBoundMonths)),
        "year" -> of(datePartFormatter("error.date.year_blank", "error.date.year_invalid", upperBoundYears))
      )(Date.apply)(Date.unapply)
        .verifying("error.invalid_date", fields => isValidDate(fields.day, fields.month, fields.year)),
      "hasNino" -> of[Boolean](requiredBooleanFormatter),
      "nino" -> optional(text),
      "phoneNumber" -> nonEmptyText,
      "contactEmail" -> optional(text)
    )(IndividualDetails.apply)(IndividualDetails.unapply)
  )
}
