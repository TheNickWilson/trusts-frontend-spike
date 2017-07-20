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

import uk.gov.hmrc.residencenilratebandcalculator.models.RadioOption

object Constants {
  val companyDetailsId = "CompanyDetails"
  val companySettlorId = "CompanySettlor"
  val individualDetailsId = "IndividualDetails"
  val individualSettlorId = "IndividualSettlor"
  val typeOfSettlorId = "TypeOfSettlor"
  val typeOfTrustId = "TypeOfTrust"

  val company = "company"
  val individual = "individual"

  val companyOrIndividualOptions = Seq(
    RadioOption("type_of_trust", company),
    RadioOption("type_of_trust", individual)
  )

  val trading = "trading"
  val investment = "investment"

  val typeOfCompanyOptions = Seq(
    RadioOption("type_of_company", trading),
    RadioOption("type_of_company", investment)
  )

  val no = "No"
  val yes = "Yes"
}
