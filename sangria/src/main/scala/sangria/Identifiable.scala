package sangria

/*
  GraphQL syntax:
    interface Identifiable {
      id: String!
    }
 */
trait Identifiable {
  def id: String
}
