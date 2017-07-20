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

import javax.inject.{Inject, Singleton}

import play.api.mvc.Call
import uk.gov.hmrc.residencenilratebandcalculator.controllers.routes._
import uk.gov.hmrc.residencenilratebandcalculator.models.UserAnswers


@Singleton
class Navigator @Inject()() {
  private val routeMap: Map[String, UserAnswers => Call] = {

    Map(
      Constants.typeOfTrustId -> (ua => getTypeOfTrustRoute(ua)),
      Constants.companyDetailsId -> (ua => TypeOfSettlorController.onPageLoad()),
      Constants.individualDetailsId -> (ua => TypeOfSettlorController.onPageLoad()),
      Constants.typeOfSettlorId -> (ua => getTypeOfSettlorRoute(ua))
    )
  }

  private def getTypeOfTrustRoute(userAnswers: UserAnswers): Call = userAnswers.typeOfTrust match {
    case Some(Constants.company) => CompanyDetailsController.onPageLoad()
    case Some(Constants.individual) => IndividualDetailsController.onPageLoad()
    case _ => PageNotFoundController.onPageLoad() // Probably want to go to a start page instead
  }

  private def getTypeOfSettlorRoute(userAnswers: UserAnswers): Call = userAnswers.typeOfSettlor match {
    case Some(Constants.company) => CompanySettlorController.onPageLoad()
    case Some(Constants.individual) => IndividualSettlorController.onPageLoad()
    case _ => PageNotFoundController.onPageLoad() // Probably want to go to a start page instead
  }

  def nextPage(controllerId: String): UserAnswers => Call = {
    routeMap.getOrElse(controllerId, _ => PageNotFoundController.onPageLoad())
  }

  private def goToPageNotFound: UserAnswers => Call = _ => PageNotFoundController.onPageLoad()
}
