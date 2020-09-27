package recompose.ast.view

import recompose.ast.Node
import recompose.ast.attributes.ViewAttributes
import recompose.visitor.Visitor

/**
 * Data class holding values of a parsed `<CardView>`.
 *
 * https://developer.android.com/reference/androidx/cardview/widget/CardView
 */

data class CardViewNode(
    override val view: ViewAttributes
) : Node {
    override fun accept(visitor: Visitor) = visitor.visitCardView(this)
}