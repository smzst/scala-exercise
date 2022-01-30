package sangria.data

/*
  GraphQL syntax:
    type Product implements Identifiable {
      id: String!
      name: String!
      description: String
      picture(size: Int!): Picture
    }
 */
case class Product(id: String, name: String, description: String) extends Identifiable {
  def picture(size: Int): Picture =
    Picture(width = size, height = size, url = Some(s"//cdn.com/$size/$id.jpg"))
}
