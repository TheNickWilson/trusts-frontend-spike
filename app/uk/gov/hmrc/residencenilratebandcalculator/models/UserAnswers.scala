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

package uk.gov.hmrc.residencenilratebandcalculator.models

import org.joda.time.LocalDate
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.residencenilratebandcalculator.Constants

class UserAnswers(cacheMap: CacheMap) {
  def companyDetails: Option[CompanyDetails] = cacheMap.getEntry[CompanyDetails](Constants.companyDetailsId)

  def individualDetails: Option[IndividualDetails] = cacheMap.getEntry[IndividualDetails](Constants.individualDetailsId)

  def typeOfTrust: Option[String] = cacheMap.getEntry[String](Constants.typeOfTrustId)

  def typeOfSettlor: Option[String] = cacheMap.getEntry[String](Constants.typeOfSettlorId)
}
