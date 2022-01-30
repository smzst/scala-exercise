package sangria

import io.circe.Json
import org.scalatest.flatspec.AnyFlatSpec
import sangria.SchemaDefinition.schema
import sangria.execution.*
import sangria.macros.*
import sangria.marshalling.circe.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.*
import scala.concurrent.{Await, Future}

class Test extends AnyFlatSpec {
  val query =
    graphql"""
      query MyProduct {
        product(id: "2") {
          name
          description

          picture(size: 500) {
            width, height, url
          }
        }

        products {
          name
        }
      }
    """

  val result: Future[Json] =
    Executor.execute(schema, query, new ProductRepo)

  // とりあえず動けばいいので手抜き
  Await.result(result.map(println), 2.seconds)
}
