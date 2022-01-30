package sangria

import sangria.macros.derive.*
import sangria.schema.{fields, Argument, Field, InterfaceType, ListType, ObjectType, OptionType, Schema, StringType}

object SchemaDefinition {
  /*
    これだと、フィールドが変わるたびに手で直す必要がある。マクロを使って簡略化できる
      val PictureType = ObjectType(
        "Picture", // name
        "The product picture", // description
        fields[Unit, Picture]( // describe thr Picture type
          Field("width", IntType, resolve = _.value.width),
          Field("height", IntType, resolve = _.value.height),
          Field("url", OptionType(StringType), description = Some("Picture CDN URL"), resolve = _.value.url)
        )
      )
   */
  implicit val PictureType: ObjectType[Unit, Picture] = deriveObjectType[Unit, Picture](
    ObjectTypeDescription("The product picture"),
    DocumentField("url", "Picture CDN URL")
  )

  val IdentifiableType: InterfaceType[Unit, Identifiable] = InterfaceType(
    "Identifiable",
    "Entity that can be identified",
    fields[Unit, Identifiable](
      Field("id", StringType, resolve = _.value.id)
    )
  )

  /*
    デフォルトでは、マクロは case class のフィールドのみを考慮する。以下はマクロに `picture()` を含めるように要求している
    また、デフォルトでは実現しないため実装されたインターフェイスを定義している
   */
  val ProductType: ObjectType[Unit, Product] = deriveObjectType[Unit, Product](
    Interfaces(IdentifiableType),
    IncludeMethods("picture")
  )

  val Id: Argument[String] = Argument("id", StringType)

  /*
    GraphQL のエントリーポイントにあたる
    スキーマ内のすべての GraphQL type field で利用可能なユーザーコンテキストオブジェクトを提供することができる。これは型引数の最初に指定する

    GraphQL syntax:
      type Query {
        product(id: Int!): Product
        products: [Product]
      }
   */
  val QueryType: ObjectType[ProductRepo, Unit] = ObjectType(
    "Query",
    fields[ProductRepo, Unit](
      Field(
        "product",
        OptionType(ProductType),
        description = Some("Returns a product with specific `id`."),
        arguments = Id :: Nil,
        resolve = c => c.ctx.product(c.arg(Id))
      ),
      Field(
        "products",
        ListType(ProductType),
        description = Some("Returns a list of all available products."),
        resolve = _.ctx.products
      )
    )
  )

  val schema: Schema[ProductRepo, Unit] = Schema(QueryType)

}
