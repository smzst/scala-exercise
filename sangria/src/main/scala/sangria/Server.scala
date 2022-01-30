package sangria

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCode
import akka.http.scaladsl.model.StatusCodes.*
import akka.http.scaladsl.server.*
import akka.http.scaladsl.server.Directives.*
import sangria.ast.Document
import sangria.data.ProductRepo
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.marshalling.sprayJson.*
import sangria.parser.QueryParser
import spray.json.{JsObject, JsString, JsValue}

import scala.concurrent.Future
import scala.util.{Failure, Success}

object Server extends App with SprayJsonSupport {
  implicit val system = ActorSystem("sangria-server")
  import system.dispatcher

  val route: Route =
    (post & path("graphql")) {
      entity(as[JsValue]) { requestJson =>
        graphQLEndpoint(requestJson)
      }
    } ~
      get {
        complete(OK)
      }

  Http().newServerAt("0.0.0.0", 8080).bind(route)

  private def graphQLEndpoint(requestJson: JsValue): Route = {
    val JsObject(fields) = requestJson
    val JsString(query) = fields("query")
    val operation = fields.get("operationName") collect { case JsString(op) =>
      op
    }
    val vars = fields.get("variables") match {
      case Some(obj: JsObject) => obj
      case _                   => JsObject.empty
    }

    QueryParser.parse(query) match {
      case Success(queryAst) =>
        complete(executeGraphQLQuery(queryAst, operation, vars))
      case Failure(error) =>
        complete(BadRequest, JsObject("error" -> JsString(error.getMessage)))
    }

  }

  private def executeGraphQLQuery(
      query: Document,
      op: Option[String],
      vars: JsObject
  ): Future[(StatusCode, JsValue)] =
    Executor
      .execute(SchemaDefinition.schema, query, new ProductRepo, variables = vars, operationName = op)
      .map(OK -> _)
      .recover {
        case error: QueryAnalysisError => BadRequest -> error.resolveError
        case error: ErrorWithResolver  => InternalServerError -> error.resolveError
      }
}
