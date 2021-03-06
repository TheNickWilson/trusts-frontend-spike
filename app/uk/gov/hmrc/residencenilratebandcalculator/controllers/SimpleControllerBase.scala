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


import play.api.data.{Form, FormError}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc._
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.play.frontend.controller.FrontendController
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.residencenilratebandcalculator.connectors.SessionConnector
import uk.gov.hmrc.residencenilratebandcalculator.models.UserAnswers
import uk.gov.hmrc.residencenilratebandcalculator.{FrontendAppConfig, Navigator}

import scala.concurrent.Future

trait ControllerBase[A] extends FrontendController with I18nSupport {
  def onPageLoad(implicit rds: Reads[A]): Action[AnyContent]

  def onSubmit(implicit wts: Writes[A]): Action[AnyContent]
}

trait SimpleControllerBase[A] extends ControllerBase[A] {

  val appConfig: FrontendAppConfig

  val controllerId: String

  def sessionConnector: SessionConnector

  def form: () => Form[A]

  def view(form: Option[Form[A]], userAnswers: UserAnswers)(implicit request: Request[_]): HtmlFormat.Appendable

  val navigator: Navigator

  override def onPageLoad(implicit rds: Reads[A]): Action[AnyContent] = Action.async { implicit request =>
    sessionConnector.fetch().map {
      case None => Redirect(uk.gov.hmrc.residencenilratebandcalculator.controllers.routes.SessionExpiredController.onPageLoad())
      case Some(cacheMap) =>
        val userAnswers = new UserAnswers(cacheMap)
        Ok(view(cacheMap.getEntry(controllerId).map(value => form().fill(value)), userAnswers))
    }
  }

  def onSubmit(implicit wts: Writes[A]) = Action.async { implicit request =>

    sessionConnector.fetch().flatMap {
      case None => Future.successful(Redirect(uk.gov.hmrc.residencenilratebandcalculator.controllers.routes.SessionExpiredController.onPageLoad()))
      case Some(cacheMap) =>
        val userAnswers = new UserAnswers(cacheMap)
        val boundForm = form().bindFromRequest()
        boundForm.fold(
          (formWithErrors: Form[A]) => {
            println(formWithErrors)
            Future.successful(BadRequest(view(Some(formWithErrors), userAnswers)))},
          (value) => validate(value, userAnswers) match {
            case Some(error) =>
              Future.successful(BadRequest(view(Some(form().fill(value).withError(error)), userAnswers)))
            case None =>
              sessionConnector.cache[A](controllerId, value).map(cacheMap =>
                Redirect(navigator.nextPage(controllerId)(new UserAnswers(cacheMap))))
          }
        )
    }
  }

  def validate(value: A, userAnswers: UserAnswers)(implicit hc: HeaderCarrier): Option[FormError] = None
}
