package sangria.data

/*
  GraphQL syntax:
    type Picture {
      width: Int!
      height: Int!
      url: String
    }
 */
case class Picture(width: Int, height: Int, url: Option[String])
