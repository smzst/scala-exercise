package sangria.data

/*
  GraphQL syntax:
    interface Identifiable {
      id: String!
    }
 */
trait Identifiable {
  def id: String
}
